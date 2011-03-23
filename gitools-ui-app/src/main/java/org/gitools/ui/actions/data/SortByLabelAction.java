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

package org.gitools.ui.actions.data;

import edu.upf.bg.progressmonitor.IProgressMonitor;

import java.awt.event.ActionEvent;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.sort.MatrixViewSorter;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.PageDialog;
import org.gitools.ui.sort.LabelSortPage;


public class SortByLabelAction extends BaseAction {

	public SortByLabelAction() {
		super("Sort by label ...");
		setDesc("Sort by label");
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

		final LabelSortPage page = new LabelSortPage(hm);
		PageDialog dlg = new PageDialog(AppFrame.instance(), page);
		dlg.setVisible(true);

		if (dlg.isCancelled())
			return;

		final IMatrixView matrixView = hm.getMatrixView();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {
				monitor.begin("Sorting ...", 1);

				MatrixViewSorter.sortByLabel(matrixView,
						page.isApplyToRowsSelected(),
						page.getRowsPattern(),
						hm.getRowDim().getAnnotations(),
						page.getRowsDirection(),
						page.isApplyToColumnsSelected(),
						page.getColumnsPattern(),
						hm.getColumnDim().getAnnotations(),
						page.getColumnsDirection());

				monitor.end();
			}
		});

		AppFrame.instance().setStatusText("Sort done.");
	}

}
