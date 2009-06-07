package org.gitools.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.gitools.datafilters.ValueFilter;
import org.gitools.model.Analysis;
import org.gitools.model.DataMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.model.ToolConfig;
import org.gitools.resources.DataResource;
import org.gitools.resources.ModuleMapResource;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.tool.processors.OncozProcessor;

import es.imim.bg.progressmonitor.ProgressMonitor;

public class OncozCommand extends AnalysisCommand {
	
	public OncozCommand(String analysisName, String testName,
			int samplingNumSamples, String dataFile, ValueFilter valueFilter, 
			String groupsFile, int minModuleSize, int maxModuleSize, 
			String workdir, String outputFormat, boolean resultsByCond) {
		
		super(analysisName, testName, samplingNumSamples, dataFile, valueFilter, 
				groupsFile, minModuleSize, maxModuleSize, true,
				workdir, outputFormat, resultsByCond);
	}

	@Override
	public void run(ProgressMonitor monitor) 
			throws IOException, DataFormatException, InterruptedException {
		
		// Prepare test factory
		
		TestFactory testFactory = 
			createTestFactory(ToolConfig.ONCOZET, testName);
		
		// Load data and modules
		
		monitor.begin("Loading ...", 1);
		monitor.info("Data: " + dataFile);
		monitor.info("Modules: " + modulesFile);
		
		DataMatrix dataMatrix = new DataMatrix();
		//FIXME: id
		ModuleMap moduleMap = new ModuleMap(null, null);
		loadDataAndModules(
				dataMatrix, moduleMap, 
				dataFile, valueFilter, 
				modulesFile, minModuleSize, maxModuleSize,
				includeNonMappedItems,
				monitor.subtask());
		
		monitor.end();
		
		// Create and process analysis
		
		//FIXME id
		Analysis analysis = new Analysis(null, null);
		analysis.setName(analysisName);
		analysis.setToolConfig(testFactory.getTestConfig());
		analysis.setDataTable(dataMatrix);
		analysis.setModuleSet(moduleMap);
		
		OncozProcessor processor = 
			new OncozProcessor(analysis);
		
		processor.run(monitor);
		
		// Save analysis
		
		save(analysis, monitor);
	}

	private void loadDataAndModules(
			DataMatrix dataMatrix, ModuleMap moduleMap,
			String dataFileName, ValueFilter valueFilter, 
			String modulesFileName,	int minModuleSize, int maxModuleSize,
			boolean includeNonMappedItems,
			ProgressMonitor monitor) throws FileNotFoundException, IOException, DataFormatException {
		
		// Load metadata
		
		DataResource dataResource = new DataResource(dataFileName);
		dataResource.loadMetadata(dataMatrix, valueFilter, monitor);
		
		// Load modules
		
		if (modulesFileName != null) {
			File file = new File(modulesFileName);
			moduleMap.setName(file.getName());
			
			ModuleMapResource moduleMapResource = new ModuleMapResource(file);
			moduleMapResource.load(
				moduleMap,
				minModuleSize,
				maxModuleSize,
				dataMatrix.getColNames(),
				includeNonMappedItems,
				monitor);
		}
		else {
			moduleMap.setItemNames(dataMatrix.getColNames());
			moduleMap.setModuleNames(new String[] {"all"});
			int num = dataMatrix.getColNames().length;
			int[][] indices = new int[1][num];
			for (int i = 0; i < num; i++)
				indices[0][i] = i;
			moduleMap.setItemIndices(indices);
		}
		
		// Load data
		
		dataResource.loadData(
				dataMatrix,
				valueFilter,
				moduleMap.getItemsOrder(),
				null,
				monitor);		
	}
}
