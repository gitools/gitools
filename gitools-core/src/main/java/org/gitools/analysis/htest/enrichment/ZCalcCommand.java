package org.gitools.analysis.htest.enrichment;

import java.io.File;

import org.gitools.datafilters.ValueParser;
import org.gitools.model.ModuleMap;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.text.DoubleMatrixTextPersistence;
import org.gitools.persistence.text.ModuleMapTextSimplePersistence;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.htest.HtestCommand;
import org.gitools.persistence.xml.EnrichmentAnalysisXmlPersistence;

public class ZCalcCommand extends HtestCommand {

	protected String modulesPath;
	
	public ZCalcCommand(
			EnrichmentAnalysis analysis,
			String dataFile,
			String modulesFile,
			String workdir,
			String fileName,
			String outputFormat,
			boolean resultsByCond) {
		
		super(analysis, dataFile,
				workdir, fileName,
				outputFormat, resultsByCond);

		this.modulesPath = modulesFile;
	}
	
	@Override
	public void run(IProgressMonitor monitor) 
			throws PersistenceException, InterruptedException {
		
		final EnrichmentAnalysis enrichAnalysis = (EnrichmentAnalysis) analysis;
		
		// Load data and modules
		
		monitor.begin("Loading ...", 1);
		monitor.info("Data: " + dataPath);
		monitor.info("Modules: " + modulesPath);
		
		DoubleMatrix doubleMatrix = new DoubleMatrix();
		ModuleMap moduleMap = new ModuleMap();
		loadDataAndModules(
				doubleMatrix, moduleMap, 
				dataPath, createValueParser(analysis),
				modulesPath,
				enrichAnalysis.getMinModuleSize(),
				enrichAnalysis.getMaxModuleSize(),
				!enrichAnalysis.isDiscardNonMappedRows(),
				monitor.subtask());

		enrichAnalysis.setDataMatrix(doubleMatrix);
		enrichAnalysis.setModuleMap(moduleMap);
		
		monitor.end();
		
		// Create and process analysis
		
		ZCalcProcessor processor = new ZCalcProcessor(enrichAnalysis);
		
		processor.run(monitor);

		save(enrichAnalysis, monitor);
	}

	private void save(final EnrichmentAnalysis enrichAnalysis, IProgressMonitor monitor) throws PersistenceException {

		File workdirFile = new File(workdir);
		if (!workdirFile.exists())
			workdirFile.mkdirs();

		File enrichmentFile = new File(workdirFile, fileName + ".enrichment");
		EnrichmentAnalysisXmlPersistence p = new EnrichmentAnalysisXmlPersistence();
		p.setRecursivePersistence(true);
		p.write(enrichmentFile, enrichAnalysis, monitor);
	}

	public String getModulesFile() {
		return modulesPath;
	}

	public void setModulesFile(String modulesFile) {
		this.modulesPath = modulesFile;
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
}
