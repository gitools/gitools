package org.gitools.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.gitools.model.ModuleMap;
import org.gitools.resources.IResource;
import org.gitools.utils.CSVStrategies;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class ModuleMapPersistence implements IEntityPersistence<ModuleMap> {

	private static final CSVStrategy csvStrategy = CSVStrategies.TSV;

	public ModuleMapPersistence() {

	}

	@Override
	public ModuleMap read(IResource resource, IProgressMonitor monitor)
			throws PersistenceException {

		Reader reader;

		try {
			reader = resource.openReader();
		} catch (Exception e) {
			throw new PersistenceException("Error opening resource: "
					+ resource.toURI(), e);
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
		return moduleMap;

	}

	@Override
	public void write(IResource resource, ModuleMap entity,
			IProgressMonitor monitor) throws PersistenceException {
		// TODO Auto-generated method stub

	}

	private void loadItemNames(ModuleMap moduleMap, IProgressMonitor monitor,
			CSVParser parser) throws FileNotFoundException, IOException,
			DataFormatException {

		monitor.begin("Reading  item names ...", 1);

		final String[] itemNames = parser.getLine();

		moduleMap.setItemNames(itemNames);

		monitor.info(itemNames.length + " items");

		monitor.end();

	}

	private void loadModules(ModuleMap moduleMap, IProgressMonitor monitor,
			CSVParser parser) throws NumberFormatException, IOException {

		monitor.begin("Reading  modules names ...", 1);

		String[] fields;
		final List<String> moduleNames = new ArrayList<String>();
		final List<int[]> itemIndices = new ArrayList<int[]>();

		int i = 0;
		while ((fields = parser.getLine()) != null) {

			moduleNames.add(fields[0]);

			int[] items = new int[fields.length];
			items[0] = i;

			for (int j = 1; j < fields.length; j++) {
				items[j] = Integer.parseInt(fields[j]);
			}
			itemIndices.add(items);
			i++;

		}
		
		moduleMap.setModuleNames(moduleNames.toArray(new String[0]));
		moduleMap.setItemIndices(itemIndices.toArray(new int[0][]));

		monitor.info(moduleNames.size() + " modules");

		monitor.end();

	}

}
