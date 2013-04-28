package org.gitools.core.persistence.locators;

import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProgressMonitorInputStream extends FilterInputStream {

    private IProgressMonitor progressMonitor;

    public ProgressMonitorInputStream(IProgressMonitor progressMonitor, InputStream inputStream) {
        super(inputStream);
        this.progressMonitor = progressMonitor;
    }

    @Override
    public int read() throws IOException {
        progressMonitor.worked(1);
        return in.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        progressMonitor.worked(len);
        return in.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        progressMonitor.worked((int)n);
        return in.skip(n);
    }
}
