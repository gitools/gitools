/*
 *  Copyright 2010 cperez.
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

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.csv.CSVParser;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.utils.CSVStrategies;

/** Read/Write modules from a two columns tabulated file,
 * first column for item and second for module. */

public class ModuleMapText2CPersistence
		extends ModuleMapPersistence<ModuleMap>{

	@Override
	public ModuleMap read(File file, IProgressMonitor monitor) throws PersistenceException {

		// map between the item names and its index

		Map<String, Integer> itemNameToRowMapping = new TreeMap<String, Integer>();

		if (isItemNamesFilterEnabled()) {
			String[] itemNames = getItemNames();
			for (int i = 0; i < itemNames.length; i++)
				itemNameToRowMapping.put(itemNames[i], i);
		}

		// map between modules and item indices

		final Map<String, Set<Integer>> moduleItemsMap =
				new HashMap<String, Set<Integer>>();

		// read mappings

		try {
			monitor.begin("Reading modules ...", 1);

			Reader reader = PersistenceUtils.openReader(file);

			CSVParser parser = new CSVParser(reader, CSVStrategies.TSV);

			readModuleMappings(parser, isItemNamesFilterEnabled(),
					itemNameToRowMapping, moduleItemsMap);

			monitor.end();
		}
		catch (Exception ex) {
			throw new PersistenceException(ex);
		}

		monitor.begin("Filtering modules ...", 1);

		int minSize = getMinSize();
		int maxSize = getMaxSize();

		// create array of item names
		//monitor.debug("isItemNamesFilterEnabled() = " + isItemNamesFilterEnabled());
		//monitor.debug("itemNameToRowMapping.size() = " + itemNameToRowMapping.size());
		String[] itemNames = new String[itemNameToRowMapping.size()];
		for (Map.Entry<String, Integer> entry : itemNameToRowMapping.entrySet()) {
			//monitor.debug(entry.getKey() + " --> " + entry.getValue());
			itemNames[entry.getValue()] = entry.getKey();
		}

		// mask of used items
		BitSet used = new BitSet(itemNames.length);

		// remappend indices
		int lastIndex = 0;
		int[] indexMap = new int[itemNames.length];

		// filter modules by size and identify which items are indexed

		List<String> moduleNames = new ArrayList<String>();
		List<int[]> modulesItemIndices = new ArrayList<int[]>();

		Iterator<Entry<String, Set<Integer>>> it =
				moduleItemsMap.entrySet().iterator();

		while (it.hasNext()) {
			Entry<String, Set<Integer>> entry = it.next();
			Set<Integer> indices = entry.getValue();
			if (indices.size() >= minSize && indices.size() <= maxSize) {
				moduleNames.add(entry.getKey());
				int[] remapedIndices = new int[indices.size()];
				Iterator<Integer> iit = indices.iterator();
				for (int i = 0; i < indices.size(); i++) {
					int index = iit.next();
					if (!used.get(index)) {
						used.set(index);
						indexMap[index] = lastIndex++;
					}

					remapedIndices[i] = indexMap[index];
				}
				modulesItemIndices.add(remapedIndices);
			}
			else
				it.remove();
		}

		// reorder item names according with remaped indices

		String[] finalItemNames = new String[lastIndex];
		for (int i = 0; i < itemNames.length; i++)
			if (used.get(i))
				finalItemNames[indexMap[i]] = itemNames[i];

		monitor.end();

		ModuleMap mmap = new ModuleMap();
		mmap.setItemNames(finalItemNames);
		mmap.setModuleNames(moduleNames.toArray(new String[moduleNames.size()]));
		mmap.setAllItemIndices(modulesItemIndices.toArray(new int[modulesItemIndices.size()][]));
		return mmap;
	}

	protected void readModuleMappings(
			CSVParser parser,
			boolean filterRows,
			Map<String, Integer> itemNameToRowMapping,
			Map<String, Set<Integer>> moduleItemsMap)
			throws PersistenceException {

		try {
			String[] fields;

			while ((fields = parser.getLine()) != null) {
				if (fields.length < 2)
					throw new PersistenceException(
							"At least 2 columns expected at "
							+ parser.getLineNumber()
							+ "(item name and group name).");

				String itemName = fields[0];
				String groupName = fields[1];

				Integer itemIndex = itemNameToRowMapping.get(itemName);
				if (itemIndex == null && !filterRows) {
					itemIndex = itemNameToRowMapping.size();
					itemNameToRowMapping.put(itemName, itemIndex);
				}

				if (itemIndex != null) {
					Set<Integer> itemIndices = moduleItemsMap.get(groupName);
					if (itemIndices == null) {
						itemIndices = new TreeSet<Integer>();
						moduleItemsMap.put(groupName, itemIndices);
					}
					itemIndices.add(itemIndex);
				}
			}
		}
		catch (IOException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void write(File file, ModuleMap moduleMap, IProgressMonitor monitor) throws PersistenceException {

		final String[] moduleNames = moduleMap.getModuleNames();

		int numModules = moduleNames.length;

		monitor.begin("Saving modules...", numModules);

		try {
			Writer writer = PersistenceUtils.openWriter(file);

			final PrintWriter pw = new PrintWriter(writer);

			final String[] itemNames = moduleMap.getItemNames();

			final int[][] indices = moduleMap.getAllItemIndices();

			for (int i = 0; i < numModules; i++) {
				for (int index : indices[i]) {
					pw.print(itemNames[index]);
					pw.print('\t');
					pw.print(moduleNames[i]);
					pw.print('\n');
				}

				monitor.worked(1);
			}

			pw.close();

			monitor.end();
		}
		catch (Exception e) {
			throw new PersistenceException(e);
		}
	}
}
