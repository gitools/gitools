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

package org.gitools.analysis.htest.oncozet;

import java.io.File;

import org.gitools.datafilters.ValueTranslator;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.PersistenceException;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.List;
import java.util.Properties;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.htest.HtestCommand;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.text.MatrixTextPersistence;
import org.gitools.persistence.text.ModuleMapPersistence;
import org.gitools.persistence.xml.OncodriveAnalysisXmlPersistence;

public class OncodriveCommand extends HtestCommand {

	protected String modulesMime;
	protected String modulesPath;

	public OncodriveCommand(
			OncodriveAnalysis analysis,
			String dataMime,
			String dataPath,
			String populationPath,
			Double populationDefaultValue,
			String modulesMime,
			String modulesFile,
			String workdir,
			String fileName) {
		
		super(analysis, dataMime, dataPath,
				populationPath, populationDefaultValue,
				workdir, fileName);

		this.modulesMime = modulesMime;
		this.modulesPath = modulesFile;
	}

	@Override
	public void run(IProgressMonitor monitor) throws AnalysisException {

		try {
			final OncodriveAnalysis oncozAnalysis = (OncodriveAnalysis) analysis;

			// Load data and modules

			monitor.begin("Loading ...", 1);
			monitor.info("Data: " + dataPath);
			monitor.info("Columns: " + modulesPath);

			loadDataAndModules(
					dataMime, dataPath,
					populationPath,
					modulesMime, modulesPath,
					oncozAnalysis,
					monitor.subtask());

			monitor.end();

			OncodriveProcessor processor = new OncodriveProcessor(oncozAnalysis);

			processor.run(monitor);

			// Save analysis

			save(oncozAnalysis, monitor);
		}
		catch (Exception ex) {
			throw new AnalysisException(ex);
		}
	}

	private void loadDataAndModules(
			String dataFileMime,
			String dataFileName,
			String populationFileName,
			String modulesFileMime,
			String modulesFileName,
			OncodriveAnalysis analysis,
			IProgressMonitor monitor) throws PersistenceException {

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

		ValueTranslator valueTranslator = createValueTranslator(
				analysis.isBinaryCutoffEnabled(),
				analysis.getBinaryCutoffCmp(),
				analysis.getBinaryCutoffValue());

		Properties dataProps = new Properties();
		dataProps.put(MatrixTextPersistence.BINARY_VALUES, analysis.isBinaryCutoffEnabled());
		dataProps.put(MatrixTextPersistence.VALUE_TRANSLATOR, valueTranslator);
		if (populationLabels != null) {
			dataProps.put(MatrixTextPersistence.POPULATION_LABELS, populationLabels);
			dataProps.put(MatrixTextPersistence.BACKGROUND_VALUE, populationDefaultValue);
		}

		BaseMatrix dataMatrix = loadDataMatrix(dataFile, dataFileMime, dataProps, monitor);

		PersistenceManager.getDefault().clearEntityCache(dataMatrix);

		analysis.setData(dataMatrix);

		// Load modules

		if (modulesFileName != null) {
			File file = new File(modulesFileName);

			Properties modProps = new Properties();
			modProps.put(ModuleMapPersistence.ITEM_NAMES_FILTER_ENABLED, true);
			modProps.put(ModuleMapPersistence.ITEM_NAMES, dataMatrix.getColumnStrings());
			modProps.put(ModuleMapPersistence.MIN_SIZE, analysis.getMinModuleSize());
			modProps.put(ModuleMapPersistence.MAX_SIZE, analysis.getMaxModuleSize());

			ModuleMap moduleMap = loadModuleMap(file, modulesFileMime, modProps, monitor);

			PersistenceManager.getDefault().clearEntityCache(moduleMap);
			
			analysis.setModuleMap(moduleMap);
		}
	}
	
	private void save(final OncodriveAnalysis analysis, IProgressMonitor monitor) throws PersistenceException {

		File workdirFile = new File(workdir);
		if (!workdirFile.exists())
			workdirFile.mkdirs();

		File file = new File(workdirFile, fileName);
		OncodriveAnalysisXmlPersistence p = new OncodriveAnalysisXmlPersistence();
		p.setRecursivePersistence(true);
		p.write(file, analysis, monitor);
	}
}
