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
package org.gitools.ui.fileimport.wizard.excel;

import org.gitools.persistence.formats.FileFormat;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileFormatFilter;
import org.gitools.ui.wizard.common.FileChooserPage;

import java.io.File;
import java.util.List;

public class ImportExcelWizard extends AbstractWizard {

    private FileChooserPage fileSelectionPage;
    private SelectColumnsPage selectColumnsPage;

    public ImportExcelWizard() {
        setTitle("Import an Excel matrix");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.excel48, 48));
        setHelpContext("import_excel");
    }


    @Override
    public void addPages() {

        fileSelectionPage = new FileChooserPage();
        fileSelectionPage.setTitle("Select Excel file");
        fileSelectionPage.setFileFilter(new FileFormatFilter("Microsoft Excel files", new FileFormat("Excel", "xls"), new FileFormat("Excel", "xlsx")));
        addPage(fileSelectionPage);

        selectColumnsPage = new SelectColumnsPage();
        selectColumnsPage.setTitle("Select rows, columns and values headers");
        addPage(selectColumnsPage);


    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {

        if (page == fileSelectionPage) {
            selectColumnsPage.setReader(new ExcelReader(fileSelectionPage.getSelectedFile()));
        }

        return super.getNextPage(page);
    }

    @Override
    public void performFinish() {

        File excelFile = fileSelectionPage.getSelectedFile();

        ExcelReader reader = selectColumnsPage.getReader();

        Settings.getDefault().setLastPath(excelFile.getParent());
        Settings.getDefault().save();

        int columns = selectColumnsPage.getSelectedColumn();
        int rows = selectColumnsPage.getSelectedRow();
        List<Integer> values = selectColumnsPage.getSelectedValues();
        JobRunnable loadFile = new CommandConvertAndLoadExcelFile(columns, rows, values, reader);
        JobThread.execute(AppFrame.get(), loadFile);

        AppFrame.get().setStatusText("Done.");
    }
}
