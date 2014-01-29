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
package org.gitools.ui.app.utils;

import org.gitools.persistence.formats.FileFormat;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileFormatFilter extends FileFilter {

    private final String description;
    private FileFormat[] formats;

    public FileFormatFilter(String description) {
        this.description = description;
    }

    public FileFormatFilter(String description, FileFormat... formats) {
        this(description);
        this.formats = formats;
    }

    public FileFormatFilter(FileFormat format) {
        this(format.getTitleWithExtension());
        this.formats = new FileFormat[] { format };
    }

    @Override
    public final boolean accept(File f) {
        return accept(f.isDirectory(), f.getName());
    }

    public boolean accept(boolean directory, String fileName) {

        if (directory) {
            return true;
        }

        if (formats != null) {
            for (FileFormat ff : formats) {
                if (ff.checkExtension(fileName)) {
                    return true;
                }
            }

            return false;
        }

        return true;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public FileFormat[] getFormats() {
        return formats;
    }
}
