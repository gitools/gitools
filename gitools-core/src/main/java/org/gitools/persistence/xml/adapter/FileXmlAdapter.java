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
import org.gitools.persistence.PersistenceUtils;

public class FileXmlAdapter extends XmlAdapter<String, File> {

	private File baseFile;

	public FileXmlAdapter() {
	}

	public FileXmlAdapter(File baseFile) {
		this.baseFile = baseFile;
	}

	@Override
	public File unmarshal(String v) throws Exception {
		return baseFile != null ?
			new File(baseFile, v) :
			new File(v);
	}

	@Override
	public String marshal(File v) throws Exception {
		return baseFile != null ?
			PersistenceUtils.getRelativePath(
				baseFile.getAbsolutePath(), v.getAbsolutePath()) :
			v.getAbsolutePath();
	}

}
