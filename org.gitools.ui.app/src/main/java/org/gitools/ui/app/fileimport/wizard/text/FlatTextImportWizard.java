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
package org.gitools.ui.app.fileimport.wizard.text;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.persistence.FileFormat;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.ui.app.fileimport.ImportWizard;
import org.gitools.ui.app.fileimport.wizard.text.reader.FlatTextImporter;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.utils.FileFormatFilter;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.utils.readers.profile.ReaderProfile;
import org.gitools.utils.readers.profile.ReaderProfileValidationException;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class FlatTextImportWizard extends AbstractWizard implements ImportWizard {

    private static FileFormat[] FILE_FORMATS = new FileFormat[]{new FileFormat("CSV", "csv"), new FileFormat("TSV", "tsv"), new FileFormat("TXT", "txt")};
    private static FileFormatFilter FILE_FORMAT_FILTER = new FileFormatFilter("Text files", FILE_FORMATS);

    private IResourceLocator locator;
    private SelectDataLayoutPage selectDataLayoutPage;
    private SelectTableColumnsPage selectTableColumnsPage;
    private SelectMatrixColumnsPage selectMatrixColumnsPage;
    private IProgressMonitor monitor;
    private WizardDialog wizDlg;
    private Callback callback;

    public FlatTextImportWizard() {
        setTitle("Import a text file");
        setHelpContext("import_text");
    }

    @Override
    public FileFormatFilter getFileFormatFilter() {
        return FILE_FORMAT_FILTER;
    }

    public void setLocator(IResourceLocator locator) {
        this.locator = locator;
    }

    @Override
    public void setCallback(Callback callbackFunction) {
        this.callback = callbackFunction;
    }

    @Override
    public void addPages() {

        selectDataLayoutPage = new SelectDataLayoutPage(new FlatTextImporter(locator, monitor, true));
        selectDataLayoutPage.setTitle("Select data Layout");
        selectDataLayoutPage.setMessage(MessageStatus.INFO, "");
        addPage(selectDataLayoutPage);

        selectTableColumnsPage = new SelectTableColumnsPage();
        selectTableColumnsPage.setTitle("Table format");
        addPage(selectTableColumnsPage);

        selectMatrixColumnsPage = new SelectMatrixColumnsPage();
        selectMatrixColumnsPage.setTitle("Matrix format");
        addPage(selectMatrixColumnsPage);
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {

        IWizardPage nextPage = super.getNextPage(getCurrentPage());


        if (selectDataLayoutPage.equals(page)) {
            String layout = selectDataLayoutPage.getReader().getReaderProfile().getLayout();
            if (layout.equals(ReaderProfile.MATRIX)) {
                selectMatrixColumnsPage.setReader(selectDataLayoutPage.getReader());
                nextPage = selectMatrixColumnsPage;
            } else if (layout.equals(ReaderProfile.TABLE)) {
                selectTableColumnsPage.setReader(selectDataLayoutPage.getReader());
                nextPage = selectTableColumnsPage;
            }
        }
        return nextPage;
    }

    @Override
    public boolean isLastPage(IWizardPage page) {
        if (page.equals(selectTableColumnsPage) || page.equals(selectMatrixColumnsPage))
            return true;

        return super.isLastPage(page);
    }

    @Override
    public void performFinish() {
        if (getCurrentPage() instanceof IFileImportStep) {
            IFileImportStep page = (IFileImportStep) getCurrentPage();
            try {
                page.finish();
            } catch (ReaderProfileValidationException e) {
                e.printStackTrace();
            }

            // validation succeded, close dialog and read file

            wizDlg.setVisible(false);
            FlatTextImporter reader = page.getReader();
            JobRunnable loadFile = new CommandConvertAndLoadCsvFile(reader) {
                @Override
                public void afterLoad(IResource resource, IProgressMonitor monitor) throws CommandException {
                    callback.afterLoad(resource, monitor);
                }
            };
            JobThread.execute(Application.get(), loadFile);
            Application.get().setStatusText("Done.");
        } else {
            throw new RuntimeException("Premature finish");
        }

    }


    @Override
    public void run(IProgressMonitor monitor) throws IOException, InvocationTargetException, InterruptedException {
        init();
        this.monitor = monitor;
        this.wizDlg = new WizardDialog(Application.get(), this);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                wizDlg.open();
            }
        });

    }
}
