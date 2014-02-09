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
package org.gitools.ui.app.fileimport.wizard.csv;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.persistence.FileFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.fileimport.ImportWizard;
import org.gitools.ui.app.utils.FileFormatFilter;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.WizardDialog;

import java.io.IOException;
import java.util.List;

public class CsvImportWizard extends AbstractWizard implements ImportWizard {

    private static FileFormat[] FILE_FORMATS = new FileFormat[] { new FileFormat("CSV", "csv"), new FileFormat("TSV", "tsv"), new FileFormat("TXT", "txt") };
    private static FileFormatFilter FILE_FORMAT_FILTER = new FileFormatFilter("Text files", FILE_FORMATS);

    private IResourceLocator locator;
    private SelectColumnsPage selectColumnsPage;

    public CsvImportWizard() {
        setTitle("Import a CSV file");
        //TODO setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.excel48, 48));
        setHelpContext("import_csv");
    }

    @Override
    public FileFormatFilter getFileFormatFilter() {
        return FILE_FORMAT_FILTER;
    }

    public void setLocator(IResourceLocator locator) {
        this.locator = locator;
    }

    @Override
    public void addPages() {
        selectColumnsPage = new SelectColumnsPage();
        selectColumnsPage.setTitle("Select rows, columns and values headers");
        selectColumnsPage.setReader(new CsvReader(locator));
        addPage(selectColumnsPage);

    }

    @Override
    public void performFinish() {
        CsvReader reader = selectColumnsPage.getReader();
        int columns = selectColumnsPage.getSelectedColumn();
        int rows = selectColumnsPage.getSelectedRow();
        List<Integer> values = selectColumnsPage.getSelectedValues();
        JobRunnable loadFile = new CommandConvertAndLoadCsvFile(columns, rows, values, reader);
        JobThread.execute(Application.get(), loadFile);
        Application.get().setStatusText("Done.");
    }


    @Override
    public void run(IProgressMonitor monitor) throws IOException {
        init();
        WizardDialog wizDlg = new WizardDialog(Application.get(), this);
        wizDlg.open();
    }
}
