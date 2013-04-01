/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.persistence.formats.analysis.adapter;

import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.ResourceReference;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ResourceReferenceXmlAdapter extends XmlAdapter<ResourceReferenceXmlElement, ResourceReference>
{

    private IProgressMonitor progressMonitor;
    private IResourceLocator resourceLocator;

    public ResourceReferenceXmlAdapter(IResourceLocator resourceLocator, IProgressMonitor progressMonitor)
    {
        super();
        this.resourceLocator = resourceLocator;
        this.progressMonitor = progressMonitor;
    }

    @Override
    public ResourceReference unmarshal(ResourceReferenceXmlElement resourceReference) throws Exception
    {

        String referenceName = resourceReference.getPath();
        IResourceLocator referenceLocator = resourceLocator.getReferenceLocator(referenceName);

        return new ResourceReference(referenceLocator);
    }

    @Override
    public ResourceReferenceXmlElement marshal(ResourceReference resourceReference) throws Exception
    {

        PersistenceManager pm = PersistenceManager.get();

        // It's a memory instance. Change the resource locator.
        if (resourceReference.getLocator() == null)
        {
            String parentName = resourceLocator.getBaseName();
            String extension = pm.getDefaultExtension(resourceReference.getResourceClass());
            resourceReference.setLocator(resourceLocator.getReferenceLocator(parentName + "-" + resourceReference.getBaseName() + "." + extension + ".gz"));
        }

        IResourceLocator referenceLocator = resourceReference.getLocator();
        if (referenceLocator.isWritable())
        {
            PersistenceManager.get().store(referenceLocator, resourceReference.get(), progressMonitor);
        }

        return new ResourceReferenceXmlElement(null, referenceLocator.getName());
    }

}
