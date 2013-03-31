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

import org.gitools.utils.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import org.gitools.matrix.filter.MatrixViewLabelFilter.FilterDimension;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.filter.MatrixViewLabelFilter;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.dialog.filter.LabelFilterPage;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.PageDialog;

public class FilterByLabelAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public FilterByLabelAction() {
		super("Filter by label...");
		setDesc("Filter by label");
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

		final LabelFilterPage page = new LabelFilterPage(hm);
		PageDialog dlg = new PageDialog(AppFrame.instance(), page);
		dlg.setVisible(true);

		if (dlg.isCancelled())
			return;

		final IMatrixView matrixView = hm.getMatrixView();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				monitor.begin("Filtering ...", 1);

				AnnotationMatrix am = null;
				FilterDimension dim = page.getFilterDimension();
				switch (dim) {
					case ROWS: am = hm.getRowDim().getAnnotations(); break;
					case COLUMNS: am = hm.getColumnDim().getAnnotations(); break;
				}

				MatrixViewLabelFilter.filter(matrixView, dim,
						page.getPattern(), am,
						page.getValues(),
						page.isUseRegexChecked());

				monitor.end();
			}
		});

		AppFrame.instance().setStatusText("Filter by label done.");
	}
}
