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

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.filter.MatrixViewLabelFilter;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.dialog.filter.LabelFilterDialog;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

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

		final LabelFilterDialog dlg =
				new LabelFilterDialog(AppFrame.instance());

		boolean origIds = dlg.isUseOriginalIds();

		final IMatrixView matrixView = origIds ?
			ActionUtils.getMatrixView() :
			ActionUtils.getHeatmapMatrixView();

		if (matrixView == null)
			return;
		
		dlg.setVisible(true);

		if (dlg.getReturnStatus() != LabelFilterDialog.RET_OK) {
			AppFrame.instance().setStatusText("Filter cancelled.");
			return;
		}

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				monitor.begin("Filtering ...", 1);

				MatrixViewLabelFilter.filter(matrixView,
						dlg.getValues(),
						dlg.isUseRegexChecked(),
						dlg.isApplyToRowsChecked(),
						dlg.isApplyToColumnsChecked());

				monitor.end();
			}
		});

		AppFrame.instance().setStatusText("Filter by label done.");
	}
}
