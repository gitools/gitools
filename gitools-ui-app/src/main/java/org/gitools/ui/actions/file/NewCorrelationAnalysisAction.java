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
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.correlation.CorrelationCommand;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.persistence.formats.analysis.HeatmapFormat;
import org.gitools.ui.analysis.correlation.editor.CorrelationAnalysisEditor;
import org.gitools.ui.analysis.correlation.wizard.CorrelationAnalysisFromFileWizard;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @noinspection ALL
 */
public class NewCorrelationAnalysisAction extends BaseAction {

    private static final long serialVersionUID = -8917512377366424724L;

    public NewCorrelationAnalysisAction() {
        super("Correlation analysis ...");

        setDesc("Run a correlation analysis");
        setMnemonic(KeyEvent.VK_C);

        setDefaultEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final CorrelationAnalysisFromFileWizard wizard = new CorrelationAnalysisFromFileWizard();

        WizardDialog wizDlg = new WizardDialog(Application.get(), wizard);

        wizDlg.open();

        if (wizDlg.isCancelled()) {
            return;
        }

        final CorrelationAnalysis analysis = wizard.getAnalysis();

        final CorrelationCommand cmd = new CorrelationCommand(analysis, wizard.getDataFileFormat(), wizard.getDataFile().getAbsolutePath(), wizard.getWorkdir(), wizard.getFileName());

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    cmd.run(monitor);

                    if (monitor.isCancelled()) {
                        return;
                    }

                    final CorrelationAnalysisEditor editor = new CorrelationAnalysisEditor(analysis);

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
