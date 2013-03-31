/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.actions.file;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.gitools.heatmap.Heatmap;
import org.gitools.label.AnnotationsPatternProvider;
import org.gitools.label.LabelProvider;
import org.gitools.label.MatrixColumnsLabelProvider;
import org.gitools.label.MatrixRowsLabelProvider;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.wizard.common.ExportHeatmapLabelsWizard;

public class ExportHeatmapLabelsAction extends BaseAction {

	private static final long serialVersionUID = -7288045475037410310L;

	public ExportHeatmapLabelsAction() {
		super("Export labels ...");
		
		setDesc("Export row or column labels");
		setMnemonic(KeyEvent.VK_N);
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		IEditor editor = AppFrame.instance()
			.getEditorsPanel()
			.getSelectedEditor();

		Object model = editor != null ? editor.getModel() : null;
		if (model == null || !(model instanceof Heatmap))
			return;

		final Heatmap hm = (Heatmap) model;

		final ExportHeatmapLabelsWizard wiz = new ExportHeatmapLabelsWizard(hm);
		WizardDialog dlg = new WizardDialog(AppFrame.instance(), wiz);
		dlg.setVisible(true);

		if (dlg.isCancelled())
			return;

		final IMatrixView matrixView = hm.getMatrixView();

		final File file = wiz.getSavePage().getPathAsFile();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					monitor.begin("Exporting labels ...", 1);
					monitor.info("File: " + file.getName());

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

					LabelProvider labelProvider = null;
					AnnotationMatrix annMatrix = null;

					switch (wiz.getWhichLabels()) {
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
						labelProvider = new AnnotationsPatternProvider(
								labelProvider, annMatrix, pattern);

					for (int i = 0; i < labelProvider.getCount(); i++)
						pw.println(labelProvider.getLabel(i));

					pw.close();

					monitor.end();
				}
				catch (IOException ex) {
					monitor.exception(ex);
				}
			}
		});

		AppFrame.instance().setStatusText("Labels exported.");
	}

	private LabelProvider hiddenRowsLabelProvider(IMatrixView matrixView) {
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
				hiddenIndices[j++] = i;

		IMatrixView hiddenView = new MatrixView(matrixView);
		hiddenView.setVisibleRows(hiddenIndices);
		return new MatrixRowsLabelProvider(hiddenView);
	}

	private LabelProvider hiddenColumnsLabelProvider(IMatrixView matrixView) {
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
				hiddenIndices[j++] = i;

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
