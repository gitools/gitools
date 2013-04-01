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

@Deprecated
public class FileFormat
{

    private String title;
    private String extension;
    private String mime;
    private boolean titleWithExtension;
    private boolean allowGzExtension;

    public FileFormat(String title, String extension)
    {
        this(title, extension, null);
    }

    public FileFormat(String title, String extension, String mime)
    {
        this(title, extension, mime, true, true);
    }

    public FileFormat(String title, String extension, String mime, boolean titleWithExtension, boolean allowGzExtension)
    {
        this.title = title;
        this.extension = extension;
        this.mime = mime;
        this.titleWithExtension = titleWithExtension;
        this.allowGzExtension = allowGzExtension;
    }

    public String getTitle()
    {
        return title;
    }

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

    public String getMime()
    {
        return mime;
    }

    public boolean checkExtension(String fileName)
    {
        fileName = fileName.toLowerCase();
        String ext = extension.toLowerCase();
        return fileName.endsWith(ext)
                || (allowGzExtension && fileName.endsWith(ext + ".gz"));
    }

    @Override
    public String toString()
    {
        return titleWithExtension ? getTitleWithExtension() : getTitle();
    }
}
