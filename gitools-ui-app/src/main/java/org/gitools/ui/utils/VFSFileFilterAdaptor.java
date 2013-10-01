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

import com.googlecode.vfsjfilechooser2.filechooser.AbstractVFSFileFilter;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;

import java.util.ArrayList;
import java.util.List;

public class VFSFileFilterAdaptor extends AbstractVFSFileFilter {
    private FileFormatFilter filter;

    public VFSFileFilterAdaptor(FileFormatFilter filter) {
        this.filter = filter;
    }

    @Override
    public boolean accept(FileObject f) {
        boolean directory;
        try {
            directory = (f.getType() == FileType.FOLDER);
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        }
        return filter.accept(directory, f.getName().toString());
    }

    @Override
    public String getDescription() {
        return filter.getDescription();
    }

    public static List<VFSFileFilterAdaptor> adapt(FileFormatFilter[] filters) {
        List<VFSFileFilterAdaptor> result = new ArrayList<VFSFileFilterAdaptor>(filters.length);

        for (FileFormatFilter filter : filters) {
            result.add(new VFSFileFilterAdaptor(filter));
        }

        return result;
    }

    public FileFormatFilter getFilter() {
        return filter;
    }
}
