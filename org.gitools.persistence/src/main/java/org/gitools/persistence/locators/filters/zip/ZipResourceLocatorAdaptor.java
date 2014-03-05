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

import org.gitools.api.ApplicationContext;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceFilter;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.persistence.locators.filters.FilterResourceLocator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipResourceLocatorAdaptor extends FilterResourceLocator {

    private final String entryName;

    public ZipResourceLocatorAdaptor(String entryName, IResourceFilter filter, IResourceLocator resourceLocator) {
        super(filter, resourceLocator);
        this.entryName = entryName;
    }

    private ZipResourceLocatorAdaptor(String entryName, String name, String extension, IResourceLocator resourceLocator) {
        super(name, extension, resourceLocator);
        this.entryName = entryName;
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public IResourceLocator getReferenceLocator(String referenceName) throws PersistenceException {

        String extension = ApplicationContext.getPersistenceManager().getFormatExtension(referenceName);
        int extensionIndex = referenceName.indexOf(extension);
        String extensionWithFilters = referenceName.substring(extensionIndex);

        return new ZipResourceLocatorAdaptor(referenceName, referenceName, extensionWithFilters, getResourceLocator()) {
            @Override
            protected ZipOutputStream getZipOutputStream() throws IOException {
                return ZipResourceLocatorAdaptor.this.getZipOutputStream();
            }


            @Override
            public OutputStream openOutputStream() throws IOException {
                return new NonClosableOutputStream(super.openOutputStream());
            }
        };
    }


    @Override
    public InputStream openInputStream(IProgressMonitor progressMonitor) throws IOException {
        ZipInputStream in = new ZipInputStream(getResourceLocator().openInputStream(progressMonitor));

        ZipEntry entry;
        while ((entry = in.getNextEntry()) != null) {
            if (entryName.equals(entry.getName())) {
                return in;
            }
        }

        throw new PersistenceException("Entry '" + entryName + "' not found in '" + getResourceLocator().getURL() + "'");
    }


    @Override
    public OutputStream openOutputStream() throws IOException {
        ZipOutputStream out = getZipOutputStream();
        out.putNextEntry(new ZipEntry(entryName));
        return out;
    }

    private ZipOutputStream out;

    protected ZipOutputStream getZipOutputStream() throws IOException {

        if (this.out == null) {
            this.out = new ZipOutputStream(getResourceLocator().openOutputStream()) {
                @Override
                public void close() throws IOException {
                    super.close();
                    ZipResourceLocatorAdaptor.this.out = null;
                }
            };
        }

        return out;
    }

    private class NonClosableOutputStream extends OutputStream {

        private final OutputStream out;

        private NonClosableOutputStream(OutputStream out) {
            this.out = out;
        }

        @Override
        public void write(int b) throws IOException {
            this.out.write(b);
        }

        @Override
        public void flush() throws IOException {
            this.out.flush();
        }

        @Override
        public void write(byte[] b) throws IOException {
            this.out.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            this.out.write(b, off, len);
        }

        @Override
        public void close() throws IOException {
            // Ignore close
        }
    }

}
