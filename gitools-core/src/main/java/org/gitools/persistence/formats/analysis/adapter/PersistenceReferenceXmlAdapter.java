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

import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceManager;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Deprecated
public class PersistenceReferenceXmlAdapter extends XmlAdapter<PersistenceReferenceXmlElement, IResource>
{

    private boolean loadReferences;
    private IResourceLocator resourceLocator;
    private IProgressMonitor progressMonitor;
    private Map<Object, String> resourceToReference = new HashMap<Object, String>();

    public PersistenceReferenceXmlAdapter(IResourceLocator resourceLocator, IProgressMonitor progressMonitor)
    {
        this(resourceLocator, progressMonitor, true);
    }

    public PersistenceReferenceXmlAdapter(IResourceLocator resourceLocator, IProgressMonitor progressMonitor, boolean loadReferences)
    {
        super();
        this.resourceLocator = resourceLocator;
        this.progressMonitor = progressMonitor;
        this.loadReferences = loadReferences;
    }

    public void addReference(Object resource, String resourceName)
    {
        resourceToReference.put(resource, resourceName);
    }

    @Override
    public IResource unmarshal(PersistenceReferenceXmlElement resourceReference) throws Exception
    {

        String referenceName = resourceReference.getPath();

        if (referenceName == null || !loadReferences)
        {
            return null;
        }

        IResourceLocator referenceLocator = resourceLocator.getReferenceLocator(referenceName);
        return PersistenceManager.get().load(referenceLocator, IResource.class, new Properties(), progressMonitor);

    }

    @Override
    public PersistenceReferenceXmlElement marshal(IResource resource) throws Exception
    {

        if (resource == null)
        {
            return new PersistenceReferenceXmlElement();
        }

        String resourceReference = resourceToReference.get(resource);
        IResourceLocator referenceLocator = resourceLocator.getReferenceLocator(resourceReference);

        if (referenceLocator.isWritable())
        {
            PersistenceManager.get().store(referenceLocator, resource, progressMonitor);
        }

        return new PersistenceReferenceXmlElement(null, resourceToReference.get(resource));
    }

}
