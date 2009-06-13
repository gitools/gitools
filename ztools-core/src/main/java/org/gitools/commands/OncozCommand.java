package org.gitools.commands;

import java.io.File;

import org.gitools.datafilters.ValueFilter;
import org.gitools.model.ModuleMap;
import org.gitools.model.ToolConfig;
import org.gitools.model.analysis.Analysis;
import org.gitools.model.matrix.DoubleMatrix;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.SimpleMapPersistence;
import org.gitools.persistence.TextDoubleMatrixPersistence;
import org.gitools.resources.FileResource;
import org.gitools.resources.IResource;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.tool.processors.OncozProcessor;

import edu.upf.bg.progressmonitor.IProgressMonitor;

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
	public void run(IProgressMonitor monitor) 
			throws PersistenceException, InterruptedException {
		
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
			IProgressMonitor monitor)
			throws PersistenceException {
		
		// Load metadata
		
		IResource resource = new FileResource(dataFileName);
		TextDoubleMatrixPersistence dmPersistence = new TextDoubleMatrixPersistence();
		dmPersistence.readMetadata(resource, doubleMatrix, valueFilter, monitor);
		
		// Load modules
		
		if (modulesFileName != null) {
			File file = new File(modulesFileName);
			moduleMap.setName(file.getName());
			
			SimpleMapPersistence simpleMapPersistence = new SimpleMapPersistence(file);
			simpleMapPersistence.load(
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
		
		dmPersistence.readData(
				resource,
				doubleMatrix,
				valueFilter,
				moduleMap.getItemsOrder(),
				null,
				monitor);		
	}
}
