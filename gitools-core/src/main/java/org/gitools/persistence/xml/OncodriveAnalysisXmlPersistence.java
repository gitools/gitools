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

package org.gitools.persistence.xml;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.persistence.*;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.File;

public class OncodriveAnalysisXmlPersistence
		extends AbstractXmlPersistence<OncodriveAnalysis> {

	public OncodriveAnalysisXmlPersistence() {
		super(OncodriveAnalysis.class);

		setPersistenceTitle("oncodrive analysis");
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
	protected void beforeWrite(File file, OncodriveAnalysis entity,
			IProgressMonitor monitor) throws PersistenceException {

		File baseFile = file.getParentFile();
		String baseName = PersistenceUtils.getFileName(file.getName());

		PersistenceContext context = getPersistenceContext();
		context.setBasePath(baseFile.getAbsolutePath());
		context.setMonitor(monitor);

		PersistenceManager pm = getPersistenceManager();

		String dataExt = pm.getExtensionFromEntity(entity.getData());
		context.setEntityContext(entity.getData(), new PersistenceEntityContext(
				new File(baseFile, baseName + "-data." + dataExt + ".gz").getAbsolutePath(), false));

		context.setEntityContext(entity.getModuleMap(), new PersistenceEntityContext(
				new File(baseFile, baseName + "-modules.ixm.gz").getAbsolutePath(), false));

		String resultsExt = pm.getExtensionFromEntity(entity.getResults());
		context.setEntityContext(entity.getResults(), new PersistenceEntityContext(
				new File(baseFile, baseName + "-results." + resultsExt + ".gz").getAbsolutePath()));
	}
}
