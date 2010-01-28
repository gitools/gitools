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

package org.gitools.persistence;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.HashMap;
import java.util.Map;

public class PersistenceContext extends HashMap<Object, Object> {

	private IProgressMonitor monitor;

	private String basePath;

	private Map<Object, String> mimeType = new HashMap<Object, String>();
	private Map<Object, String> filePath = new HashMap<Object, String>();

	public PersistenceContext() {
	}

	public IProgressMonitor getMonitor() {
		return monitor;
	}

	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getMimeType(Object key) {
		return mimeType.get(key);
	}

	public void setMimeType(Object object, String mimeType) {
		this.mimeType.put(object, mimeType);
	}

	public String getFilePath(Object key) {
		return filePath.get(key);
	}

	public void setFilePath(Object key, String filePath) {
		this.filePath.put(key, filePath);
	}

	public String getString(Object key) {
		return (String) get(key);
	}
}
