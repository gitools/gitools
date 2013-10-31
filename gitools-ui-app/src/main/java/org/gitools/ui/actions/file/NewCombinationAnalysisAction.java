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
import org.gitools.core.analysis.combination.CombinationAnalysis;
import org.gitools.core.analysis.combination.CombinationCommand;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.model.IModuleMap;
import org.gitools.core.persistence.IResourceFormat;
import org.gitools.core.persistence.PersistenceManager;
import org.gitools.core.persistence.formats.analysis.HeatmapFormat;
import org.gitools.ui.analysis.combination.editor.CombinationAnalysisEditor;
import org.gitools.ui.analysis.combination.wizard.CombinationAnalysisWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * @noinspection ALL
 */
public class NewCombinationAnalysisAction extends BaseAction {

    private static final long serialVersionUID = 4604642713057641252L;

    public NewCombinationAnalysisAction() {
        super("Combination analysis ...");

        setDesc("Run a combination analysis");
        setMnemonic(KeyEvent.VK_B);

        setDefaultEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final CombinationAnalysisWizard wizard = new CombinationAnalysisWizard();

        WizardDialog wizDlg = new WizardDialog(AppFrame.get(), wizard);

        wizDlg.open();

        if (wizDlg.isCancelled()) {
            return;
        }

        final CombinationAnalysis analysis = wizard.getAnalysis();

        final String analysisPath = wizard.getSaveFilePage().getFileName();
        File columnSetsFile = wizard.getColumnSetsPage().getFile();
        String columnSetsPath = columnSetsFile != null ? columnSetsFile.getAbsolutePath() : null;
        String columnSetsMime = columnSetsFile != null ? wizard.getColumnSetsPage().getFileFormat().getExtension() : null;

        IResourceFormat columnSetsFormat = PersistenceManager.get().getFormat(columnSetsMime, IModuleMap.class);
        IResourceFormat dataFormat = PersistenceManager.get().getFormat(wizard.getDataFilePage().getFileFormat().getExtension(), IMatrix.class);

        final CombinationCommand cmd = new CombinationCommand(analysis, dataFormat, wizard.getDataFilePage().getFile().getAbsolutePath(), columnSetsFormat, columnSetsPath, wizard.getSaveFilePage().getFolder(), analysisPath);

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(@NotNull IProgressMonitor monitor) {
                try {
                    cmd.run(monitor);

                    if (monitor.isCancelled()) {
                        return;
                    }

                    final CombinationAnalysisEditor editor = new CombinationAnalysisEditor(analysis);

                    editor.setName(FilenameUtils.getName(analysisPath) + "." + HeatmapFormat.EXTENSION);

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            AppFrame.get().getEditorsPanel().addEditor(editor);
                            AppFrame.get().refresh();
                        }
                    });

                    monitor.end();

                    AppFrame.get().setStatusText("Done.");
                } catch (Throwable ex) {
                    monitor.exception(ex);
                }
            }
        });
    }
}
