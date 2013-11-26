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
