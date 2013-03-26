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

    private IProgressMonitor progressMonitor;

    private String basePath;

    private Map<Object, PersistenceEntityContext> entityContext = new HashMap<Object, PersistenceEntityContext>();

    private boolean loadReferences;

    public PersistenceContext() {
        loadReferences = true;
    }

    public IProgressMonitor getProgressMonitor() {
        return progressMonitor;
    }

    public void setProgressMonitor(IProgressMonitor progressMonitor) {
        this.progressMonitor = progressMonitor;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public PersistenceEntityContext getEntityContext(Object key) {
        return entityContext.get(key);
    }

    public void setEntityContext(Object key, PersistenceEntityContext context) {
        this.entityContext.put(key, context);
    }

    public boolean isLoadReferences() {
        return loadReferences;
    }

    public void setLoadReferences(boolean loadReferences) {
        this.loadReferences = loadReferences;
    }
}
