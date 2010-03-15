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

package org.gitools.ui.actions.data;

import edu.upf.bg.progressmonitor.IProgressMonitor;

import java.awt.event.ActionEvent;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.sort.MatrixViewSorter;
import org.gitools.matrix.sort.SortCriteria.SortDirection;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.dialog.sort.LabelSortDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;


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
		final IMatrixView matrixView = ActionUtils.getMatrixView();
		if (matrixView == null)
			return;

		final LabelSortDialog dlg = new LabelSortDialog(AppFrame.instance(), SortDirection.values());
		dlg.setVisible(true);

		if (dlg.isCancelled())
			return;

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				monitor.begin("Sorting ...", 1);

				MatrixViewSorter.sortByLabel(matrixView,
						dlg.getSortDirection(),
						dlg.isApplyToRowsChecked(),
						dlg.isApplyToColumnsChecked());

				monitor.end();
			}
		});

		AppFrame.instance().setStatusText("Sorted.");
	}

}
