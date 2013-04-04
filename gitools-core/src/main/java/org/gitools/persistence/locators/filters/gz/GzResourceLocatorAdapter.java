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
package org.gitools.persistence.locators.filters.gz;

import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzResourceLocatorAdapter implements IResourceLocator
{

    private String name;
    private String extension;
    private IResourceLocator resourceLocator;

    GzResourceLocatorAdapter(@NotNull IResourceLocator resourceLocator)
    {
        this.resourceLocator = resourceLocator;
        this.name = resourceLocator.getName().replace(".gz", "");
        this.extension = resourceLocator.getExtension().replace(".gz", "");
    }

    @Override
    public URL getURL()
    {
        return resourceLocator.getURL();
    }

    @Override
    public String getBaseName()
    {
        return resourceLocator.getBaseName();
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getExtension()
    {
        return extension;
    }

    @Override
    public IResourceLocator getReferenceLocator(String referenceName) throws PersistenceException
    {
        return resourceLocator.getReferenceLocator(referenceName);
    }

    @Override
    public boolean isWritable()
    {
        return resourceLocator.isWritable();
    }

    @NotNull
    @Override
    public InputStream openInputStream() throws IOException
    {
        return new GZIPInputStream(resourceLocator.openInputStream());
    }

    @NotNull
    @Override
    public OutputStream openOutputStream() throws IOException
    {
        return new GZIPOutputStream(resourceLocator.openOutputStream());
    }

}
