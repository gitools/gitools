package org.gitools.persistence.text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.AbstractEntityPersistence;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.utils.CSVStrategies;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class ModuleMapTextIndicesPersistence
		extends AbstractEntityPersistence<ModuleMap> {

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
				pw.print(itemNames[0]);
	
				for (int i = 1; i < itemNames.length; i++) {
					pw.print("\t\"");
					pw.print(itemNames[i]);
					pw.print('"');
				}
			}
			pw.print('\n');
			
			final String[] moduleNames = moduleMap.getModuleNames();
			
			final int[][] indices = moduleMap.getItemIndices();
			
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

		monitor.begin("Reading modules names ...", 1);

		String[] fields;
		final List<String> moduleNames = new ArrayList<String>();
		final List<int[]> itemIndices = new ArrayList<int[]>();

		while ((fields = parser.getLine()) != null) {

			moduleNames.add(fields[0]);

			int[] items = new int[fields.length - 1];

			for (int j = 1; j < fields.length; j++) {
				items[j - 1] = Integer.parseInt(fields[j]);
			}
			itemIndices.add(items);

		}
		
		moduleMap.setModuleNames(
				moduleNames.toArray(new String[moduleNames.size()]));
		
		moduleMap.setItemIndices(
				itemIndices.toArray(new int[moduleNames.size()][]));

		monitor.info(moduleNames.size() + " modules");

		monitor.end();
	}
}
