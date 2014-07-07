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
package org.gitools.persistence.locators;

import edu.upf.bg.mtabix.compress.SeekableBufferedStream;
import edu.upf.bg.mtabix.compress.SeekableFileStream;
import edu.upf.bg.mtabix.compress.SeekableHTTPStream;
import org.gitools.api.ApplicationContext;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.utils.HttpUtils;
import org.gitools.utils.progressmonitor.ProgressMonitorInputStream;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

public class UrlResourceLocator implements IResourceLocator {

    private static final Pattern ABSOLUTE_FILE_PATH = Pattern.compile("^(\\/.*|[a-zA-Z]\\:\\\\)");
    private static final Pattern ABSOLUTE_REMOTE_URL = Pattern.compile("[a-zA-Z]+:\\/\\/.*");
    private URL url;

    private File readFile = null;
    private File writeFile = null;
    private File tempFile = null;

    private transient String baseName;
    private transient String name;

    public UrlResourceLocator(URL url) {
        try {
            this.url = url;
            if (this.url.getProtocol().equals("file")) {
                this.readFile = new File(this.url.toURI());
                this.writeFile = readFile;
            } else {
                this.readFile = null;
                this.writeFile = null;
            }
        } catch (URISyntaxException e) {
            throw new PersistenceException(e);
        }
    }

    public UrlResourceLocator(File file) {
        this.readFile = file;
        this.writeFile = readFile;
        try {
            this.url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new PersistenceException(e);
        }
    }

    public UrlResourceLocator(String url) {
        try {
            if (ABSOLUTE_REMOTE_URL.matcher(url).matches()) {
                this.readFile = null;
                this.writeFile = null;
                this.url = new URL(url);

                if (this.url.getProtocol().equals("file")) {
                    this.readFile = new File(this.url.toURI());
                    this.writeFile = readFile;
                }

            } else {
                this.readFile = new File(url);
                this.writeFile = readFile;
                this.url = readFile.toURI().toURL();
            }
        } catch (MalformedURLException | URISyntaxException e) {
            throw new PersistenceException(e);
        }

    }

    public UrlResourceLocator(File inputFile, File outputFile) {
        this.readFile = inputFile;
        this.writeFile = outputFile;
        try {
            this.url = outputFile.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public File getReadFile() {
        return readFile;
    }

    @Override
    public File getWriteFile() {
        try {
            return getTemporalFile();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String getBaseName() {

        if (baseName == null) {
            String name = getName();
            String extension = ApplicationContext.getPersistenceManager().getFormatExtension(name);
            int startExtension = name.lastIndexOf(extension);
            baseName = (startExtension == 0 ? name : name.substring(0, startExtension - 1));
        }

        return baseName;
    }

    @Override
    public String getName() {

        if (name == null) {
            String url = this.url.toString();
            name = url.substring(url.lastIndexOf('/') + 1);
        }

        return name;
    }

    @Override
    public boolean isContainer() {
        return false;
    }

    @Override
    public long getContentLength() {

        if (readFile != null) {
            long value = readFile.length();
            return (value != 0L ? value : -1);
        }

        return HttpUtils.getContentLength(url);
    }

    @Override
    public IResourceLocator getParentLocator() {
        return null;
    }

    @Override
    public String getExtension() {
        String name = getName();
        return name.substring(name.indexOf(".") + 1);
    }

    @Override
    public boolean isWritable() {
        return writeFile != null;
    }


    @Override
    public IResourceLocator getReferenceLocator(String referenceName) throws PersistenceException {

        // An absolute remote file
        if (ABSOLUTE_REMOTE_URL.matcher(referenceName).matches()) {
            return new UrlResourceLocator(referenceName);
        }

        // A relative path to a remote URL
        if (writeFile == null) {
            String parentUrl = url.toString();
            parentUrl = parentUrl.substring(0, parentUrl.lastIndexOf('/'));
            return new UrlResourceLocator(parentUrl + '/' + referenceName);
        }

        // We allow absolute path references on local files
        if (ABSOLUTE_FILE_PATH.matcher(referenceName).matches()) {
            return new UrlResourceLocator(new File(referenceName));
        }

        // A relative path to the local file
        return new UrlResourceLocator(new File(writeFile.getParentFile(), referenceName));
    }


    @Override
    public InputStream openInputStream(IProgressMonitor progressMonitor) throws IOException {

        if (readFile == null) {
            return new ProgressMonitorInputStream(progressMonitor, new SeekableBufferedStream(new SeekableHTTPStream(getURL())));
        }

        return new ProgressMonitorInputStream(progressMonitor, new SeekableFileStream(readFile));
    }


    @Override
    public OutputStream openOutputStream(IProgressMonitor monitor) throws IOException {

        if (!isWritable()) {
            throw new UnsupportedOperationException("Write to '" + url + "' is not supported.");
        }

        return new FileOutputStream(getTemporalFile());
    }

    private File getTemporalFile() throws IOException {

        if (tempFile == null) {
            tempFile = File.createTempFile(writeFile.getName(), "tmp", writeFile.getParentFile());
        }

        return tempFile;
    }


    @Override
    public void close(IProgressMonitor monitor) {

        // Remove current file
        if (tempFile != null && tempFile.exists()) {

            if (writeFile.exists()) {
                writeFile.delete();
            }

            // Move temporal to final file
            tempFile.renameTo(writeFile);
        }

    }
}
