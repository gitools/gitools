/*
 *  Copyright 2010 chris.
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

package org.gitools.persistence.xml;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.persistence.PersistenceContext;
import org.gitools.persistence.PersistenceEntityContext;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;

public class CorrelationAnalysisXmlPersistence
		extends AbstractXmlPersistence<CorrelationAnalysis> {

	public CorrelationAnalysisXmlPersistence() {
		super(CorrelationAnalysis.class);

		setPersistenceTitle("correlation analysis");
	}

	@Override
	protected XmlAdapter<?, ?>[] createAdapters() {
		PersistenceContext context = getPersistenceContext();
		return new XmlAdapter<?, ?>[] {
			new PersistenceReferenceXmlAdapter(context)
		};
	}

	@Override
	protected void beforeRead(File file, IProgressMonitor monitor) throws PersistenceException {
		File baseFile = file.getParentFile();

		PersistenceContext context = getPersistenceContext();
		context.setBasePath(baseFile.getAbsolutePath());
		context.setMonitor(monitor);
	}

	@Override
	protected void beforeWrite(File file, CorrelationAnalysis entity,
			IProgressMonitor monitor) throws PersistenceException {

		File baseFile = file.getParentFile();
		String baseName = PersistenceUtils.getFileName(file.getName());

		PersistenceContext context = getPersistenceContext();
		context.setBasePath(baseFile.getAbsolutePath());
		context.setMonitor(monitor);

		PersistenceManager pm = getPersistenceManager();

		String dataExt = pm.getExtensionFromEntity(entity.getData().getClass());
		context.setEntityContext(entity.getData(), new PersistenceEntityContext(
				new File(baseFile, baseName + "-data." + dataExt + ".gz").getAbsolutePath(), false));

		String resultsExt = pm.getExtensionFromEntity(entity.getResults().getClass());
		context.setEntityContext(entity.getResults(), new PersistenceEntityContext(
				new File(baseFile, baseName + "-results." + resultsExt + ".gz").getAbsolutePath()));
	}
}
