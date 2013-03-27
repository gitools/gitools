package org.gitools.ui.genomespace;

import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.ui.genomespace.dm.HttpUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class GsResourceLocator implements IResourceLocator {

    private IResourceLocator resourceLocator;

    public GsResourceLocator(IResourceLocator resourceLocator) throws PersistenceException {
        super();
        this.resourceLocator = resourceLocator;
    }

    @Override
    public URL getURL() {
        return resourceLocator.getURL();
    }

    @Override
    public String getBaseName() {
        return resourceLocator.getBaseName();
    }

    @Override
    public String getExtension() {
        return resourceLocator.getExtension();
    }

    @Override
    public String getName() {
        return resourceLocator.getName();
    }

    @Override
    public IResourceLocator getReferenceLocator(String referenceName) throws PersistenceException {
        return new GsResourceLocator(resourceLocator.getReferenceLocator(referenceName));
    }

    @Override
    public boolean isWritable() {
        return resourceLocator.isWritable();
    }

    @Override
    public InputStream openInputStream() throws IOException {

        if (!getURL().getProtocol().equals("file")) {
            return HttpUtils.getInstance().openConnectionStream(getURL());
        }

        return resourceLocator.openInputStream();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return resourceLocator.openOutputStream();
    }
}
