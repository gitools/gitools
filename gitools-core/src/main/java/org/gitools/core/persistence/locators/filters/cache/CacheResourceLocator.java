package org.gitools.core.persistence.locators.filters.cache;

import com.sun.xml.internal.messaging.saaj.util.TeeInputStream;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.PersistenceException;

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
    public IResourceLocator getReferenceLocator(String referenceName) throws PersistenceException {
        return originalLocator.getReferenceLocator(referenceName);
    }

    @Override
    public boolean isWritable() {
        return originalLocator.isWritable();
    }

    @Override
    public InputStream openInputStream() throws IOException {

        if (cached) {
            return new FileInputStream(cachedFile);
        }

        return new TeeInputStream(originalLocator.openInputStream(), new FileOutputStream(cachedFile));
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return originalLocator.openOutputStream();
    }
}
