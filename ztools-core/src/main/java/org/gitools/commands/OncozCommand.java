package org.gitools.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.gitools.datafilters.ValueFilter;
import org.gitools.model.Analysis;
import org.gitools.model.ModuleMap;
import org.gitools.model.ToolConfig;
import org.gitools.model.table.DoubleMatrix;
import org.gitools.resources.DataResource;
import org.gitools.resources.ModuleMapResource;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.tool.processors.OncozProcessor;

import edu.upf.bg.progressmonitor.ProgressMonitor;

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
		
		DoubleMatrix doubleMatrix = new DoubleMatrix();
		//FIXME: id
		ModuleMap moduleMap = new ModuleMap(null, null);
		loadDataAndModules(
				doubleMatrix, moduleMap, 
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
		analysis.setDataTable(doubleMatrix);
		analysis.setModuleSet(moduleMap);
		
		OncozProcessor processor = 
			new OncozProcessor(analysis);
		
		processor.run(monitor);
		
		// Save analysis
		
		save(analysis, monitor);
	}

	private void loadDataAndModules(
			DoubleMatrix doubleMatrix, ModuleMap moduleMap,
			String dataFileName, ValueFilter valueFilter, 
			String modulesFileName,	int minModuleSize, int maxModuleSize,
			boolean includeNonMappedItems,
			ProgressMonitor monitor) throws FileNotFoundException, IOException, DataFormatException {
		
		// Load metadata
		
		DataResource dataResource = new DataResource(dataFileName);
		dataResource.loadMetadata(doubleMatrix, valueFilter, monitor);
		
		// Load modules
		
		if (modulesFileName != null) {
			File file = new File(modulesFileName);
			moduleMap.setName(file.getName());
			
			ModuleMapResource moduleMapResource = new ModuleMapResource(file);
			moduleMapResource.load(
				moduleMap,
				minModuleSize,
				maxModuleSize,
				doubleMatrix.getColNames(),
				includeNonMappedItems,
				monitor);
		}
		else {
			moduleMap.setItemNames(doubleMatrix.getColNames());
			moduleMap.setModuleNames(new String[] {"all"});
			int num = doubleMatrix.getColNames().length;
			int[][] indices = new int[1][num];
			for (int i = 0; i < num; i++)
				indices[0][i] = i;
			moduleMap.setItemIndices(indices);
		}
		
		// Load data
		
		dataResource.loadData(
				doubleMatrix,
				valueFilter,
				moduleMap.getItemsOrder(),
				null,
				monitor);		
	}
}
