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

import edu.upf.bg.cutoffcmp.CutoffCmp;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.matrix.filter.ValueFilterCriteria;
import org.gitools.ui.platform.AppFrame;


import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.filter.MatrixViewValueFilter;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.dialog.filter.ValueFilterDialog;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

public class FilterByValueAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public FilterByValueAction() {
		super("Filter by values...");	
		setDesc("Filter by values");
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

		List<IElementAttribute> attributes = matrixView.getContents().getCellAdapter().getProperties();

		int pvalueIndex = -1;
		String[] attrNames = new String[attributes.size()];
		for (int i = 0; i < attributes.size(); i++) {
			attrNames[i] = attributes.get(i).getName();
			if (pvalueIndex == -1 && attrNames[i].contains("p-value"))
				pvalueIndex = i;
		}
		if (pvalueIndex == -1)
			pvalueIndex = 0;

		ArrayList<ValueFilterCriteria> initialCriteria = new ArrayList<ValueFilterCriteria>(1);
		initialCriteria.add(new ValueFilterCriteria(attrNames[pvalueIndex], pvalueIndex, CutoffCmp.LT, 0.05));
		
		final ValueFilterDialog dlg = new ValueFilterDialog(AppFrame.instance(),
				attrNames, CutoffCmp.comparators, initialCriteria);

		dlg.setVisible(true);

		if (dlg.getReturnStatus() != ValueFilterDialog.RET_OK) {
			AppFrame.instance().setStatusText("Filter cancelled.");
			return;
		}

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				monitor.begin("Filtering ...", 1);

				MatrixViewValueFilter.filter(matrixView,
						dlg.getCriteriaList(),
						dlg.isAllCriteriaChecked(),
						dlg.isAllElementsChecked(),
						dlg.isInvertCriteriaChecked(),
						dlg.isApplyToRowsChecked(),
						dlg.isApplyToColumnsChecked());
			}
		});

		AppFrame.instance().setStatusText("Filter applied.");
	}
}
