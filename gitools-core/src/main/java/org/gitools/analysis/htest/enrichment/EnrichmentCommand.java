/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.analysis.htest.enrichment;

import java.io.File;
import java.util.*;

import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.PersistenceException;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.htest.HtestCommand;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.text.BaseMatrixPersistence;
import org.gitools.persistence.text.MatrixTextPersistence;
import org.gitools.persistence.text.ModuleMapPersistence;
import org.gitools.persistence.text.ObjectMatrixTextPersistence;
import org.gitools.persistence.xml.EnrichmentAnalysisXmlPersistence;

public class EnrichmentCommand extends HtestCommand {

	protected String modulesMime;
	protected String modulesPath;
	
	public EnrichmentCommand(
			EnrichmentAnalysis analysis,
			String dataMime,
			String dataFile,
            int valueIndex,
			String populationPath,
			Double populationDefaultValue,
			String modulesMime,
			String modulesFile,
			String workdir,
			String fileName) {
		
		super(analysis, dataMime, dataFile, valueIndex,
				populationPath, populationDefaultValue,
				workdir, fileName);

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
                    valueIndex,
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
     * @param valueIndex
	 * @param modulesFileMime
	 * @param modulesFileName
	 * @param analysis
	 * @param monitor
	 * @throws PersistenceException
	 */
	private void loadDataAndModules(
			String dataFileMime,
			String dataFileName,
            int valueIndex,
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

		Map<Integer, ValueTranslator> valueTranslators = new HashMap<Integer, ValueTranslator>();
        valueTranslators.put(0,
                createValueTranslator(
                        analysis.isBinaryCutoffEnabled(),
                        analysis.getBinaryCutoffCmp(),
                        analysis.getBinaryCutoffValue())
        );

		Properties dataProps = new Properties();
		dataProps.put(BaseMatrixPersistence.BINARY_VALUES, analysis.isBinaryCutoffEnabled());
		dataProps.put(BaseMatrixPersistence.VALUE_TRANSLATORS, valueTranslators);
        dataProps.put(ObjectMatrixTextPersistence.VALUE_INDICES,new int[]{ valueIndex });
		if (populationLabels != null) {
			dataProps.put(MatrixTextPersistence.POPULATION_LABELS, populationLabels);
			dataProps.put(MatrixTextPersistence.BACKGROUND_VALUE, populationDefaultValue);
		}

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
                fmatrix = DoubleBinaryMatrix.class.newInstance();
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
