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
package org.gitools.ui.actions.analysis;

import org.apache.commons.io.FilenameUtils;
import org.gitools.core.analysis.combination.CombinationAnalysis;
import org.gitools.core.analysis.combination.CombinationCommand;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.model.ModuleMap;
import org.gitools.core.persistence.IResourceFormat;
import org.gitools.core.persistence.ResourceReference;
import org.gitools.core.persistence.formats.analysis.CombinationAnalysisFormat;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.analysis.combination.editor.CombinationAnalysisEditor;
import org.gitools.ui.analysis.combination.wizard.CombinationAnalysisWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class CombinationsAction extends BaseAction {

    public CombinationsAction() {
        super("Combinations");
        setDesc("Combinations");
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final EditorsPanel editorPanel = AppFrame.get().getEditorsPanel();

        final IEditor currentEditor = editorPanel.getSelectedEditor();

        IMatrixView matrixView = ActionUtils.getMatrixView();

        if (matrixView == null) {
            return;
        }

        final CombinationAnalysisWizard wizard = new CombinationAnalysisWizard();
        wizard.setExamplePageEnabled(false);
        wizard.setDataFromMemory(true);
        wizard.setAttributes(matrixView.getLayers());
        wizard.setSaveFilePageEnabled(false);

        WizardDialog wizDlg = new WizardDialog(AppFrame.get(), wizard);

        wizDlg.open();

        if (wizDlg.isCancelled()) {
            return;
        }

        final CombinationAnalysis analysis = wizard.getAnalysis();
        analysis.setData(new ResourceReference<IMatrix>("data", matrixView));

        File columnSetsFile = wizard.getColumnSetsPage().getFile();
        String columnSetsPath = columnSetsFile != null ? columnSetsFile.getAbsolutePath() : null;
        IResourceFormat columnSetsFormat = columnSetsFile != null ? wizard.getColumnSetsPage().getFileFormat().getFormat(ModuleMap.class) : null;

        final CombinationCommand cmd = new CombinationCommand(analysis, null, null, columnSetsFormat, columnSetsPath, null, null);
        cmd.setStoreAnalysis(false);

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(@NotNull IProgressMonitor monitor) {
                try {
                    cmd.run(monitor);

                    if (monitor.isCancelled()) {
                        return;
                    }

                    final CombinationAnalysisEditor editor = new CombinationAnalysisEditor(analysis);

                    String ext = FilenameUtils.getExtension(currentEditor.getName());
                    String analysisTitle = analysis.getTitle();

                    if (!analysisTitle.equals("")) {
                        editor.setName(analysis.getTitle() + "." + CombinationAnalysisFormat.EXTENSION);
                    } else {
                        editor.setName(editorPanel.deriveName(currentEditor.getName(), ext, "", CombinationAnalysisFormat.EXTENSION));
                    }

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
