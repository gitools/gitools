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
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentCommand;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.ui.analysis.htest.editor.EnrichmentAnalysisEditor;
import org.gitools.ui.analysis.htest.wizard.EnrichmentAnalysisWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * @noinspection ALL
 */
public class NewEnrichmentAnalysisAction extends BaseAction {

    private static final long serialVersionUID = -8592231961109105958L;

    public NewEnrichmentAnalysisAction() {
        super("Enrichment analysis ...");

        setDesc("Run an enrichment analysis");
        setMnemonic(KeyEvent.VK_E);
        setDefaultEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final EnrichmentAnalysisWizard wizard = new EnrichmentAnalysisWizard();

        WizardDialog wizDlg = new WizardDialog(AppFrame.get(), wizard);

        wizDlg.open();

        if (wizDlg.isCancelled()) {
            return;
        }

        final EnrichmentAnalysis analysis = wizard.getAnalysis();

        File populationFile = wizard.getPopulationFile();

        final EnrichmentCommand cmd = new EnrichmentCommand(analysis, wizard.getDataFileFormat(), wizard.getDataFile().getAbsolutePath(), wizard.getSelectedValueIndex(), populationFile != null ? populationFile.getAbsolutePath() : null, wizard.getPopulationDefaultValue(), wizard.getModulesFileFormat(), wizard.getModulesFile().getAbsolutePath(), wizard.getWorkdir(), wizard.getFileName());

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    cmd.run(monitor);

                    if (monitor.isCancelled()) {
                        return;
                    }

                    final EnrichmentAnalysisEditor editor = new EnrichmentAnalysisEditor(analysis);

                    editor.setName(FilenameUtils.getBaseName(wizard.getFileName()));

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            AppFrame.get().getEditorsPanel().addEditor(editor);
                            AppFrame.get().refresh();
                        }
                    });

                    monitor.end();

                    AppFrame.get().setStatusText("Ok.");
                } catch (Throwable ex) {
                    monitor.exception(ex);
                }
            }
        });
    }
}
