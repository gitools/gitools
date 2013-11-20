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

import org.apache.commons.io.FilenameUtils;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.core.model.IModuleMap;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.datasources.kegg.modules.EnsemblKeggModulesImporter;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.formats.matrix.GmtMatrixFormat;
import org.gitools.persistence.formats.matrix.GmxMatrixFormat;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.IconNames;
import org.gitools.ui.datasources.go.wizard.GoModulesImportWizard;
import org.gitools.ui.modules.wizard.ModulesImportWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ImportGoModulesAction extends BaseAction {

    public ImportGoModulesAction() {
        super("Gene Ontology ...");
        setLargeIconFromResource(IconNames.GO24);
        setSmallIconFromResource(IconNames.GO16);
        setDefaultEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final EnsemblKeggModulesImporter importer = new EnsemblKeggModulesImporter(false, true);

        final ModulesImportWizard wz = new GoModulesImportWizard(importer);

        WizardDialog dlg = new WizardDialog(AppFrame.get(), wz);
        dlg.setVisible(true);

        if (dlg.isCancelled()) {
            return;
        }

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    Map<String, String> descriptions = new HashMap<>();
                    IModuleMap mmap = importer.importMap(monitor, descriptions);
                    if (!monitor.isCancelled()) {
                        String extension = wz.getSaveFilePage().getFormat().getExtension();
                        File file = wz.getSaveFilePage().getPathAsFile();
                        IResourceLocator resourceLocator = new UrlResourceLocator(file);
                        if (GmxMatrixFormat.EXTENSION.equals(extension) || GmtMatrixFormat.EXTENSION.equals(extension)) {

                            IMatrix mat = MatrixUtils.moduleMapToMatrix(mmap);

                            IResourceFormat format = PersistenceManager.get().getFormat(extension, mat.getClass());

                            PersistenceManager.get().store(resourceLocator, mat, format, monitor);
                        } else {
                            IResourceFormat format = PersistenceManager.get().getFormat(extension, mmap.getClass());
                            PersistenceManager.get().store(resourceLocator, mmap, format, monitor);
                        }

                        //TODO Use PersistenceManager to save this file
                        String prefix = FilenameUtils.getName(resourceLocator.getName());
                        File annotations = new File(file.getParentFile(), prefix + "_annotations.tsv");
                        monitor.begin("Saving module annotations ...", mmap.getModules().size());
                        PrintWriter pw = new PrintWriter(file);
                        pw.println("id\tname");
                        for (String module : mmap.getModules()) {
                            pw.print(module);
                            pw.print('\t');
                            pw.println(descriptions.get(module));
                            monitor.worked(1);
                        }
                        pw.close();
                        monitor.end();

                        setStatus("Ok");
                    } else {
                        setStatus("Operation cancelled");
                    }
                } catch (Throwable ex) {
                    monitor.exception(ex);
                }
            }
        });
    }

    private void setStatus(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AppFrame.get().setStatusText(msg);
            }
        });
    }
}
