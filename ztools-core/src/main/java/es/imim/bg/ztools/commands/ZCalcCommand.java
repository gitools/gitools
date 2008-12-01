package es.imim.bg.ztools.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.DataMatrix;
import es.imim.bg.ztools.model.ModuleSet;
import es.imim.bg.ztools.processors.ZCalcProcessor;
import es.imim.bg.ztools.resources.DataResource;
import es.imim.bg.ztools.resources.ModulesResource;
import es.imim.bg.ztools.test.factory.TestFactory;

public class ZCalcCommand extends AnalysisCommand {
	
	public ZCalcCommand(String analysisName, String testName,
			int samplingNumSamples, String dataFile, String groupsFile,
			int minGroupSize, int maxModuleSize, String workdir,
			String outputFormat, boolean resultsByCond) {
		
		super(analysisName, testName, samplingNumSamples, dataFile, groupsFile,
				minGroupSize, maxModuleSize, workdir, outputFormat, resultsByCond);
	}
	
	public void run(ProgressMonitor monitor) 
			throws IOException, DataFormatException, InterruptedException {
		
		// Prepare output
		
		//AnalysisResource output = createOutput(outputFormat);
		
		// Prepare test factory
		
		TestFactory testFactory = createTestFactory(testName);
		testFactory.getTestConfig().setName("zetcalc");
		
		// Load data and modules
		
		monitor.begin("Loading ...", 1);
		monitor.info("Data: " + dataFile);
		monitor.info("Modules: " + modulesFile);
		
		DataMatrix dataMatrix = new DataMatrix();
		ModuleSet moduleSet = new ModuleSet();
		loadDataAndModules(
				dataMatrix, moduleSet, 
				dataFile, modulesFile, 
				minModuleSize, maxModuleSize,
				monitor.subtask());
		
		monitor.end();
		
		// Create and process analysis
		
		Analysis analysis = new Analysis();
		analysis.setName(analysisName);
		analysis.setToolConfig(testFactory.getTestConfig());
		analysis.setDataMatrix(dataMatrix);
		analysis.setModuleSet(moduleSet);
		
		ZCalcProcessor processor = 
			new ZCalcProcessor(analysis);
		
		processor.run(monitor);
		
		// Save analysis
		
		save(analysis, monitor);
	}

	private void loadDataAndModules(
			DataMatrix dataMatrix, ModuleSet moduleSet,
			String dataFileName, String modulesFileName, 
			int minModuleSize, int maxModuleSize, 
			ProgressMonitor monitor) throws FileNotFoundException, IOException, DataFormatException {
		
		// Load metadata
		
		DataResource dataResource = new DataResource(dataFileName);
		dataResource.loadMetadata(dataMatrix, monitor);
		
		// Load modules
		
		File file = new File(modulesFileName);
		moduleSet.setName(file.getName());
		
		ModulesResource modulesResource = new ModulesResource(file);
		modulesResource.load(
			moduleSet,
			minModuleSize,
			maxModuleSize,
			dataMatrix.getRowNames(),
			monitor);
		
		// Load data
		
		dataResource.loadData(
				dataMatrix,
				null, 
				moduleSet.getItemsOrder(), 
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
