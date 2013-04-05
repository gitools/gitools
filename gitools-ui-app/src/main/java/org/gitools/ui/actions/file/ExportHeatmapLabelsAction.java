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

import org.gitools.heatmap.Heatmap;
import org.gitools.label.AnnotationsPatternProvider;
import org.gitools.label.LabelProvider;
import org.gitools.label.MatrixColumnsLabelProvider;
import org.gitools.label.MatrixRowsLabelProvider;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.wizard.common.ExportHeatmapLabelsWizard;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @noinspection ALL
 */
public class ExportHeatmapLabelsAction extends BaseAction
{

    private static final long serialVersionUID = -7288045475037410310L;

    public ExportHeatmapLabelsAction()
    {
        super("Export labels ...");

        setDesc("Export row or column labels");
        setMnemonic(KeyEvent.VK_N);
    }

    @Override
    public boolean isEnabledByModel(Object model)
    {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        IEditor editor = AppFrame.get().getEditorsPanel().getSelectedEditor();

        Object model = editor != null ? editor.getModel() : null;
        if (model == null || !(model instanceof Heatmap))
        {
            return;
        }

        final Heatmap hm = (Heatmap) model;

        final ExportHeatmapLabelsWizard wiz = new ExportHeatmapLabelsWizard(hm);
        WizardDialog dlg = new WizardDialog(AppFrame.get(), wiz);
        dlg.setVisible(true);

        if (dlg.isCancelled())
        {
            return;
        }

        final IMatrixView matrixView = hm.getMatrixView();

        final File file = wiz.getSavePage().getPathAsFile();

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(@NotNull IProgressMonitor monitor)
            {
                try
                {
                    monitor.begin("Exporting labels ...", 1);
                    monitor.info("File: " + file.getName());

                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

                    LabelProvider labelProvider = null;
                    AnnotationMatrix annMatrix = null;

                    switch (wiz.getWhichLabels())
                    {
                        case VISIBLE_ROWS:
                            labelProvider = new MatrixRowsLabelProvider(matrixView);
                            annMatrix = hm.getRowDim().getAnnotations();
                            break;

                        case VISIBLE_COLUMNS:
                            labelProvider = new MatrixColumnsLabelProvider(matrixView);
                            annMatrix = hm.getColumnDim().getAnnotations();
                            break;

                        case HIDDEN_ROWS:
                            labelProvider = hiddenRowsLabelProvider(matrixView);
                            annMatrix = hm.getRowDim().getAnnotations();
                            break;

                        case HIDDEN_COLUMNS:
                            labelProvider = hiddenColumnsLabelProvider(matrixView);
                            annMatrix = hm.getColumnDim().getAnnotations();
                            break;
                    }

                    String pattern = wiz.getPattern();
                    if (!pattern.equalsIgnoreCase("${id}"))
                    {
                        labelProvider = new AnnotationsPatternProvider(labelProvider, annMatrix, pattern);
                    }

                    for (int i = 0; i < labelProvider.getCount(); i++)
                        pw.println(labelProvider.getLabel(i));

                    pw.close();

                    monitor.end();
                } catch (IOException ex)
                {
                    monitor.exception(ex);
                }
            }
        });

        AppFrame.get().setStatusText("Labels exported.");
    }

    @NotNull
    private LabelProvider hiddenRowsLabelProvider(@NotNull IMatrixView matrixView)
    {
        int[] visibleIndices = matrixView.getVisibleRows();
        Set<Integer> visibleSet = new HashSet<Integer>();
        for (int i = 0; i < visibleIndices.length; i++)
            visibleSet.add(visibleIndices[i]);

        IMatrix contents = matrixView.getContents();

        int j = 0;
        int count = contents.getRowCount();
        int[] hiddenIndices = new int[count - visibleIndices.length];
        for (int i = 0; i < count; i++)
            if (!visibleSet.contains(i))
            {
                hiddenIndices[j++] = i;
            }

        IMatrixView hiddenView = new MatrixView(matrixView);
        hiddenView.setVisibleRows(hiddenIndices);
        return new MatrixRowsLabelProvider(hiddenView);
    }

    @NotNull
    private LabelProvider hiddenColumnsLabelProvider(@NotNull IMatrixView matrixView)
    {
        int[] visibleIndices = matrixView.getVisibleColumns();
        Set<Integer> visibleSet = new HashSet<Integer>();
        for (int i = 0; i < visibleIndices.length; i++)
            visibleSet.add(visibleIndices[i]);

        IMatrix contents = matrixView.getContents();

        int j = 0;
        int count = contents.getColumnCount();
        int[] hiddenIndices = new int[count - visibleIndices.length];
        for (int i = 0; i < count; i++)
            if (!visibleSet.contains(i))
            {
                hiddenIndices[j++] = i;
            }

        IMatrixView hiddenView = new MatrixView(matrixView);
        hiddenView.setVisibleColumns(hiddenIndices);
        return new MatrixColumnsLabelProvider(hiddenView);
    }

	/*@Override
    public void actionPerformed(ActionEvent e) {

		final String visibleRows = "Visible row names";
		final String visibleCols = "Visible column names";
		final String hiddenRows = "Hidden row names";
		final String hiddenCols = "Hidden column names";

		final IMatrixView matrixView = ActionUtils.getHeatmapMatrixView();
		if (matrixView == null)
			return;
		
		final IMatrix contents = matrixView.getContents();
		
		String[] possibilities = { 
				visibleRows, visibleCols, hiddenRows, hiddenCols };

		final String selected = (String) JOptionPane.showInputDialog(AppFrame.instance(),
				"What do you want to export ?", "Export names",
				JOptionPane.QUESTION_MESSAGE, null, possibilities,
				"Visible row names");

		if (selected == null || selected.isEmpty())
			return;

		final File file = FileChooserUtils.selectFile(
					"Select destination file",
					Settings.getDefault().getLastExportPath(),
					FileChooserUtils.MODE_SAVE);

		if (file == null)
			return;

		Settings.getDefault().setLastExportPath(file.getParentFile().getAbsolutePath());

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					monitor.begin("Exporting " + selected.toLowerCase() + " ...", 1);
					monitor.info("File: " + file.getName());

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			
					if (visibleRows.equals(selected)) {
						for (int i = 0; i < matrixView.getRowCount(); i++)
							pw.println(matrixView.getRowLabel(i));
					}
					else if (visibleCols.equals(selected)) {
						for (int i = 0; i < matrixView.getColumnCount(); i++)
							pw.println(matrixView.getColumnLabel(i));
					}
					else if (hiddenRows.equals(selected)) {
						for (int i = 0; i < contents.getRowCount(); i++) {
							if (!inArray(i, matrixView.getVisibleRows()))
									pw.println(contents.getRowLabel(i));
						}
					}
					else if (hiddenCols.equals(selected)) {
						for (int i = 0; i < contents.getColumnCount(); i++) {
							if (!inArray(i, matrixView.getVisibleColumns()))
									pw.println(contents.getColumnLabel(i));
						}
					}

					pw.close();

					monitor.end();
				}
				catch (IOException ex) {
					monitor.exception(ex);
				}
			}
		});
		
		AppFrame.instance().setStatusText(selected + " exported.");
	}

	private boolean inArray(int needle, int[] ary) {
		for (int i = 0; i < ary.length; i++)
			if (needle == ary[i])
				return true;
		return false;
	}*/
}
