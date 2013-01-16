package org.gitools.ui.fileimport.wizard.excel;

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.FileChooserPage;

import javax.swing.filechooser.FileNameExtensionFilter;
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
        fileSelectionPage.setFileFilter(new FileNameExtensionFilter("Microsoft Excel files", "xls", "xlsx"));
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
        JobThread.execute(AppFrame.instance(), loadFile);

        AppFrame.instance().setStatusText("Done.");
    }
}
