package org.gitools.analysis.htest.enrichment;

import java.io.File;

import org.gitools.model.ModuleMap;
import org.gitools.persistence.PersistenceException;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.gitools.analysis.htest.HtestCommand;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.matrix.model.IMatrix;
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
	public void run(IProgressMonitor monitor) 
			throws PersistenceException, InterruptedException {
		
		final EnrichmentAnalysis enrichAnalysis = (EnrichmentAnalysis) analysis;
		
		// Load data and modules
		
		monitor.begin("Loading ...", 1);
		monitor.info("Data: " + dataPath);
		monitor.info("Modules: " + modulesPath);
		
		Object[] ret = loadDataAndModules(
				dataMime, dataPath,
				populationPath,
				modulesMime, modulesPath,
				enrichAnalysis,
				monitor.subtask());

		enrichAnalysis.setData((IMatrix) ret[0]);
		enrichAnalysis.setModuleMap((ModuleMap) ret[1]);
		
		monitor.end();
		
		// Create and process analysis
		
		EnrichmentProcessor processor = new EnrichmentProcessor(enrichAnalysis);
		
		processor.run(monitor);

		save(enrichAnalysis, monitor);
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

	public String getModulesFile() {
		return modulesPath;
	}

	public void setModulesFile(String modulesFile) {
		this.modulesPath = modulesFile;
	}

	/** Loads data and modules taking into account filtering
	 *
	 * @param dataFileMime
	 * @param dataFileName
	 * @param modulesFileMime
	 * @param modulesFileName
	 * @param analysis
	 * @param monitor
	 * @return [0] = IMatrix with the data, [1] = ModuleMap with the modules
	 * @throws PersistenceException
	 */
	private Object[] loadDataAndModules(
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

		Object dataObj = PersistenceManager.getDefault()
				.load(dataFile, dataFileMime, dataProps, monitor);

		BaseMatrix dataMatrix = null;
		if (dataObj instanceof BaseMatrix)
			dataMatrix = (BaseMatrix) dataObj;
		else if (dataObj instanceof ModuleMap)
			dataMatrix = moduleMapToMatrix((ModuleMap) dataObj);

		// Load modules

		File file = new File(modulesFileName);

		Properties modProps = new Properties();
		modProps.put(ModuleMapPersistence.ITEM_NAMES_FILTER_ENABLED, true);
		modProps.put(ModuleMapPersistence.ITEM_NAMES, dataMatrix.getRowStrings());
		modProps.put(ModuleMapPersistence.MIN_SIZE, analysis.getMinModuleSize());
		modProps.put(ModuleMapPersistence.MAX_SIZE, analysis.getMaxModuleSize());

		Object modObj = (ModuleMap) PersistenceManager.getDefault()
				.load(file, modulesFileMime, modProps, monitor);

		ModuleMap moduleMap = null;
		if (modObj instanceof BaseMatrix)
			moduleMap = matrixToModuleMap((BaseMatrix) modObj);
		else if (modObj instanceof ModuleMap)
			moduleMap = (ModuleMap) modObj;

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
			Set<String> nameSet = new HashSet<String>();
			for (String name : names)
				nameSet.add(name);

			for (int i = 0; i < dataMatrix.getRowCount(); i++)
				if (nameSet.contains(dataMatrix.getRowLabel(i)))
					rows.add(i);

			fmatrix.makeCells(rows.size(), dataMatrix.getColumnCount());
			
			int numColumns = dataMatrix.getColumnCount();
			for (int ri = 0; ri < rows.size(); ri++) {
				int srcRow = rows.get(ri);
				fmatrix.setRow(ri, dataMatrix.getRowLabel(srcRow));
				for (int ci = 0; ci < numColumns; ci++) {
					Object value = dataMatrix.getCellValue(srcRow, ci, 0);
					fmatrix.setCellValue(ri, ci, 0, value);
				}
			}

			dataMatrix = fmatrix;
		}

		return new Object[] { dataMatrix, moduleMap };
	}

	private BaseMatrix moduleMapToMatrix(ModuleMap mmap) {
		DoubleBinaryMatrix matrix = new DoubleBinaryMatrix();
		String[] columns = mmap.getModuleNames();
		String[] rows = mmap.getItemNames();
		matrix.setColumns(columns);
		matrix.setRows(rows);
		matrix.makeCells(rows.length, columns.length);
		for (int col = 0; col < mmap.getModuleCount(); col++)
			for (int row : mmap.getItemIndices(col))
				matrix.setCellValue(row, col, 0, 1.0);
		return matrix;
	}

	private ModuleMap matrixToModuleMap(IMatrix matrix) {
		String[] itemNames = new String[matrix.getRowCount()];
		for (int i = 0; i < matrix.getRowCount(); i++)
			itemNames[i] = matrix.getRowLabel(i);

		String[] modNames = new String[matrix.getColumnCount()];
		for (int i = 0; i < matrix.getColumnCount(); i++)
			modNames[i] = matrix.getColumnLabel(i);

		ModuleMap map = new ModuleMap();
		map.setItemNames(itemNames);
		map.setModuleNames(modNames);

		int[][] mapIndices = new int[matrix.getColumnCount()][];
		for (int col = 0; col < matrix.getColumnCount(); col++) {
			List<Integer> indexList = new ArrayList<Integer>();
			for (int row = 0; row < matrix.getRowCount(); row++) {
				double value = MatrixUtils.doubleValue(matrix.getCellValue(row, col, 0));
				if (value == 1.0)
					indexList.add(row);
			}
			int[] indexArray = new int[indexList.size()];
			for (int i = 0; i < indexList.size(); i++)
				indexArray[i] = indexList.get(i);
			mapIndices[col] = indexArray;
		}

		map.setAllItemIndices(mapIndices);

		return map;
	}
}
