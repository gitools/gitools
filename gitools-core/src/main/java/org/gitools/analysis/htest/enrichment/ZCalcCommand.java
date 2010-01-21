package org.gitools.analysis.htest.enrichment;

import java.io.File;

import org.gitools.datafilters.ValueParser;
import org.gitools.model.ModuleMap;
import org.gitools.model.ToolConfig;
import org.gitools.model.Analysis;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.text.DoubleMatrixTextPersistence;
import org.gitools.persistence.text.ModuleMapTextSimplePersistence;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.stats.test.factory.ZscoreTestFactory;
import org.gitools.analysis.htest.enrichment.ZCalcProcessor;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.htest.AnalysisCommand;

public class ZCalcCommand extends AnalysisCommand {
	
	public ZCalcCommand() {
		super();
	}
	
	public ZCalcCommand(String analysisName, String testName,
			int samplingNumSamples, String dataFile, ValueParser valueParser, 
			String groupsFile, int minGroupSize, int maxModuleSize, boolean includeNonMappedItems, 
			String workdir, String outputFormat, boolean resultsByCond) {
		
		super(analysisName, testName, samplingNumSamples, dataFile, valueParser, 
				groupsFile, minGroupSize, maxModuleSize, includeNonMappedItems,
				workdir, outputFormat, resultsByCond);
	}
	
	public void run(IProgressMonitor monitor) 
			throws PersistenceException, InterruptedException {
		
		// Prepare output
		
		//AnalysisResource output = createOutput(outputFormat);
		
		// Prepare test factory
		
		TestFactory testFactory = 
			createTestFactory(ToolConfig.ZETCALC, testName);
		
		// Load data and modules
		
		monitor.begin("Loading ...", 1);
		monitor.info("Data: " + dataFile);
		monitor.info("Modules: " + modulesFile);
		
		DoubleMatrix doubleMatrix = new DoubleMatrix();
		//FIXME: id
		ModuleMap moduleMap = new ModuleMap();
		loadDataAndModules(
				doubleMatrix, moduleMap, 
				dataFile, valueParser, 
				modulesFile, minModuleSize, maxModuleSize,
				includeNonMappedItems,
				monitor.subtask());
		
		monitor.end();
		
		// Create and process analysis
		
		Analysis analysis = new Analysis();
		analysis.setTitle(title);
		analysis.setToolConfig(testFactory.getTestConfig());
		analysis.setDataTable(doubleMatrix);
		analysis.setModuleMap(moduleMap);
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
			DoubleMatrix doubleMatrix,
			ModuleMap moduleMap,
			String dataFileName,
			ValueParser valueParser,
			String modulesFileName, 
			int minModuleSize,
			int maxModuleSize,
			boolean includeNonMappedItems,
			IProgressMonitor monitor)
			throws PersistenceException {
		
		// Load metadata
		
		File resource = new File(dataFileName);
		
		DoubleMatrixTextPersistence dmPersistence = new DoubleMatrixTextPersistence();
		dmPersistence.readMetadata(resource, doubleMatrix, valueParser, monitor);
		
		// Load modules
		
		File file = new File(modulesFileName);
		moduleMap.setTitle(file.getName());
		
		ModuleMapTextSimplePersistence moduleMapTextSimplePersistence = new ModuleMapTextSimplePersistence(file);
		moduleMapTextSimplePersistence.load(
			moduleMap,
			minModuleSize,
			maxModuleSize,
			doubleMatrix.getRowStrings(),
			includeNonMappedItems,
			monitor);
		
		doubleMatrix.setRows(moduleMap.getItemNames());
		
		// Load data
		
		dmPersistence.readData(
				resource,
				doubleMatrix,
				valueParser,
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
