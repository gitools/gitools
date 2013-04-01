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
package org.gitools.ui.utils;

import org.gitools.persistence._DEPRECATED.FileFormat;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileFormatFilter extends FileFilter
{

    private String description;
    private String mime;

    private FileFormat format;

    private FileFormat[] formats;

    public FileFormatFilter(String description, String mime)
    {
        this.description = description;
        this.mime = mime;
    }

    public FileFormatFilter(String description, String mime, FileFormat[] formats)
    {
        this(description, mime);
        this.formats = formats;
    }

    public FileFormatFilter(FileFormat format)
    {
        this(format.getTitleWithExtension(), format.getMime());
        this.format = format;
    }

    @Override
    public boolean accept(File f)
    {
        if (f.isDirectory())
        {
            return true;
        }

        if (format != null)
        {
            return format.checkExtension(f.getName());
        }
        else if (formats != null)
        {
            for (FileFormat ff : formats)
                if (ff.checkExtension(f.getName()))
                {
                    return true;
                }
            return false;
        }

        return true;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    public String getMime()
    {
        return mime;
    }

    public FileFormat getFormat()
    {
        return format;
    }
}
