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
package org.gitools.api.resource;

import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * A ResourceLocator contains all the information needed to locate a Gitools resource.
 */
public interface IResourceLocator {

    URL getURL();

    String getBaseName();

    String getExtension();

    String getName();

    boolean isContainer();

    /**
     * Returns, if known, the length in bytes of the content.
     *
     * @return the total length or -1 if it's unknown.
     */
    long getContentLength();

    IResourceLocator getReferenceLocator(String referenceName) throws PersistenceException;

    boolean isWritable();

    InputStream openInputStream(IProgressMonitor monitor) throws IOException;

    OutputStream openOutputStream() throws IOException;
}
