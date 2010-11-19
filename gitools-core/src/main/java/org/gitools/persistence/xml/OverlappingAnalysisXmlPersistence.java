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
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.ResourceRef;
import org.gitools.persistence.PersistenceContext;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.persistence.xml.adapter.ResourceRefXmlAdapter;

public class OverlappingAnalysisXmlPersistence
		extends AbstractXmlPersistence<OverlappingAnalysis> {

	public OverlappingAnalysisXmlPersistence() {
		super(OverlappingAnalysis.class);

		setPersistenceTitle("overlapping analysis");
	}

	@Override
	protected XmlAdapter<?, ?>[] createAdapters() {
		PersistenceContext context = getPersistenceContext();
		return new XmlAdapter<?, ?>[] {
			new ResourceRefXmlAdapter(context) };
	}

	@Override
	protected void beforeRead(File file, IProgressMonitor monitor) throws PersistenceException {
		File baseFile = file.getParentFile();

		PersistenceContext context = getPersistenceContext();
		context.setBasePath(baseFile.getAbsolutePath());
		context.setMonitor(monitor);
	}

	@Override
	protected void afterRead(File file, OverlappingAnalysis entity, IProgressMonitor monitor) throws PersistenceException {
		File baseFile = file.getParentFile();

		PersistenceContext context = getPersistenceContext();

		IMatrix data = null;
		if (context.isLoadReferences())
			data = (IMatrix) load(baseFile, entity.getFilteredDataResource(), monitor);
		entity.setData(data);

		IMatrix cellResults = null;
		if (context.isLoadReferences())
			cellResults = (IMatrix) load(baseFile, entity.getCellResultsResource(), monitor);
		entity.setCellResults(cellResults);
	}

	@Override
	protected void beforeWrite(File file, OverlappingAnalysis entity,
			IProgressMonitor monitor) throws PersistenceException {

		File baseFile = file.getParentFile();
		String baseName = PersistenceUtils.getFileName(file.getName());

		PersistenceContext context = getPersistenceContext();
		context.setBasePath(baseFile.getAbsolutePath());
		context.setMonitor(monitor);

		PersistenceManager pm = getPersistenceManager();

		if (entity.isBinaryCutoffEnabled()) {
			String dataExt = pm.getExtensionFromEntity(entity.getData().getClass());
			File dataFile = new File(baseFile, baseName + "-data." + dataExt + ".gz");
			if (entity.getFilteredDataResource() == null)
				entity.setFilteredDataResource(new ResourceRef(
						PersistenceManager.getDefault().getMimeFromEntity(entity.getData().getClass()),
						dataFile.getAbsolutePath()));

			PersistenceManager.getDefault().store(dataFile, entity.getData(), monitor);
		}
		else
			entity.setFilteredDataResource(entity.getSourceDataResource());

		String cellResultsExt = pm.getExtensionFromEntity(entity.getCellResults().getClass());
		File cellResultsFile = new File(baseFile, baseName + "-results-cells." + cellResultsExt + ".gz");
		if (entity.getCellResultsResource() == null)
			entity.setCellResultsResource(new ResourceRef(
					PersistenceManager.getDefault().getMimeFromEntity(entity.getCellResults().getClass()),
					cellResultsFile.getAbsolutePath()));

		PersistenceManager.getDefault().store(cellResultsFile, entity.getCellResults(), monitor);
	}

	private Object load(File baseFile, ResourceRef resourceRef, IProgressMonitor monitor) throws PersistenceException {
		if (resourceRef == null)
			return null;

		String path = resourceRef.getPath();
		boolean absolute = PersistenceUtils.isAbsolute(path);

		File file = absolute ?
			new File(path) : new File(baseFile, path);

		String mimeType = resourceRef.getMime();
		if (mimeType == null)
			mimeType = PersistenceManager.getDefault().getMimeFromFile(file.getName());

		Object entity = PersistenceManager.getDefault().load(file, mimeType, monitor);

		return entity;
	}
}
