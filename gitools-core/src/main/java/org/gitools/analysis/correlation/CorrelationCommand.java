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

package org.gitools.analysis.correlation;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.AnalysisCommand;
import org.gitools.analysis.AnalysisException;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.locators.UrlResourceLocator;

import java.io.File;

public class CorrelationCommand extends AnalysisCommand {

	protected CorrelationAnalysis analysis;
	protected String dataMime;
	protected String dataPath;

	public CorrelationCommand(
			CorrelationAnalysis analysis,
			String dataMime, String dataPath,
			String workdir, String fileName) {

		super(workdir, fileName);
		
		this.analysis = analysis;
		this.dataMime = dataMime;
		this.dataPath = dataPath;
	}

	@Override
	public void run(IProgressMonitor progressMonitor) throws AnalysisException {

		try {
			BaseMatrix data = loadDataMatrix(
                    new UrlResourceLocator(new File(dataPath)),
                    progressMonitor);

			analysis.setData(data);

			CorrelationProcessor proc = new CorrelationProcessor(analysis);

			proc.run(progressMonitor);

			File workdirFile = new File(workdir);
			if (!workdirFile.exists())
				workdirFile.mkdirs();

			IResourceLocator resourceLocator = new UrlResourceLocator(
                    new File(workdirFile, fileName)
                    );
			PersistenceManager.get().store(resourceLocator, analysis, progressMonitor);
		}
		catch (Throwable cause) {
			throw new AnalysisException(cause);
		}
	}

}
