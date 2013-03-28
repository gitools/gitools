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

package org.gitools.persistence.formats.analysis.adapter;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.ResourceReference;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ResourceReferenceXmlAdapter extends XmlAdapter<ResourceReferenceXmlElement, ResourceReference> {

    private IProgressMonitor progressMonitor;
    private IResourceLocator resourceLocator;

    public ResourceReferenceXmlAdapter(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) {
        super();
        this.resourceLocator = resourceLocator;
        this.progressMonitor = progressMonitor;
    }

    @Override
    public ResourceReference unmarshal(ResourceReferenceXmlElement resourceReference) throws Exception {

        String referenceName = resourceReference.getPath();
        IResourceLocator referenceLocator = resourceLocator.getReferenceLocator(referenceName);

        return new ResourceReference(referenceLocator);
    }

    @Override
    public ResourceReferenceXmlElement marshal(ResourceReference resourceReference) throws Exception {

        PersistenceManager pm = PersistenceManager.get();

        // It's a memory instance. Change the resource locator.
        if (resourceReference.getLocator() == null) {
            String parentName = resourceLocator.getBaseName();
            String extension = pm.getDefaultExtension(resourceReference.getResourceClass());
            resourceReference.setLocator( resourceLocator.getReferenceLocator( parentName + "-" + resourceReference.getBaseName() + "." + extension + ".gz"));
        }

        IResourceLocator referenceLocator = resourceReference.getLocator();
        if (referenceLocator.isWritable()) {
            PersistenceManager.get().store(referenceLocator, resourceReference.get(), progressMonitor);
        }

        return new ResourceReferenceXmlElement(null, referenceLocator.getName());
    }

}
