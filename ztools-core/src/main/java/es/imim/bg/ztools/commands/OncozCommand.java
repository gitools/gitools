package es.imim.bg.ztools.commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Data;
import es.imim.bg.ztools.model.Modules;
import es.imim.bg.ztools.processors.OncozProcessor;
import es.imim.bg.ztools.resources.DataResource;
import es.imim.bg.ztools.resources.ModulesResource;
import es.imim.bg.ztools.test.factory.TestFactory;

public class OncozCommand extends AnalysisCommand {
	
	public OncozCommand(String analysisName, String testName,
			int samplingNumSamples, String dataFile, String groupsFile,
			int minModuleSize, int maxModuleSize, String workdir,
			String outputFormat, boolean resultsByCond) {
		
		super(analysisName, testName, samplingNumSamples, dataFile, groupsFile,
				minModuleSize, maxModuleSize, workdir, outputFormat, resultsByCond);
	}

	@Override
	public void run(ProgressMonitor monitor) 
			throws IOException, DataFormatException, InterruptedException {
		
		// Prepare test factory
		
		TestFactory testFactory = createTestFactory(testName);
		testFactory.getTestConfig().setName("oncozet");
		
		// Load data and modules
		
		monitor.begin("Loading ...", 1);
		monitor.info("Data: " + dataFile);
		monitor.info("Modules: " + modulesFile);
		
		Data data = new Data();
		Modules modules = new Modules();
		loadDataAndModules(
				data, modules, 
				dataFile, modulesFile, 
				minModuleSize, maxModuleSize,
				monitor.subtask());
		
		monitor.end();
		
		// Create and process analysis
		
		Analysis analysis = new Analysis();
		analysis.setName(analysisName);
		analysis.setToolConfig(testFactory.getTestConfig());
		analysis.setData(data);
		analysis.setModules(modules);
		
		OncozProcessor processor = 
			new OncozProcessor(analysis);
		
		processor.run(monitor);
		
		// Save analysis
		
		save(analysis, monitor);
	}

	private void loadDataAndModules(
			Data data, Modules modules,
			String dataFileName, String modulesFileName, 
			int minModuleSize, int maxModuleSize, 
			ProgressMonitor monitor) throws FileNotFoundException, IOException, DataFormatException {
		
		// Load metadata
		
		DataResource dataResource = new DataResource(dataFileName);
		dataResource.loadMetadata(data, monitor);
		
		// Load modules
		
		if (modulesFileName != null) {
			ModulesResource modulesResource = new ModulesResource(modulesFileName);
			modulesResource.load(
				modules,
				minModuleSize,
				maxModuleSize,
				data.getColNames(),
				monitor);
		}
		else {
			modules.setItemNames(data.getColNames());
			modules.setModuleNames(new String[] {"all"});
			int num = data.getColNames().length;
			int[][] indices = new int[1][num];
			for (int i = 0; i < num; i++)
				indices[0][i] = i;
			modules.setItemIndices(indices);
		}
		
		// Load data
		
		dataResource.loadData(
				data,
				modules.getItemsOrder(),
				null,
				monitor);		
	}
}