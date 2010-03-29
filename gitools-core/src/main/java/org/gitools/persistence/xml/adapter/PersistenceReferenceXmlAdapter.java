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

package org.gitools.persistence.xml.adapter;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.gitools.persistence.PersistenceContext;
import org.gitools.persistence.PersistenceEntityContext;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.PersistenceManager.FileRef;
import org.gitools.persistence.PersistenceUtils;

public class PersistenceReferenceXmlAdapter<T>
		extends XmlAdapter<PersistenceReferenceXmlElement, T> {

	private PersistenceContext context;

	public PersistenceReferenceXmlAdapter(PersistenceContext context) {
		this.context = context;
	}

	@Override
	public T unmarshal(PersistenceReferenceXmlElement v) throws Exception {
		if (v.getPath() == null)
			return null;
		
		File baseFile = new File(context.getBasePath());

		boolean absolute = PersistenceUtils.isAbsolute(v.getPath());

		File file = absolute ?
			new File(v.getPath()) : new File(baseFile, v.getPath());

		String mimeType = v.getMime();
		if (mimeType == null)
			mimeType = PersistenceManager.getDefault().getMimeFromFile(file.getName());

		IProgressMonitor monitor = context.getMonitor();

		T entity = (T) PersistenceManager.getDefault().load(file, mimeType, monitor);

		context.setEntityContext(entity,
				new PersistenceEntityContext(mimeType, file.getAbsolutePath()));

		return entity;
	}

	@Override
	public PersistenceReferenceXmlElement marshal(T v) throws Exception {
		if (v == null)
			return new PersistenceReferenceXmlElement();
		
		PersistenceEntityContext entityContext = context.getEntityContext(v);

		String mimeType = entityContext.getMimeType();
		if (mimeType == null)
			mimeType = PersistenceManager.getDefault().getMimeFromEntity(v.getClass());

		File baseFile = new File(context.getBasePath());
		
		File file = new File(entityContext.getFilePath());

		FileRef fileRef = PersistenceManager.getDefault().getEntityFileRef(v);
		File linkFile = fileRef != null ? fileRef.getFile() : null;
		if (entityContext.isReferenceCacheEnabled() && linkFile != null)
			file = linkFile;

		String path = PersistenceUtils.getRelativePath(
				baseFile.getAbsolutePath(),
				file.getAbsolutePath());

		IProgressMonitor monitor = context.getMonitor();

		if (!entityContext.isReferenceCacheEnabled() || linkFile == null)
			PersistenceManager.getDefault().store(file, v, monitor);

		return new PersistenceReferenceXmlElement(mimeType, path);
	}

}
