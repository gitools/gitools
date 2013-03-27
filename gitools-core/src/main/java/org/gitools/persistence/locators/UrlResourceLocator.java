package org.gitools.persistence.locators;

import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

public class UrlResourceLocator implements IResourceLocator {

    private static Pattern ABSOLUTE_FILE_PATH = Pattern.compile("^(\\/.*|[a-zA-Z]\\:\\\\)");
    private static Pattern ABSOLUTE_REMOTE_URL = Pattern.compile("[a-zA-Z]+\\:\\/\\/");

    private URL url;
    private File file;

    public UrlResourceLocator(File file) throws PersistenceException {
        this.file = file;
        try {
            this.url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new PersistenceException(e);
        }
    }

    public UrlResourceLocator(String url) throws PersistenceException {
        try {
            if (ABSOLUTE_REMOTE_URL.matcher(url).matches()) {
                this.file = null;
                this.url = new URL(url);

                if (this.url.getProtocol().equals("file")) {
                    this.file = new File(this.url.toURI());
                }

            } else {
                this.file = new File(url);
                this.url = file.toURI().toURL();
            }
        } catch (MalformedURLException e) {
            throw new PersistenceException(e);
        } catch (URISyntaxException e) {
            throw new PersistenceException(e);
        }

    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public String getBaseName() {
        String name = getName();

        int firstDot = name.indexOf('.');
        if (firstDot != -1) {
            name = name.substring(0, firstDot);
        }

        return name;
    }

    @Override
    public String getName() {
        String name = url.toString();
        return name.substring(name.lastIndexOf('/')+1);
    }

    @Override
    public String getExtension() {
        String name = getName();
        return name.substring(name.indexOf(".") + 1);
    }

    @Override
    public boolean isWritable() {
        return file != null;
    }

    @Override
    public IResourceLocator getReferenceLocator(String referenceName) throws PersistenceException {

        // An absolute remote file
        if (ABSOLUTE_REMOTE_URL.matcher(referenceName).matches()) {
            return new UrlResourceLocator(referenceName);
        }

        // A relative path to a remote URL
        if (file == null) {
            return new UrlResourceLocator(url.toString() + '/' + referenceName);
        }

        // We allow absolute path references on local files
        if (ABSOLUTE_FILE_PATH.matcher(referenceName).matches()) {
            return new UrlResourceLocator(new File(referenceName));
        }

        // A relative path to the local file
        return new UrlResourceLocator(new File(file.getParentFile(), referenceName));
    }

    @Override
    public InputStream openInputStream() throws IOException {

        if (file == null) {
            return url.openStream();
        }

        return new FileInputStream(file);
    }

    @Override
    public OutputStream openOutputStream() throws IOException {

        if (!isWritable()) {
            throw new UnsupportedOperationException("Write to '" + url + "' is not supported.");
        }

        return new FileOutputStream(file);
    }


}
