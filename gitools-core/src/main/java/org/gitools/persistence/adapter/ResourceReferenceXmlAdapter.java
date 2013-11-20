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
package org.gitools.persistence.adapter;

import org.apache.commons.lang.StringUtils;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.locators.filters.zip.ZipResourceLocatorAdaptor;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.List;

public class ResourceReferenceXmlAdapter extends XmlAdapter<ResourceReferenceXmlElement, ResourceReference> {
    private final IResourceLocator resourceLocator;
    private final List<ResourceReference> dependencies;

    public ResourceReferenceXmlAdapter(List<ResourceReference> dependencies, IResourceLocator resourceLocator) {
        super();
        this.resourceLocator = resourceLocator;
        this.dependencies = dependencies;
    }


    @Override
    public ResourceReference unmarshal(ResourceReferenceXmlElement resourceReference) throws Exception {

        String referenceName = resourceReference.getPath();
        IResourceLocator referenceLocator = resourceLocator.getReferenceLocator(referenceName);

        String extension = resourceReference.getFormat();
        if (StringUtils.isEmpty(extension)) {
            extension = referenceLocator.getExtension();
        }

        PersistenceManager pm = PersistenceManager.get();
        IResourceFormat resourceFormat = pm.getFormat(extension, IResource.class);

        ResourceReference dependency = new ResourceReference(referenceLocator, resourceFormat);
        dependencies.add(dependency);

        return dependency;
    }

    @Override
    public ResourceReferenceXmlElement marshal(ResourceReference resourceReference) throws Exception {
        if (resourceReference == null) {
            return null;
        }

        PersistenceManager pm = PersistenceManager.get();

        if (resourceReference.getLocator() == null) {

            // It's a memory instance. Set the resource locator.
            String parentName = resourceLocator.getBaseName();
            String extension = pm.getDefaultExtension(resourceReference.getResourceClass());
            resourceReference.setLocator(resourceLocator.getReferenceLocator(parentName + "-" + resourceReference.getBaseName() + "." + extension));
            resourceReference.setChanged(true);
        } else if (resourceLocator instanceof ZipResourceLocatorAdaptor) {

            // It's a ZIP container. Set the resource locator.
            String extension = pm.getDefaultExtension(resourceReference.getResourceClass());
            resourceReference.setLocator(resourceLocator.getReferenceLocator(resourceReference.getBaseName() + "." + extension));
            resourceReference.setChanged(true);
        }

        if (resourceReference.isChanged()) {
            dependencies.add(resourceReference);
        }

        IResourceLocator referenceLocator = resourceReference.getLocator();
        return new ResourceReferenceXmlElement(null, referenceLocator.getName());
    }

}
