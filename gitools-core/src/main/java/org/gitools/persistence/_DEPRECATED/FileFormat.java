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
package org.gitools.persistence._DEPRECATED;

import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceFormat;
import org.gitools.persistence.PersistenceManager;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class FileFormat
{

    private final String title;
    private final String extension;
    private final boolean titleWithExtension;
    private final boolean allowGzExtension;

    public FileFormat(String title, String extension)
    {
        this(title, extension, true, true);
    }

    public FileFormat(String title, String extension, boolean titleWithExtension, boolean allowGzExtension)
    {
        this.title = title;
        this.extension = extension;
        this.titleWithExtension = titleWithExtension;
        this.allowGzExtension = allowGzExtension;
    }

    public String getTitle()
    {
        return title;
    }

    @NotNull
    public String getTitleWithExtension()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append(" (").append(extension);
        if (allowGzExtension)
        {
            sb.append(", ").append(extension).append(".gz");
        }
        sb.append(")");
        return sb.toString();
    }

    public String getExtension()
    {
        return extension;
    }

    public boolean checkExtension(String fileName)
    {
        fileName = fileName.toLowerCase();
        String ext = extension.toLowerCase();
        return fileName.endsWith(ext) || (allowGzExtension && fileName.endsWith(ext + ".gz"));
    }

    public <R extends IResource> IResourceFormat<? extends R> getFormat(@NotNull Class<R> resourceClass)
    {
        return PersistenceManager.get().getFormat(getExtension(), resourceClass);
    }

    @NotNull
    @Override
    public String toString()
    {
        return titleWithExtension ? getTitleWithExtension() : getTitle();
    }
}
