/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.genomespace;

import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.ui.genomespace.dm.HttpUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class GsResourceLocator implements IResourceLocator {

    private final IResourceLocator resourceLocator;

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

    @NotNull
    @Override
    public IResourceLocator getReferenceLocator(String referenceName) throws PersistenceException {
        return new GsResourceLocator(resourceLocator.getReferenceLocator(referenceName));
    }

    @Override
    public boolean isWritable() {
        return resourceLocator.isWritable();
    }

    @Nullable
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
