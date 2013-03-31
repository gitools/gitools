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

package org.gitools.analysis.overlapping;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.analysis.AnalysisCommand;
import org.gitools.analysis.AnalysisException;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.model.ResourceRef;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.locators.UrlResourceLocator;

import java.io.File;


public class OverlappingCommand extends AnalysisCommand {

	private OverlappingAnalysis analysis;

	public OverlappingCommand(OverlappingAnalysis analysis, String workdir, String fileName) {
		super(workdir, fileName);

		this.analysis = analysis;
	}

	@Override
	public void run(IProgressMonitor progressMonitor) throws AnalysisException {
		try {
			if (analysis.getData() == null) {
				ResourceRef res = analysis.getSourceDataResource();
				String dataPath = res.getPath();
				String dataMime = res.getMime();

				BaseMatrix data = loadDataMatrix(
						new UrlResourceLocator(new File(dataPath)),
                        progressMonitor
                );

				analysis.setData(data);
			}

			OverlappingProcessor proc = new OverlappingProcessor(analysis);

			proc.run(progressMonitor);

			if (storeAnalysis) {
				File workdirFile = new File(workdir);
				if (!workdirFile.exists())
					workdirFile.mkdirs();

				PersistenceManager.get().store(
                        new UrlResourceLocator(new File(workdirFile, fileName)),
                        analysis,
                        progressMonitor
                );
			}
		}
		catch (Throwable cause) {
			throw new AnalysisException(cause);
		}
	}
}
