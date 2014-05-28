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
import org.gitools.utils.progressmonitor.NullProgressMonitor;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipResourceLocatorAdaptor extends FilterResourceLocator {

    private File tmpFolder;

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

        return new ZipResourceLocatorAdaptor(referenceName, referenceName, extensionWithFilters, getParentLocator()) {

            @Override
            public File getWriteFile() {
                try {
                    return new File(getTemporalFolder(), getEntryName());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected File getTemporalFolder() throws IOException {
                return ZipResourceLocatorAdaptor.this.getTemporalFolder();
            }

            @Override
            public void close(IProgressMonitor monitor) {
            }

            @Override
            public OutputStream openOutputStream(IProgressMonitor monitor) throws IOException {
                return new FileOutputStream(getWriteFile());
            }
        };
    }

    @Override
    public InputStream openInputStream(IProgressMonitor progressMonitor) throws IOException {
        ZipInputStream in = new ZipInputStream(getParentLocator().openInputStream(progressMonitor));

        ZipEntry entry;
        while ((entry = in.getNextEntry()) != null) {
            if (entryName.equals(entry.getName())) {
                return in;
            }
        }

        throw new PersistenceException("Entry '" + entryName + "' not found in '" + getParentLocator().getURL() + "'");
    }


    @Override
    public OutputStream openOutputStream(IProgressMonitor monitor) throws IOException {

        // Create a temporal folder
        tmpFolder = createTemporalFolder(getURL());

        // Extract all the files
        monitor.title("Extracting to temporal folder...");
        try {
            ZipUtil.unpack(getParentLocator().openInputStream(new NullProgressMonitor()), tmpFolder);

            // Check if we are doing a 'Save as...'
            if (!getWriteFile().equals(getReadFile())) {

                // Rename all entries
                String inName = getReadFile().getName().replace("." + getExtension() + ".zip", "");
                String toName = getBaseName();

                renameAll(getTemporalFolder(), inName, toName);

            }

        } catch (FileNotFoundException e) {
            // It's a all in memory resource
        }

        monitor.title("Copying files...");
        return new FileOutputStream(new File(tmpFolder, entryName));
    }

    @Override
    public void close(IProgressMonitor monitor) {

        try {

            // Compress folder
            monitor.title("Compressing files...");
            ZipUtil.pack(getTemporalFolder(), getWriteFile(), Deflater.NO_COMPRESSION);

            // Close temporal file
            getParentLocator().close(monitor);

            // Remove temporal folder
            deleteFolder(getTemporalFolder());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    protected File getTemporalFolder() throws IOException {
        return tmpFolder;
    }

    protected String getEntryName() {
        return entryName;
    }

    /**
     *
     * Creates a temporal folder. If the 'url' is a file then the temporal folder is
     * created next to the file, otherwise is created at the system temporal folder.
     *
     * @param url
     * @return
     * @throws IOException
     */
    private File createTemporalFolder(URL url) throws IOException {

        File tmpFolder;

        if ("file".equals(url.getProtocol())) {
            try {
                File parentFolder = (new File(url.toURI())).getParentFile();
                tmpFolder = File.createTempFile(getName(), "", parentFolder);
            } catch (URISyntaxException e) {
                tmpFolder = File.createTempFile("gitools" + getBaseName(), "");
            }
        } else {
            tmpFolder = File.createTempFile("gitools" + getBaseName(), "");
        }

        tmpFolder.delete();
        tmpFolder.mkdir();

        return tmpFolder;
    }

    private static void renameAll(File folder, String inName, String toName) {

        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f : files) {
                String name = f.getName();
                if (!f.isDirectory() && name.contains(inName)) {
                    name = name.replace(inName, toName);
                    File toFile = new File(f.getParentFile(), name);
                    f.renameTo(toFile);
                }
            }
        }
    }


    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

}
