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

import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.analysis.htest.oncozet.OncodriveCommand;
import org.gitools.persistence._DEPRECATED.PersistenceUtils;
import org.gitools.ui.analysis.htest.editor.OncodriveAnalysisEditor;
import org.gitools.ui.analysis.htest.wizard.OncodriveAnalysisWizard;
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
public class NewOncodriveAnalysisAction extends BaseAction {

    private static final long serialVersionUID = -8592231961109105958L;

    public NewOncodriveAnalysisAction() {
        super("OncoDrive analysis ...");

        setDesc("Run an oncodrive analysis");
        setMnemonic(KeyEvent.VK_O);

        setDefaultEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final OncodriveAnalysisWizard wizard = new OncodriveAnalysisWizard();

        WizardDialog wizDlg = new WizardDialog(AppFrame.get(), wizard);

        wizDlg.open();

        if (wizDlg.isCancelled()) {
            return;
        }

        final OncodriveAnalysis analysis = wizard.getAnalysis();

        File populationFile = wizard.getPopulationFile();
        File modulesFile = wizard.getModulesFile();

        final OncodriveCommand cmd = new OncodriveCommand(analysis, wizard.getDataFileFormat(), wizard.getDataFile().getAbsolutePath(), wizard.getSelectedValueIndex(), populationFile != null ? populationFile.getAbsolutePath() : null, wizard.getPopulationDefaultValue(), wizard.getModulesFileFormat(), modulesFile != null ? modulesFile.getAbsolutePath() : null, wizard.getWorkdir(), wizard.getFileName());

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(@NotNull IProgressMonitor monitor) {
                try {
                    cmd.run(monitor);

                    if (monitor.isCancelled()) {
                        return;
                    }

                    final OncodriveAnalysisEditor editor = new OncodriveAnalysisEditor(analysis);

                    editor.setName(PersistenceUtils.getBaseName(wizard.getFileName()));

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
