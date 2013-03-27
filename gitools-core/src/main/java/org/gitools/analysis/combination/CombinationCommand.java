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

package org.gitools.analysis.combination;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.AnalysisCommand;
import org.gitools.analysis.AnalysisException;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.locators.UrlResourceLocator;

import java.io.File;
import java.util.Properties;


public class CombinationCommand extends AnalysisCommand {

	protected CombinationAnalysis analysis;

	protected String dataMime;
	protected String dataPath;

	protected String columnsMime;
	protected String columnsPath;

	public CombinationCommand(
			CombinationAnalysis analysis,
			String dataMime, String dataPath,
			String columnsMime, String columnsPath,
			String workdir, String fileName) {

		super(workdir, fileName);

		this.analysis = analysis;
		this.dataMime = dataMime;
		this.dataPath = dataPath;
		this.columnsMime = columnsMime;
		this.columnsPath = columnsPath;

		this.storeAnalysis = true;
	}

	@Override
	public void run(IProgressMonitor progressMonitor) throws AnalysisException {
		try {
			if (analysis.getData() == null) {
				BaseMatrix data = loadDataMatrix(
						new UrlResourceLocator(new File(dataPath)),
                        progressMonitor
                );

				analysis.setData(data);
			}

			if (columnsPath != null) {
				ModuleMap columnsMap = loadModuleMap(
					    new UrlResourceLocator(new File(columnsPath)),
                        new Properties(),
                        progressMonitor);
				
				analysis.setGroupsMap(columnsMap);
			}

			CombinationProcessor proc = new CombinationProcessor(analysis);

			proc.run(progressMonitor);

			if (storeAnalysis) {
				File workdirFile = new File(workdir);
				if (!workdirFile.exists())
					workdirFile.mkdirs();

				UrlResourceLocator resourceLocator = new UrlResourceLocator(new File(workdirFile, fileName));
				PersistenceManager.get().store(resourceLocator, analysis, progressMonitor);
			}
		}
		catch (Throwable cause) {
			throw new AnalysisException(cause);
		}
	}
}
