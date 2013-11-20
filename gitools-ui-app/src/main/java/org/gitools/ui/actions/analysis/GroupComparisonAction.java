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
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.analysis.groupcomparison.GroupComparisonProcessor;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.formats.analysis.GroupComparisonAnalysisFormat;
import org.gitools.ui.actions.HeatmapAction;
import org.gitools.ui.analysis.editor.AnalysisDetailsEditor;
import org.gitools.ui.analysis.groupcomparison.editor.GroupComparisonAnalysisEditor;
import org.gitools.ui.analysis.groupcomparison.wizard.GroupComparisonAnalysisFromEditorWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GroupComparisonAction extends HeatmapAction {

    public GroupComparisonAction() {
        super("Group Comparison analysis");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final EditorsPanel editorPanel = AppFrame.get().getEditorsPanel();

        final IEditor currentEditor = editorPanel.getSelectedEditor();

        Heatmap heatmap = getHeatmap();

        IMatrixLayers attributes = heatmap.getLayers();
        String[] attributeNames = new String[attributes.size()];
        for (int i = 0; i < attributes.size(); i++)
            attributeNames[i] = attributes.get(i).getName();

        GroupComparisonAnalysisFromEditorWizard wiz = new GroupComparisonAnalysisFromEditorWizard(heatmap);
        WizardDialog dlg = new WizardDialog(AppFrame.get(), wiz);
        dlg.setVisible(true);

        if (dlg.isCancelled()) {
            return;
        }

        final GroupComparisonAnalysis analysis = wiz.getAnalysis();

        analysis.setData(new ResourceReference<IMatrix>("data", heatmap));

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    new GroupComparisonProcessor(analysis).run(monitor);

                    if (monitor.isCancelled()) {
                        return;
                    }

                    final AnalysisDetailsEditor editor = new GroupComparisonAnalysisEditor(analysis);
                    //TODO: adapt to group comparison analysis

                    String ext = FilenameUtils.getExtension(currentEditor.getName());
                    String analysisTitle = analysis.getTitle();

                    if (!analysisTitle.equals("")) {
                        editor.setName(analysis.getTitle() + "." + GroupComparisonAnalysisFormat.EXTENSION);
                    } else {
                        editor.setName(editorPanel.deriveName(currentEditor.getName(), ext, "", GroupComparisonAnalysisFormat.EXTENSION));
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
