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
package org.gitools.ui.app.actions.file;

import org.apache.commons.io.FilenameUtils;
import org.gitools.analysis.combination.ConvertModuleMapToMatrixResourceReference;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.analysis.overlapping.OverlappingCommand;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.core.model.IModuleMap;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.formats.analysis.HeatmapFormat;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.app.analysis.overlapping.OverlappingAnalysisEditor;
import org.gitools.ui.app.analysis.overlapping.wizard.OverlappingAnalysisWizard;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class NewOverlappingAnalysisAction extends BaseAction {

    private static final long serialVersionUID = -8917512377366424724L;

    public NewOverlappingAnalysisAction() {
        super("Overlapping analysis ...");

        setDesc("Run an overlapping analysis");
        setMnemonic(KeyEvent.VK_L);

        setDefaultEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final OverlappingAnalysisWizard wizard = new OverlappingAnalysisWizard();

        WizardDialog wizDlg = new WizardDialog(Application.get(), wizard);

        wizDlg.open();

        if (wizDlg.isCancelled()) {
            return;
        }

        final OverlappingAnalysis analysis = wizard.getAnalysis();

        IResourceLocator resourceLocator = new UrlResourceLocator(wizard.getDataFilePage().getFile().getAbsolutePath());
        ResourceReference<IMatrix> sourceData;
        try {
            IResourceFormat<? extends IMatrix> resourceFormat = wizard.getDataFilePage().getFileFormat().getFormat(IMatrix.class);
            sourceData = new ResourceReference<>(resourceLocator, resourceFormat);
        } catch (PersistenceException ex) {

            // Allow to use ModuleMaps as IMatrix
            IResourceFormat<? extends IModuleMap> resourceFormat = wizard.getDataFilePage().getFileFormat().getFormat(IModuleMap.class);
            sourceData = new ConvertModuleMapToMatrixResourceReference(resourceLocator, resourceFormat);
        }

        analysis.setSourceData(new ResourceReference<>("source-data", sourceData.get()));

        final OverlappingCommand cmd = new OverlappingCommand(analysis, wizard.getWorkdir(), wizard.getFileName());

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    cmd.run(monitor);

                    if (monitor.isCancelled()) {
                        return;
                    }

                    final OverlappingAnalysisEditor editor = new OverlappingAnalysisEditor(analysis);

                    editor.setName(FilenameUtils.getName(wizard.getFileName()) + "." + HeatmapFormat.EXTENSION);

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            Application.get().getEditorsPanel().addEditor(editor);
                            Application.get().refresh();
                        }
                    });

                    monitor.end();

                    Application.get().setStatusText("Done.");
                } catch (Throwable ex) {
                    monitor.exception(ex);
                }
            }
        });
    }
}
