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
package org.gitools.core.persistence.locators;

import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CancellationException;

public class ProgressMonitorInputStream extends FilterInputStream {

    private IProgressMonitor progressMonitor;

    public ProgressMonitorInputStream(IProgressMonitor progressMonitor, InputStream inputStream) {
        super(new BufferedInputStream(inputStream));
        this.progressMonitor = progressMonitor;
    }

    @Override
    public int read() throws IOException {

        if (progressMonitor.isCancelled()) {
            throw new CancellationException();
        }

        int read = in.read();
        progressMonitor.worked(read);

        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {

        if (progressMonitor.isCancelled()) {
            throw new CancellationException();
        }

        int read = in.read(b, off, len);
        progressMonitor.worked(read);

        return read;
    }

    @Override
    public long skip(long n) throws IOException {

        long skip = in.skip(n);
        progressMonitor.worked((int)skip);

        return skip;
    }
}
