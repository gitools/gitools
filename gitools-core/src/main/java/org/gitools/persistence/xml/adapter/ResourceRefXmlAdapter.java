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

package org.gitools.persistence.xml.adapter;

import java.io.File;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.gitools.model.ResourceRef;
import org.gitools.persistence.PersistenceContext;
import org.gitools.persistence.PersistenceUtils;


public class ResourceRefXmlAdapter extends XmlAdapter<ResourceRef, ResourceRef> {

	private PersistenceContext context;

	public ResourceRefXmlAdapter(PersistenceContext context) {
		this.context = context;
	}

	@Override
	public ResourceRef unmarshal(ResourceRef v) throws Exception {
		if (v == null)
			return null;

		if (!PersistenceUtils.isAbsolute(v.getPath())) {
			File baseFile = new File(context.getBasePath());
			File path = new File(baseFile, v.getPath());
			v.setPath(path.getAbsolutePath());
		}

		return v;
	}

	@Override
	public ResourceRef marshal(ResourceRef v) throws Exception {
		if (v == null)
			return null;

		File baseFile = new File(context.getBasePath());

		String path = PersistenceUtils.getRelativePath(
				baseFile.getAbsolutePath(),
				v.getPath());

		return new ResourceRef(v.getMime(), path);
	}

}
