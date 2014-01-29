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
package org.gitools.persistence.locators.filters;

import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceFilter;
import org.gitools.api.resource.IResourceLocator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public abstract class FilterResourceLocator implements IResourceLocator {

    private final String name;
    private String baseName;
    private final String extension;
    private final IResourceLocator resourceLocator;

    protected FilterResourceLocator(IResourceFilter filter, IResourceLocator resourceLocator) {
        this(filter.removeExtension(resourceLocator.getName()), filter.removeExtension(resourceLocator.getExtension()), resourceLocator);
    }

    protected FilterResourceLocator(String name, String extension, IResourceLocator resourceLocator) {
        this.name = name;
        this.extension = extension;
        this.resourceLocator = resourceLocator;
        this.baseName = name;

        int firstDot = name.indexOf('.');
        if (firstDot != -1) {
            this.baseName = name.substring(0, firstDot);
        }
    }

    @Override
    public URL getURL() {
        return resourceLocator.getURL();
    }

    @Override
    public String getBaseName() {
        return baseName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getExtension() {
        return extension;
    }

    @Override
    public boolean isWritable() {
        return resourceLocator.isWritable();
    }

    @Override
    public long getContentLength() {
        return getResourceLocator().getContentLength();
    }

    @Override
    public IResourceLocator getReferenceLocator(String referenceName) throws PersistenceException {
        return getResourceLocator().getReferenceLocator(referenceName);
    }

    @Override
    public InputStream openInputStream(IProgressMonitor progressMonitor) throws IOException {
        return getResourceLocator().openInputStream(progressMonitor);
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return getResourceLocator().openOutputStream();
    }

    protected IResourceLocator getResourceLocator() {
        return resourceLocator;
    }


}
