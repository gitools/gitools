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

import org.jetbrains.annotations.NotNull;

import java.io.File;

@Deprecated
public class PersistenceUtils
{
    /**
     * Returns file name (including extension) without path
     */
    public static String getBaseName(@NotNull String path)
    {
        int sep = path.lastIndexOf(File.separatorChar);
        return path.substring(sep + 1);
    }

    /**
     * Returns the file name without extension.
     * It takes into account composed extension for .gz
     */
    public static String getFileName(@NotNull String path)
    {
        int dot = path.lastIndexOf('.');
        if (dot > 0 && path.substring(dot).equalsIgnoreCase(".gz"))
        {
            dot = path.substring(0, dot - 1).lastIndexOf('.');
        }

        int sep = path.lastIndexOf(File.separatorChar);
        return dot != -1 ? path.substring(sep + 1, dot) : path.substring(sep + 1);
    }

    /**
     * Returns only the extension of the file.
     * It takes into account composed extension for .gz
     */
    @NotNull
    public static String getExtension(@NotNull String path)
    {
        int dot = path.lastIndexOf('.');
        String ext = dot != -1 ? path.substring(dot + 1) : "";
        if (ext.equalsIgnoreCase("gz"))
        {
            String[] e = path.split("[.]");
            if (e.length >= 2)
            {
                ext = e[e.length - 2] + "." + e[e.length - 1];
            }
        }
        return ext;
    }
}
