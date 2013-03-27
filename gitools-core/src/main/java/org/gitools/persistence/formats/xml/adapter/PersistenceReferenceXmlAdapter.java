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

package org.gitools.persistence.formats.xml.adapter;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceManager;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PersistenceReferenceXmlAdapter extends XmlAdapter<PersistenceReferenceXmlElement, IResource> {

    private boolean loadReferences;
    private IResourceLocator resourceLocator;
    private IProgressMonitor progressMonitor;
    private Map<Object, String> resourceToReference = new HashMap<Object, String>();

    public PersistenceReferenceXmlAdapter(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) {
        this(resourceLocator, progressMonitor, true);
    }

    public PersistenceReferenceXmlAdapter(IResourceLocator resourceLocator, IProgressMonitor progressMonitor, boolean loadReferences) {
        super();
        this.resourceLocator = resourceLocator;
        this.progressMonitor = progressMonitor;
        this.loadReferences = loadReferences;
    }

    public void addReference(Object resource, String resourceName) {
        resourceToReference.put(resource, resourceName);
    }

    @Override
    public IResource unmarshal(PersistenceReferenceXmlElement resourceReference) throws Exception {

        String referenceName = resourceReference.getPath();

        if (referenceName == null || !loadReferences) {
            return null;
        }

        IResourceLocator referenceLocator = resourceLocator.getReferenceLocator(referenceName);
        return PersistenceManager.get().load(referenceLocator, IResource.class, new Properties(), progressMonitor);

    }

    @Override
    public PersistenceReferenceXmlElement marshal(IResource resource) throws Exception {

        if (resource == null) {
            return new PersistenceReferenceXmlElement();
        }

        String resourceReference = resourceToReference.get(resource);
        IResourceLocator referenceLocator = resourceLocator.getReferenceLocator(resourceReference);

        if (referenceLocator.isWritable()) {
            PersistenceManager.get().store(referenceLocator, resource, progressMonitor);
        }

        return new PersistenceReferenceXmlElement(null, resourceToReference.get(resource));
    }

}
