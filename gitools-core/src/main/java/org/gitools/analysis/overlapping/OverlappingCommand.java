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

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import java.util.Properties;
import org.gitools.analysis.AnalysisCommand;
import org.gitools.analysis.AnalysisException;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.model.ResourceRef;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.text.MatrixTextPersistence;


public class OverlappingCommand extends AnalysisCommand {

	private OverlappingAnalysis analysis;

	public OverlappingCommand(OverlappingAnalysis analysis, String workdir, String fileName) {
		super(workdir, fileName);

		this.analysis = analysis;
	}

	@Override
	public void run(IProgressMonitor monitor) throws AnalysisException {
		try {
			if (analysis.getData() == null) {
				ResourceRef res = analysis.getSourceDataResource();
				String dataPath = res.getPath();
				String dataMime = res.getMime();
				
				/*ValueTranslator valueTranslator = createValueTranslator(
					analysis.isBinaryCutoffEnabled(),
					analysis.getBinaryCutoffCmp(),
					analysis.getBinaryCutoffValue());

				Properties dataProps = new Properties();
				dataProps.put(MatrixTextPersistence.BINARY_VALUES, analysis.isBinaryCutoffEnabled());
				dataProps.put(MatrixTextPersistence.VALUE_TRANSLATOR, valueTranslator);*/

				BaseMatrix data = loadDataMatrix(
						new File(dataPath), dataMime, new Properties(), monitor);

				analysis.setData(data);
			}

			OverlappingProcessor proc = new OverlappingProcessor(analysis);

			proc.run(monitor);

			if (storeAnalysis) {
				File workdirFile = new File(workdir);
				if (!workdirFile.exists())
					workdirFile.mkdirs();

				File file = new File(workdirFile, fileName);
				PersistenceManager.getDefault().store(file, analysis, monitor);
			}
		}
		catch (Throwable cause) {
			throw new AnalysisException(cause);
		}
	}
}
