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

import static com.google.common.base.Predicates.in;
import org.apache.commons.io.FilenameUtils;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.correlation.CorrelationProcessor;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.formats.analysis.CorrelationAnalysisFormat;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.analysis.correlation.editor.CorrelationAnalysisEditor;
import org.gitools.ui.app.analysis.correlation.wizard.CorrelationAnalysisFromEditorWizard;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class CorrelationsAction extends HeatmapAction {

    public CorrelationsAction() {
        super("Correlations...");

        setMnemonic(KeyEvent.VK_C);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        IMatrixView matrixView = getHeatmap();
        IMatrixLayers attributes = matrixView.getLayers();
        String[] attributeNames = new String[attributes.size()];
        for (int i = 0; i < attributes.size(); i++)
            attributeNames[i] = attributes.get(i).getName();

        CorrelationAnalysisFromEditorWizard wiz = new CorrelationAnalysisFromEditorWizard(attributeNames);
        WizardDialog dlg = new WizardDialog(Application.get(), wiz);
        dlg.setVisible(true);

        if (dlg.isCancelled()) {
            return;
        }

        final CorrelationAnalysis analysis = wiz.getAnalysis();

        if (!analysis.isTransposeData()) {
            if (matrixView.getColumns().getSelected().size() > 0) {
                Heatmap mv = new Heatmap(matrixView);
                mv.getColumns().show(in(mv.getColumns().getSelected()));
                matrixView = mv;
            }
        } else {
            if (matrixView.getRows().getSelected().size() > 0) {
                Heatmap mv = new Heatmap(matrixView);
                mv.getRows().show(in(mv.getRows().getSelected()));
                matrixView = mv;
            }
        }

        analysis.setData(new ResourceReference<IMatrix>("data", matrixView));

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    new CorrelationProcessor(analysis).run(monitor);

                    if (monitor.isCancelled()) {
                        return;
                    }

                    final CorrelationAnalysisEditor editor = new CorrelationAnalysisEditor(analysis);

                    String ext = FilenameUtils.getExtension(getSelectedEditor().getName());
                    String analysisTitle = analysis.getTitle();

                    if (!analysisTitle.equals("")) {
                        editor.setName(analysis.getTitle() + "." + CorrelationAnalysisFormat.EXTENSION);
                    } else {
                        editor.setName(getEditorsPanel().deriveName(getSelectedEditor().getName(), ext, "", CorrelationAnalysisFormat.EXTENSION));
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
