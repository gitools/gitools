/*
 * #%L
 * org.gitools.api
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.api.persistence;

import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.resource.IResourceLocator;

import java.io.File;

public interface IPersistenceManager {
    <R extends IResource> IResourceFormat<R> getFormat(String fileNameOrExtension, Class<R> resourceClass);

    String getFormatExtension(String fileNameOrExtension);

    String getDefaultExtension(Class<? extends IResource> resourceClass);

    IResourceLocator applyCache(IResourceLocator resourceLocator);

    IResourceLocator applyFilters(IResourceLocator resourceLocator);

    @Deprecated
    <R extends IResource> R load(File file, Class<R> resourceClass, IProgressMonitor progressMonitor);

    <R extends IResource> R load(IResourceLocator resourceLocator, Class<R> resourceClass, IProgressMonitor progressMonitor);

    <R extends IResource> R load(IResourceLocator resourceLocator, IResourceFormat<R> resourceFormat, IProgressMonitor progressMonitor) throws PersistenceException;

    <R extends IResource> void store(IResourceLocator resourceLocator, R resource, IProgressMonitor progressMonitor) throws PersistenceException;

    <R extends IResource> void store(IResourceLocator resourceLocator, R resource, IResourceFormat<R> resourceFormat, IProgressMonitor progressMonitor) throws PersistenceException;
}
