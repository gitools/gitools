package org.gitools.persistence.locators;

import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceUtils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

public class FileResourceLocator implements IResourceLocator {

    private URL url;
    private File file;

    public FileResourceLocator(File file) throws PersistenceException {
        this.file = file;
        try {
            this.url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Reader getReader() throws IOException {
        return PersistenceUtils.openReader(file);
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public Writer getWriter() throws IOException {
        return PersistenceUtils.openWriter(file);
    }
}
