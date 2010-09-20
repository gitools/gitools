package org.gitools.analysis.htest.enrichment;

import java.io.File;
import java.util.Arrays;

import org.gitools.model.ModuleMap;
import org.gitools.persistence.PersistenceException;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.htest.HtestCommand;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.text.MatrixTextPersistence;
import org.gitools.persistence.text.ModuleMapPersistence;
import org.gitools.persistence.xml.EnrichmentAnalysisXmlPersistence;

public class EnrichmentCommand extends HtestCommand {

	protected String modulesMime;
	protected String modulesPath;
	
	public EnrichmentCommand(
			EnrichmentAnalysis analysis,
			String dataMime,
			String dataFile,
			String populationPath,
			String modulesMime,
			String modulesFile,
			String workdir,
			String fileName) {
		
		super(analysis, dataMime, dataFile,
				populationPath, workdir, fileName);

		this.modulesMime = modulesMime;
		this.modulesPath = modulesFile;
	}
	
	@Override
	public void run(IProgressMonitor monitor) throws AnalysisException {

		try {
			final EnrichmentAnalysis enrichAnalysis = (EnrichmentAnalysis) analysis;

			// Load data and modules

			monitor.begin("Loading ...", 1);
			monitor.info("Data: " + dataPath);
			monitor.info("Modules: " + modulesPath);

			loadDataAndModules(
					dataMime, dataPath,
					populationPath,
					modulesMime, modulesPath,
					enrichAnalysis,
					monitor.subtask());

			monitor.end();

			// Create and process analysis

			EnrichmentProcessor processor = new EnrichmentProcessor(enrichAnalysis);

			processor.run(monitor);

			save(enrichAnalysis, monitor);
		}
		catch (Throwable ex) {
			throw new AnalysisException(ex);
		}
	}

	private void save(final EnrichmentAnalysis analysis, IProgressMonitor monitor) throws PersistenceException {

		File workdirFile = new File(workdir);
		if (!workdirFile.exists())
			workdirFile.mkdirs();

		File file = new File(workdirFile, fileName);
		EnrichmentAnalysisXmlPersistence p = new EnrichmentAnalysisXmlPersistence();
		p.setRecursivePersistence(true);
		p.write(file, analysis, monitor);
	}

	/** Loads data and modules taking into account filtering
	 *
	 * @param dataFileMime
	 * @param dataFileName
	 * @param modulesFileMime
	 * @param modulesFileName
	 * @param analysis
	 * @param monitor
	 * @throws PersistenceException
	 */
	private void loadDataAndModules(
			String dataFileMime,
			String dataFileName,
			String populationFileName,
			String modulesFileMime,
			String modulesFileName,
			EnrichmentAnalysis analysis,
			IProgressMonitor monitor)
			throws PersistenceException {

		// Load background population

		String[] populationLabels = null;

		if (populationFileName != null) {
			File bgFile = new File(populationFileName);

			List<String> popLabels = (List<String>) PersistenceManager.getDefault()
					.load(bgFile, MimeTypes.GENE_SET, monitor);

			populationLabels = popLabels.toArray(new String[popLabels.size()]);
		}

		// Load data
		
		File dataFile = new File(dataFileName);

		ValueTranslator valueTranslator = createValueTranslator(analysis);

		Properties dataProps = new Properties();
		dataProps.put(MatrixTextPersistence.BINARY_VALUES, analysis.isBinaryCutoffEnabled());
		dataProps.put(MatrixTextPersistence.VALUE_TRANSLATOR, valueTranslator);
		if (populationLabels != null)
			dataProps.put(MatrixTextPersistence.POPULATION_LABELS, populationLabels);

		BaseMatrix dataMatrix = loadDataMatrix(dataFile, dataFileMime, dataProps, monitor);

		PersistenceManager.getDefault().clearEntityCache(dataMatrix);

		// Load modules

		File file = new File(modulesFileName);

		Properties modProps = new Properties();
		modProps.put(ModuleMapPersistence.ITEM_NAMES_FILTER_ENABLED, true);
		modProps.put(ModuleMapPersistence.ITEM_NAMES, dataMatrix.getRowStrings());
		modProps.put(ModuleMapPersistence.MIN_SIZE, analysis.getMinModuleSize());
		modProps.put(ModuleMapPersistence.MAX_SIZE, analysis.getMaxModuleSize());

		ModuleMap moduleMap = loadModuleMap(file, modulesFileMime, modProps, monitor);

		PersistenceManager.getDefault().clearEntityCache(moduleMap);

		// Filter rows if DiscardNonMappedRows is enabled

		if (analysis.isDiscardNonMappedRows()) {

			BaseMatrix fmatrix = null;
			try {
				fmatrix = dataMatrix.getClass().newInstance();
			} catch (Exception ex) {
				throw new PersistenceException("Error filtering data matrix.", ex);
			}

			List<Integer> rows = new ArrayList<Integer>();

			String[] names = moduleMap.getItemNames();
			Set<String> backgroundNames = new HashSet<String>();
			backgroundNames.addAll(Arrays.asList(names));

			for (int i = 0; i < dataMatrix.getRowCount(); i++)
				if (backgroundNames.contains(dataMatrix.getRowLabel(i)))
					rows.add(i);

			int numRows = rows.size();
			int numColumns = dataMatrix.getColumnCount();

			fmatrix.make(numRows, numColumns);
			fmatrix.setColumns(dataMatrix.getColumns());
			
			for (int ri = 0; ri < numRows; ri++) {
				int srcRow = rows.get(ri);
				fmatrix.setRow(ri, dataMatrix.getRowLabel(srcRow));
				for (int ci = 0; ci < numColumns; ci++) {
					Object value = dataMatrix.getCellValue(srcRow, ci, 0);
					fmatrix.setCellValue(ri, ci, 0, value);
				}
			}

			dataMatrix = fmatrix;
		}

		analysis.setData(dataMatrix);
		analysis.setModuleMap(moduleMap);
	}
}
