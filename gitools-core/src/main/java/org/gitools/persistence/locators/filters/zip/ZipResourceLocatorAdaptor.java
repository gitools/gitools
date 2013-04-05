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
package org.gitools.persistence.locators.filters.zip;

import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.locators.filters.AbstractResourceLocatorAdaptor;
import org.gitools.persistence.locators.filters.IResourceFilter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipResourceLocatorAdaptor extends AbstractResourceLocatorAdaptor
{
    private final String entryName;

    public ZipResourceLocatorAdaptor(String entryName, IResourceFilter filter, IResourceLocator resourceLocator)
    {
        super(filter, resourceLocator);
        this.entryName = entryName;
    }

    private ZipResourceLocatorAdaptor(String entryName, String name, String extension, IResourceLocator resourceLocator)
    {
        super(name, extension, resourceLocator);
        this.entryName = entryName;
    }

    @Override
    public IResourceLocator getReferenceLocator(String referenceName) throws PersistenceException
    {
        int firstDot = referenceName.indexOf('.');
        String extension = referenceName.substring(firstDot + 1);

        return new ZipResourceLocatorAdaptor(referenceName, referenceName, extension, getResourceLocator())
        {
            @Override
            protected ZipOutputStream getZipOutputStream() throws IOException
            {
                return ZipResourceLocatorAdaptor.this.getZipOutputStream();
            }

            @NotNull
            @Override
            public OutputStream openOutputStream() throws IOException
            {
                return new NonClosableOutputStream(super.openOutputStream());
            }
        };
    }

    @NotNull
    @Override
    public InputStream openInputStream() throws IOException
    {
        ZipInputStream in = new ZipInputStream(getResourceLocator().openInputStream());

        ZipEntry entry;
        while ((entry = in.getNextEntry()) != null)
        {
            if (entryName.equals(entry.getName()))
            {
                return in;
            }
        }

        throw new PersistenceException("Entry '" + entryName + "' not found in '" + getResourceLocator().getURL() + "'");
    }

    @NotNull
    @Override
    public OutputStream openOutputStream() throws IOException
    {
        ZipOutputStream out = getZipOutputStream();
        out.putNextEntry(new ZipEntry(entryName));
        return out;
    }

    private ZipOutputStream out;

    protected ZipOutputStream getZipOutputStream() throws IOException
    {

        if (this.out == null)
        {
            this.out = new ZipOutputStream(getResourceLocator().openOutputStream());
        }

        return out;
    }

    private class NonClosableOutputStream extends OutputStream
    {

        private final OutputStream out;

        private NonClosableOutputStream(OutputStream out)
        {
            this.out = out;
        }

        @Override
        public void write(int b) throws IOException
        {
            this.out.write(b);
        }

        @Override
        public void flush() throws IOException
        {
            this.out.flush();
        }

        @Override
        public void write(byte[] b) throws IOException
        {
            this.out.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException
        {
            this.out.write(b, off, len);
        }

        @Override
        public void close() throws IOException
        {
            // Ignore close
        }
    }

}
