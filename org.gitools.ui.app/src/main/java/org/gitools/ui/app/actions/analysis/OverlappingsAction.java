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
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.analysis.overlapping.OverlappingProcessor;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.formats.analysis.OverlappingAnalysisFormat;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.analysis.overlapping.OverlappingAnalysisEditor;
import org.gitools.ui.app.analysis.overlapping.wizard.OverlappingAnalysisWizard;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class OverlappingsAction extends HeatmapAction {

    public OverlappingsAction() {
        super("Overlapping...");
        setMnemonic(KeyEvent.VK_V);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IMatrixView matrixView = getHeatmap();

        final OverlappingAnalysisWizard wiz = new OverlappingAnalysisWizard();
        wiz.setExamplePageEnabled(false);
        wiz.setDataFromMemory(true);
        wiz.setAttributes(matrixView.getLayers());
        wiz.setSaveFilePageEnabled(false);

        WizardDialog dlg = new WizardDialog(Application.get(), wiz);

        dlg.open();

        if (dlg.isCancelled()) {
            return;
        }

        final OverlappingAnalysis analysis = wiz.getAnalysis();

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

        analysis.setSourceData(new ResourceReference<IMatrix>("source-data", matrixView));

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    new OverlappingProcessor(analysis).run(monitor);

                    if (monitor.isCancelled()) {
                        return;
                    }

                    final OverlappingAnalysisEditor editor = new OverlappingAnalysisEditor(analysis);

                    String ext = FilenameUtils.getExtension(getSelectedEditor().getName());

                    String analysisTitle = analysis.getTitle();

                    if (!analysisTitle.equals("")) {
                        editor.setName(analysis.getTitle() + "." + OverlappingAnalysisFormat.EXTENSION);
                    } else {
                        editor.setName(getEditorsPanel().deriveName(getSelectedEditor().getName(), ext, "", OverlappingAnalysisFormat.EXTENSION));
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
