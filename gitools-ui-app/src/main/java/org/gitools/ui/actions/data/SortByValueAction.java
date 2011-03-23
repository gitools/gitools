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
import java.util.ArrayList;
import java.util.List;
import org.gitools.aggregation.IAggregator;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import org.gitools.aggregation.AggregatorFactory;
import org.gitools.aggregation.MultAggregator;
import org.gitools.matrix.sort.ValueSortCriteria;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.matrix.sort.MatrixViewSorter;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.sort.ValueSortDialog;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

public class SortByValueAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;
	
	public SortByValueAction() {
		super("Sort by value ...");

		setDesc("Sort by value ...");
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

		// Aggregators

		int aggrIndex = -1;
		IAggregator[] aggregators = AggregatorFactory.getAggregatorsArray();
		for (int i = 0; i < aggregators.length && aggrIndex == -1; i++)
			if (aggregators[i].getClass().equals(MultAggregator.class))
				aggrIndex = i;

		// Attributes

		int attrIndex = -1;

		List<IElementAttribute> cellProps = matrixView.getCellAdapter().getProperties();
		String[] attributeNames = new String[cellProps.size()];
		for (int i = 0; i < cellProps.size(); i++) {
			String name = cellProps.get(i).getName();
			attributeNames[i] = name;
			if (attrIndex == -1 && name.contains("p-value"))
				attrIndex = i;
		}

		if (attrIndex == -1)
			attrIndex = 0;

		// Default criteria

		List<ValueSortCriteria> initialCriteria = new ArrayList<ValueSortCriteria>(1);
		if (attributeNames.length > 0) {
			initialCriteria.add(new ValueSortCriteria(
					attributeNames[attrIndex], attrIndex,
					aggregators[aggrIndex],
					ValueSortCriteria.SortDirection.ASCENDING));
		}

		final ValueSortDialog dlg = new ValueSortDialog(
				AppFrame.instance(),
				attributeNames,
				aggregators,
				ValueSortCriteria.SortDirection.values(),
				initialCriteria);
		dlg.setVisible(true);

		if (dlg.isCancelled())
			return;

		final List<ValueSortCriteria> criteriaList = dlg.getCriteriaList();
		if (criteriaList.size() == 0) {
			AppFrame.instance().setStatusText("No criteria specified.");
			return;
		}

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				monitor.begin("Sorting ...", 1);

				ValueSortCriteria[] criteriaArray =
						new ValueSortCriteria[criteriaList.size()];

				MatrixViewSorter.sortByValue(matrixView,
						criteriaList.toArray(criteriaArray),
						dlg.isApplyToRowsChecked(),
						dlg.isApplyToColumnsChecked());

				monitor.end();
			}
		});

		AppFrame.instance().setStatusText("Sorted.");
	}
}
