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
package org.gitools.ui.fileimport;

import org.gitools.api.resource.IResourceLocator;
import org.gitools.persistence.formats.FileFormat;
import org.gitools.ui.fileimport.wizard.excel.ExcelImportWizard;
import org.gitools.ui.utils.FileFormatFilter;

import java.util.*;

public class ImportManager {

    private static ImportManager INSTANCE = new ImportManager();
    public static ImportManager get() {
        return INSTANCE;
    }

    private Map<FileFormatFilter, ImportWizard> wizards = new HashMap<>();

    private ImportManager() {
        super();

        register(new ExcelImportWizard());
    }

    public Collection<FileFormatFilter> getFileFormatFilters() {
        return wizards.keySet();
    }

    public Collection<FileFormat> getFileFormats() {
        List<FileFormat> formats = new ArrayList<>();

        for (FileFormatFilter filter : getFileFormatFilters()) {
            Collections.addAll(formats, filter.getFormats());
        }

        return formats;
    }

    public boolean isImportable(IResourceLocator locator) {
        return getFileFormatFilter(locator) != null;
    }

    public ImportWizard getWizard(IResourceLocator locator) {

        ImportWizard wizard = wizards.get(getFileFormatFilter(locator));
        wizard.setLocator(locator);

        return wizard;
    }

    public void register(ImportWizard importWizard) {
        wizards.put(importWizard.getFileFormatFilter(), importWizard);
    }

    private FileFormatFilter getFileFormatFilter(IResourceLocator locator) {

        for (FileFormatFilter filter : wizards.keySet()) {
            if (filter.accept(false, locator.getExtension())) {
                return filter;
            }
        }

        return null;
    }

}
