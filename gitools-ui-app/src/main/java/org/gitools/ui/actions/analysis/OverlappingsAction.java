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

import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.analysis.overlapping.OverlappingProcessor;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence._DEPRECATED.PersistenceUtils;
import org.gitools.persistence.formats.analysis.OverlappingAnalysisFormat;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.analysis.overlapping.OverlappingAnalysisEditor;
import org.gitools.ui.analysis.overlapping.wizard.OverlappingAnalysisWizard;
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

public class OverlappingsAction extends BaseAction
{

    public OverlappingsAction()
    {
        super("Overlapping");
        setDesc("Overlapping analysis");
    }

    @Override
    public boolean isEnabledByModel(Object model)
    {
        return model instanceof Heatmap
                || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        final EditorsPanel editorPanel = AppFrame.get().getEditorsPanel();

        final IEditor currentEditor = editorPanel.getSelectedEditor();

        IMatrixView matrixView = ActionUtils.getMatrixView();

        if (matrixView == null)
        {
            return;
        }

        final OverlappingAnalysisWizard wiz = new OverlappingAnalysisWizard();
        wiz.setExamplePageEnabled(false);
        wiz.setDataFromMemory(true);
        wiz.setAttributes(matrixView.getCellAttributes());
        wiz.setSaveFilePageEnabled(false);

        WizardDialog dlg = new WizardDialog(AppFrame.get(), wiz);

        dlg.open();

        if (dlg.isCancelled())
        {
            return;
        }

        final OverlappingAnalysis analysis = wiz.getAnalysis();

        if (!analysis.isTransposeData())
        {
            if (matrixView.getSelectedColumns().length > 0)
            {
                MatrixView mv = new MatrixView(matrixView);
                mv.visibleColumnsFromSelection();
                matrixView = mv;
            }
        }
        else
        {
            if (matrixView.getSelectedRows().length > 0)
            {
                MatrixView mv = new MatrixView(matrixView);
                mv.visibleRowsFromSelection();
                matrixView = mv;
            }
        }

        analysis.setSourceData(new ResourceReference<IMatrix>("source-data", matrixView));

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(@NotNull IProgressMonitor monitor)
            {
                try
                {
                    new OverlappingProcessor(analysis).run(monitor);

                    if (monitor.isCancelled())
                    {
                        return;
                    }

                    final OverlappingAnalysisEditor editor = new OverlappingAnalysisEditor(analysis);

                    String ext = PersistenceUtils.getExtension(currentEditor.getName());

                    String analysisTitle = analysis.getTitle();

                    if (!analysisTitle.equals(""))
                    {
                        editor.setName(analysis.getTitle() + "." + OverlappingAnalysisFormat.EXTENSION);
                    }
                    else
                    {
                        editor.setName(editorPanel.deriveName(currentEditor.getName(), ext, "", OverlappingAnalysisFormat.EXTENSION));
                    }

                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            AppFrame.get().getEditorsPanel().addEditor(editor);
                            AppFrame.get().refresh();
                        }
                    });

                    monitor.end();

                    AppFrame.get().setStatusText("Done.");
                } catch (Throwable ex)
                {
                    monitor.exception(ex);
                }
            }
        });
    }
}
