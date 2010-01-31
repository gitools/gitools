package org.gitools.analysis.htest.enrichment;

import java.io.File;

import org.gitools.datafilters.ValueTranslator;
import org.gitools.model.ModuleMap;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.text.DoubleMatrixTextPersistence;
import org.gitools.persistence.text.ModuleMapTextSimplePersistence;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.htest.HtestCommand;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.text.DoubleBinaryMatrixTextPersistence;
import org.gitools.persistence.text.MatrixTextPersistence;
import org.gitools.persistence.xml.EnrichmentAnalysisXmlPersistence;

public class EnrichmentCommand extends HtestCommand {

	protected String modulesPath;
	
	public EnrichmentCommand(
			EnrichmentAnalysis analysis,
			String dataMime,
			String dataFile,
			String modulesFile,
			String workdir,
			String fileName,
			String outputFormat,
			boolean resultsByCond) {
		
		super(analysis, dataMime, dataFile,
				workdir, fileName, outputFormat, resultsByCond);

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
				dataMime, dataPath,
				createValueParser(analysis),
				modulesPath,
				enrichAnalysis.getMinModuleSize(),
				enrichAnalysis.getMaxModuleSize(),
				!enrichAnalysis.isDiscardNonMappedRows(),
				monitor.subtask());

		enrichAnalysis.setDataMatrix(doubleMatrix);
		enrichAnalysis.setModuleMap(moduleMap);
		
		monitor.end();
		
		// Create and process analysis
		
		EnrichmentProcessor processor = new EnrichmentProcessor(enrichAnalysis);
		
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
			String dataFileMime,
			String dataFileName,
			ValueTranslator valueTranslator,
			String modulesFileName, 
			int minModuleSize,
			int maxModuleSize,
			boolean includeNonMappedItems,
			IProgressMonitor monitor)
			throws PersistenceException {
		
		// Load metadata
		
		File dataFile = new File(dataFileName);

		MatrixTextPersistence dmPersistence = null;

		if (dataFileMime.equals(MimeTypes.DOUBLE_MATRIX))
			dmPersistence = new DoubleMatrixTextPersistence();
		else if (dataFileMime.equals(MimeTypes.BINARY_MATRIX))
			dmPersistence = new DoubleBinaryMatrixTextPersistence();
		else
			throw new PersistenceException("Unsupported mime type: " + dataFileMime);

		dmPersistence.readMetadata(dataFile, doubleMatrix, monitor);
		String[] rows = doubleMatrix.getRowStrings();

		// Load modules
		
		File file = new File(modulesFileName);
		moduleMap.setTitle(file.getName());
		
		ModuleMapTextSimplePersistence moduleMapTextSimplePersistence = new ModuleMapTextSimplePersistence(file);
		moduleMapTextSimplePersistence.load(
			moduleMap,
			minModuleSize,
			maxModuleSize,
			rows,
			includeNonMappedItems,
			monitor);
		
		doubleMatrix.setRows(moduleMap.getItemNames());
		
		// Load data
		
		dmPersistence.readData(
				dataFile,
				doubleMatrix,
				valueTranslator,
				null, 
				moduleMap.getItemsOrder(), 
				monitor);
		
	}
}
