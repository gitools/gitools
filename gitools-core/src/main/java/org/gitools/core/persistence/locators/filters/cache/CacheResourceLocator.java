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
package org.gitools.core.persistence.locators.filters.cache;

import org.apache.commons.io.input.TeeInputStream;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.core.persistence.locators.ProgressMonitorInputStream;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.io.*;
import java.net.URL;

public class CacheResourceLocator implements IResourceLocator {

    private boolean cached;
    private File cachedFile;
    private IResourceLocator originalLocator;

    public CacheResourceLocator(File cachedFile, IResourceLocator originalLocator) {
        this.cached = false;
        this.cachedFile = cachedFile;
        this.originalLocator = originalLocator;
    }

    @Override
    public URL getURL() {
        return originalLocator.getURL();
    }

    @Override
    public String getBaseName() {
        return originalLocator.getBaseName();
    }

    @Override
    public String getExtension() {
        return originalLocator.getExtension();
    }

    @Override
    public String getName() {
        return originalLocator.getName();
    }

    @Override
    public long getContentLength() {
        if (cached) {
            return cachedFile.length();
        }

        return originalLocator.getContentLength();
    }

    @Override
    public IResourceLocator getReferenceLocator(String referenceName) throws PersistenceException {
        return originalLocator.getReferenceLocator(referenceName);
    }

    @Override
    public boolean isWritable() {
        return originalLocator.isWritable();
    }

    @Override
    public InputStream openInputStream(IProgressMonitor progressMonitor) throws IOException {

        if (cached) {
            return new ProgressMonitorInputStream(progressMonitor, new FileInputStream(cachedFile));
        }

        cached = true;
        return new TeeInputStream(originalLocator.openInputStream(progressMonitor), new FileOutputStream(cachedFile));
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return originalLocator.openOutputStream();
    }
}
