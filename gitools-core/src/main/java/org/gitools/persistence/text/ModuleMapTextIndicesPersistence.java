/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.persistence.text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.zip.DataFormatException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.utils.CSVStrategies;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ModuleMapTextIndicesPersistence
		extends ModuleMapPersistence<ModuleMap> {

	private static final CSVStrategy csvStrategy = CSVStrategies.TSV;

	public ModuleMapTextIndicesPersistence() {
	}

	@Override
	public ModuleMap read(File file, IProgressMonitor monitor)
			throws PersistenceException {

		Reader reader;

		monitor.begin("Loading modules...", 1);
		monitor.info("File: " + file.getAbsolutePath());

		try {
			reader = PersistenceUtils.openReader(file);
		} catch (Exception e) {
			throw new PersistenceException("Error opening resource: "
					+ file.getName(), e);
		}

		CSVParser parser = new CSVParser(reader, csvStrategy);
		
		ModuleMap moduleMap = new ModuleMap();

		try {
			loadItemNames(moduleMap, monitor, parser);
			loadModules(moduleMap, monitor, parser);
			reader.close();
		} catch (Exception e) {
			throw new PersistenceException(e);
		}

		monitor.end();
		
		return moduleMap;
	}

	@Override
	public void write(
			File file,
			ModuleMap moduleMap,
			IProgressMonitor monitor) throws PersistenceException {

		monitor.begin("Saving modules...", moduleMap.getModuleNames().length);
		monitor.info("File: " + file.getAbsolutePath());

		try {
			final PrintWriter pw = new PrintWriter(
					PersistenceUtils.openWriter(file));
			
			final String[] itemNames = moduleMap.getItemNames();
			
			if (itemNames.length > 0) {
				pw.print('"');
				pw.print(itemNames[0]);
				pw.print('"');
	
				for (int i = 1; i < itemNames.length; i++) {
					pw.print("\t\"");
					pw.print(itemNames[i]);
					pw.print('"');
				}
			}
			pw.print('\n');
			
			final String[] moduleNames = moduleMap.getModuleNames();
			
			final int[][] indices = moduleMap.getAllItemIndices();
			
			int numModules = moduleNames.length;
			
			for (int i = 0; i < numModules; i++) {
				pw.print('"');
				pw.print(moduleNames[i]);
				pw.print('"');
				
				for (int index : indices[i]) {
					pw.print('\t');
					pw.print(index);
				}
				
				pw.print('\n');

				monitor.worked(1);
			}
			
			pw.close();
		}
		catch (Exception e) {
			throw new PersistenceException(e);
		}
		finally {
			monitor.end();
		}
	}

	private void loadItemNames(ModuleMap moduleMap, IProgressMonitor monitor,
			CSVParser parser) throws FileNotFoundException, IOException,
			DataFormatException {

		monitor.begin("Reading item names ...", 1);

		final String[] itemNames = parser.getLine();

		moduleMap.setItemNames(itemNames);

		monitor.info(itemNames.length + " items");

		monitor.end();

	}

	private void loadModules(ModuleMap moduleMap, IProgressMonitor monitor,
			CSVParser parser) throws NumberFormatException, IOException {

		monitor.begin("Reading modules ...", 1);

		String[] itemNames = moduleMap.getItemNames();

		// Prepare valid item names depending on whether item name filtering is enabled or not

		BitSet valid = new BitSet(itemNames.length);

		if (isItemNamesFilterEnabled()) {
			Map<String, Integer> itemIndices = new HashMap<String, Integer>();
			for (int i = 0; i < itemNames.length; i++)
				itemIndices.put(itemNames[i], i);

			for (String name : getItemNames()) {
				Integer index = itemIndices.get(name);
				if (index != null)
					valid.set(index);
			}
		}
		else
			valid.set(0, itemNames.length);

		// Load mapping and mark items used

		BitSet used = new BitSet(itemNames.length);

		String[] fields;
		final Map<String, Set<Integer>> mapItemIndices = new HashMap<String, Set<Integer>>();

		int minSize = getMinSize();
		int maxSize = getMaxSize();

		while ((fields = parser.getLine()) != null) {

			String moduleName = fields[0];

			Set<Integer> items = new HashSet<Integer>();

			for (int j = 1; j < fields.length; j++) {
				int index = Integer.parseInt(fields[j]);
				boolean inRange = index >= 0 && index < itemNames.length;
				if (inRange && valid.get(index)) {
					items.add(index);
					used.set(index);
				}
			}

			if (items.size() >= minSize && items.size() <= maxSize)
				mapItemIndices.put(moduleName, items);
			else
				items.clear();
		}

		// Remap indices as there are items that may not be used

		int lastIndex = 0;
		int[] indexMap = new int[itemNames.length];
		for (int i = 0; i < itemNames.length; i++)
			if (used.get(i))
				indexMap[i] = lastIndex++;

		int i = 0;
		String[] finalItemNames = new String[lastIndex];
		for (int j = 0; j < itemNames.length; j++)
			if (used.get(j))
				finalItemNames[i++] = itemNames[j];

		i = 0;
		String[] moduleNames = new String[mapItemIndices.size()];

		int[][] moduleItemIndices = new int[moduleNames.length][];

		for (Map.Entry<String, Set<Integer>> entry : mapItemIndices.entrySet()) {
			moduleNames[i] = entry.getKey();

			int[] indices = new int[entry.getValue().size()];
			Iterator<Integer> it = entry.getValue().iterator();
			for (int j = 0; j < indices.length; j++)
				indices[j] = indexMap[it.next()];
			moduleItemIndices[i] = indices;
			i++;
		}

		moduleMap.setItemNames(finalItemNames);
		moduleMap.setModuleNames(moduleNames);
		moduleMap.setAllItemIndices(moduleItemIndices);

		monitor.info(moduleNames.length + " modules and " + finalItemNames.length + " items annotated");

		monitor.end();
	}
}
