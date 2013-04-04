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
package org.gitools.ui.actions.file;

import org.gitools.kegg.modules.EnsemblKeggModulesImporter;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.IResourceFormat;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence._DEPRECATED.MimeTypes;
import org.gitools.persistence._DEPRECATED.PersistenceUtils;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.IconNames;
import org.gitools.ui.kegg.wizard.KeggModulesImportWizard;
import org.gitools.ui.modules.wizard.ModulesImportWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.PrintWriter;

public class ImportKeggModulesAction extends BaseAction
{

    public ImportKeggModulesAction()
    {
        super("KEGG Pathways ...");
        setLargeIconFromResource(IconNames.KEGG24);
        setSmallIconFromResource(IconNames.KEGG16);
        setDefaultEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        final EnsemblKeggModulesImporter importer =
                new EnsemblKeggModulesImporter(true, false);

        final ModulesImportWizard wz = new KeggModulesImportWizard(importer);

        WizardDialog dlg = new WizardDialog(AppFrame.get(), wz);
        dlg.setVisible(true);

        if (dlg.isCancelled())
        {
            return;
        }

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(@NotNull IProgressMonitor monitor)
            {
                try
                {
                    ModuleMap mmap = importer.importMap(monitor);
                    if (!monitor.isCancelled())
                    {
                        String extension = wz.getSaveFilePage().getFormat().getExtension();
                        File file = wz.getSaveFilePage().getPathAsFile();
                        IResourceLocator resourceLocator = new UrlResourceLocator(file);
                        if (MimeTypes.GENE_MATRIX.equals(extension)
                                || MimeTypes.GENE_MATRIX_TRANSPOSED.equals(extension))
                        {

                            BaseMatrix mat = MatrixUtils.moduleMapToMatrix(mmap);
                            IResourceFormat format = PersistenceManager.get().getFormat(extension, mat.getClass());
                            PersistenceManager.get().store(resourceLocator, mat, format, monitor);
                        }
                        else
                        {
                            IResourceFormat format = PersistenceManager.get().getFormat(extension, mmap.getClass());
                            PersistenceManager.get().store(resourceLocator, mmap, format, monitor);
                        }

                        //TODO Use PersistenceManager
                        String prefix = PersistenceUtils.getFileName(file.getName());
                        file = new File(file.getParentFile(), prefix + "_annotations.tsv");
                        monitor.begin("Saving module annotations ...", mmap.getModuleCount());
                        PrintWriter pw = new PrintWriter(file);
                        pw.println("id\tname");
                        for (int i = 0; i < mmap.getModuleCount(); i++)
                        {
                            pw.print(mmap.getModuleName(i));
                            pw.print('\t');
                            pw.println(mmap.getModuleDescription(i));
                            monitor.worked(1);
                        }
                        pw.close();
                        monitor.end();

                        setStatus("Ok");
                    }
                    else
                    {
                        setStatus("Operation cancelled");
                    }
                } catch (Throwable ex)
                {
                    monitor.exception(ex);
                }
            }
        });
    }

    private void setStatus(final String msg)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                AppFrame.get().setStatusText(msg);
            }
        });
    }
}
