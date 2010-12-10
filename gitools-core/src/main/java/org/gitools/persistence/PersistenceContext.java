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

package org.gitools.persistence;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.HashMap;
import java.util.Map;

public class PersistenceContext extends HashMap<Object, Object> {

	//TODO private PersistenceManager manager;

	private IProgressMonitor monitor;

	private String basePath;

	private Map<Object, PersistenceEntityContext> entityContext =
			new HashMap<Object, PersistenceEntityContext>();

	private boolean loadReferences;

	//private Map<Object, String> filePath = new HashMap<Object, String>();

	public PersistenceContext() {
		loadReferences = true;
	}

	/*
	public PersistenceManager getPersistenceManager() {
		return manager;
	}

	public void setPersistenceManager(PersistenceManager manager) {
		this.manager = manager;
	}
	 */

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

	@Deprecated
	public String getMimeType(Object key) {
		return entityContext.get(key).getMimeType();
	}

	@Deprecated
	public String getFilePath(Object key) {
		return entityContext.get(key).getFilePath();
	}

	public PersistenceEntityContext getEntityContext(Object key) {
		return entityContext.get(key);
	}

	public void setEntityContext(Object key, PersistenceEntityContext context) {
		this.entityContext.put(key, context);
	}

	public String getString(Object key) {
		return (String) get(key);
	}

	public boolean isLoadReferences() {
		return loadReferences;
	}

	public void setLoadReferences(boolean loadReferences) {
		this.loadReferences = loadReferences;
	}
}
