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

public abstract class AbstractImportModulesAction extends BaseAction {

    public AbstractImportModulesAction(String name) {
        super(name);
        setDefaultEnabled(true);
    }

    protected abstract EnsemblKeggModulesImporter getImporter();

    protected abstract ModulesImportWizard getWizard(EnsemblKeggModulesImporter importer);

    @Override
    public void actionPerformed(ActionEvent e) {

        final EnsemblKeggModulesImporter importer = getImporter();
        final ModulesImportWizard wz = getWizard(importer);

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

                        //TODO Use PersistenceManager
                        String prefix = FilenameUtils.getName(file.getName());
                        file = new File(file.getParentFile(), prefix + "_annotations.tsv");
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
