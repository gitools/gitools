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
import org.gitools.stats.test.factory.ZscoreTestFactory;
import org.gitools.tool.processors.ZCalcProcessor;

import es.imim.bg.progressmonitor.ProgressMonitor;

public class ZCalcCommand extends AnalysisCommand {
	
	public ZCalcCommand(String analysisName, String testName,
			int samplingNumSamples, String dataFile, ValueFilter valueFilter, 
			String groupsFile, int minGroupSize, int maxModuleSize, boolean includeNonMappedItems, 
			String workdir, String outputFormat, boolean resultsByCond) {
		
		super(analysisName, testName, samplingNumSamples, dataFile, valueFilter, 
				groupsFile, minGroupSize, maxModuleSize, includeNonMappedItems,
				workdir, outputFormat, resultsByCond);
	}
	
	public void run(ProgressMonitor monitor) 
			throws IOException, DataFormatException, InterruptedException {
		
		// Prepare output
		
		//AnalysisResource output = createOutput(outputFormat);
		
		// Prepare test factory
		
		TestFactory testFactory = 
			createTestFactory(ToolConfig.ZETCALC, testName);
		
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
		
		//FIXME: id
		Analysis analysis = new Analysis(null, null);
		analysis.setName(analysisName);
		analysis.setToolConfig(testFactory.getTestConfig());
		analysis.setDataTable(dataMatrix);
		analysis.setModuleSet(moduleMap);
		analysis.getToolConfig().put(
				ZscoreTestFactory.NUM_SAMPLES_PROPERTY,
				String.valueOf(samplingNumSamples));
		
		ZCalcProcessor processor = 
			new ZCalcProcessor(analysis);
		
		processor.run(monitor);
		
		// Save analysis
		
		save(analysis, monitor);
	}

	private void loadDataAndModules(
			DataMatrix dataMatrix, ModuleMap moduleMap,
			String dataFileName, ValueFilter valueFilter, String modulesFileName, 
			int minModuleSize, int maxModuleSize, boolean includeNonMappedItems,
			ProgressMonitor monitor) throws FileNotFoundException, IOException, DataFormatException {
		
		// Load metadata
		
		DataResource dataResource = new DataResource(dataFileName);
		dataResource.loadMetadata(dataMatrix, valueFilter, monitor);
		
		// Load modules
		
		File file = new File(modulesFileName);
		moduleMap.setName(file.getName());
		
		ModuleMapResource moduleMapResource = new ModuleMapResource(file);
		moduleMapResource.load(
			moduleMap,
			minModuleSize,
			maxModuleSize,
			dataMatrix.getRowNames(),
			includeNonMappedItems,
			monitor);
		
		dataMatrix.setRowNames(moduleMap.getItemNames());
		
		// Load data
		
		dataResource.loadData(
				dataMatrix,
				valueFilter,
				null, 
				moduleMap.getItemsOrder(), 
				monitor);
		
	}

	/*private AnalysisResource createOutput(String outputFormat) {
		AnalysisResource output = null;
		
		if (outputFormat.equalsIgnoreCase("csv"))
			output = new TabAnalysisResource(workdir, resultsByCond, defaultSep, defaultQuote);
		else if (outputFormat.equalsIgnoreCase("rexml"))
			output = new REXmlAnalysisResource(workdir, minModuleSize, maxModuleSize);
		else
			throw new IllegalArgumentException("Unknown output format '" + outputFormat + "'");
		
		return output;
	}*/
}
