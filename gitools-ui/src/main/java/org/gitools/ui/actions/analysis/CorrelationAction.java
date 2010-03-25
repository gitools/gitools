/*
 *  Copyright 2010 cperez.
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

package org.gitools.ui.actions.analysis;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.correlation.CorrelationProcessor;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.impl.LinearTwoSidedElementDecorator;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

public class CorrelationAction extends BaseAction {

	public CorrelationAction() {
		super("Correlation");
		setDesc("Correlation analysis");
	}

	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		IEditor currentEditor = editorPanel.getSelectedEditor();

		IMatrixView matrixView = ActionUtils.getMatrixView();

		if (matrixView == null)
			return;

		//FIXME depends on user selection: columns or rows
		if (matrixView.getSelectedColumns().length > 0) {
			MatrixView mv = new MatrixView(matrixView);
			mv.visibleColumnsFromSelection();
			matrixView = mv;
		}

		final CorrelationAnalysis analysis = new CorrelationAnalysis();
		analysis.setData(matrixView);

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					new CorrelationProcessor(analysis).run(monitor);
				}
				catch (AnalysisException ex) {
					monitor.exception(ex);
				}
			}
		});

		/*Heatmap heatmap = HeatmapUtil.createFromMatrixView(
				new MatrixView(analysis.getResults()));*/

		IMatrixView results = new MatrixView(analysis.getResults());
		Heatmap heatmap = new Heatmap(results);
		IElementAdapter cellAdapter = results.getCellAdapter();
		LinearTwoSidedElementDecorator dec = new LinearTwoSidedElementDecorator(cellAdapter);
		int valueIndex = cellAdapter.getPropertyIndex("Score");
		dec.setValueIndex(valueIndex != -1 ? valueIndex : 0);
		dec.setMinValue(-1);
		dec.setMaxValue(1);
		heatmap.setCellDecorator(dec);

		heatmap.setTitle(analysis.getTitle());

		HeatmapEditor editor = new HeatmapEditor(heatmap);

		String ext = PersistenceUtils.getExtension(currentEditor.getName());
		editor.setName(editorPanel.deriveName(
				currentEditor.getName(), ext,
				"-correlation", FileSuffixes.HEATMAP));

		editorPanel.addEditor(editor);

		AppFrame.instance().setStatusText("New heatmap created.");
	}
}
