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

import com.alee.extended.filefilter.DefaultFileFilter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DefaultFileFilterAdaptor extends DefaultFileFilter {
    private FileFilter filter;

    public DefaultFileFilterAdaptor(FileFilter filter) {
        this.filter = filter;
    }

    @Override
    public ImageIcon getIcon() {
        return null;
    }

    @Override
    public String getDescription() {
        return filter.getDescription();
    }

    @Override
    public boolean accept(File pathname) {
        return filter.accept(pathname);
    }

    public static List<DefaultFileFilter> adapt(FileFilter[] filters) {
        List<DefaultFileFilter> result = new ArrayList<DefaultFileFilter>(filters.length);

        for (FileFilter filter : filters) {
            result.add(new DefaultFileFilterAdaptor(filter));
        }

        return result;
    }
}
