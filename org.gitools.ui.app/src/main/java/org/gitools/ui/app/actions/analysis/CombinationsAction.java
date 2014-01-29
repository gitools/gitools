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
package org.gitools.ui.app.actions.analysis;

import org.apache.commons.io.FilenameUtils;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.analysis.combination.CombinationCommand;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.core.model.IModuleMap;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.formats.analysis.CombinationAnalysisFormat;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.analysis.combination.editor.CombinationAnalysisEditor;
import org.gitools.ui.app.analysis.combination.wizard.CombinationAnalysisWizard;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class CombinationsAction extends HeatmapAction {

    public CombinationsAction() {
        super("Combinations...");
        setMnemonic(KeyEvent.VK_M);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        IMatrixView matrixView = getHeatmap();

        final CombinationAnalysisWizard wizard = new CombinationAnalysisWizard();
        wizard.setExamplePageEnabled(false);
        wizard.setDataFromMemory(true);

        IMatrixLayers layers = matrixView.getLayers();
        String[] attributes = new String[layers.size()];

        for (int i = 0; i < layers.size(); i++) {
            attributes[i] = layers.get(i).getId();
        }

        wizard.setAttributes(attributes);
        wizard.setSaveFilePageEnabled(false);

        WizardDialog wizDlg = new WizardDialog(Application.get(), wizard);

        wizDlg.open();

        if (wizDlg.isCancelled()) {
            return;
        }

        final CombinationAnalysis analysis = wizard.getAnalysis();
        analysis.setData(new ResourceReference<IMatrix>("data", matrixView));

        File columnSetsFile = wizard.getColumnSetsPage().getFile();
        String columnSetsPath = columnSetsFile != null ? columnSetsFile.getAbsolutePath() : null;
        IResourceFormat columnSetsFormat = columnSetsFile != null ? wizard.getColumnSetsPage().getFileFormat().getFormat(IModuleMap.class) : null;

        final CombinationCommand cmd = new CombinationCommand(analysis, null, null, columnSetsFormat, columnSetsPath, null, null);
        cmd.setStoreAnalysis(false);

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    cmd.run(monitor);

                    if (monitor.isCancelled()) {
                        return;
                    }

                    final CombinationAnalysisEditor editor = new CombinationAnalysisEditor(analysis);

                    String ext = FilenameUtils.getExtension(getSelectedEditor().getName());
                    String analysisTitle = analysis.getTitle();

                    if (!analysisTitle.equals("")) {
                        editor.setName(analysis.getTitle() + "." + CombinationAnalysisFormat.EXTENSION);
                    } else {
                        editor.setName(getEditorsPanel().deriveName(getSelectedEditor().getName(), ext, "", CombinationAnalysisFormat.EXTENSION));
                    }

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
