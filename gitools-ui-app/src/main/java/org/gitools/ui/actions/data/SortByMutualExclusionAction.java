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
import org.gitools.aggregation.IAggregator;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import org.gitools.aggregation.AggregatorFactory;
import org.gitools.aggregation.MultAggregator;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.filter.MatrixViewLabelFilter.FilterDimension;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.sort.MatrixViewSorter;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.sort.MutualExclusionSortPage;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.PageDialog;

public class SortByMutualExclusionAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;
	
	public SortByMutualExclusionAction() {
		super("Sort by mutual exclusion ...");

		setDesc("Sort by mutual exclusion ...");
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


		final MutualExclusionSortPage page = new MutualExclusionSortPage(hm);
		PageDialog dlg = new PageDialog(AppFrame.instance(), page);



		//Propose selected rows or columns
		/*ArrayList<String> selected = new ArrayList<String>();
		if (hm.getMatrixView().getSelectedRows().length > 0) {
			LabelProvider labelProvider = new MatrixRowsLabelProvider(hm.getMatrixView());
			int[] selectedIndices = hm.getMatrixView().getSelectedRows();
			for (int i=0; i < selectedIndices.length; i++)
				selected.add(labelProvider.getLabel(selectedIndices[i]));
		}
		else if(hm.getMatrixView().getSelectedColumns().length > 0) {
			LabelProvider labelProvider = new MatrixColumnsLabelProvider(hm.getMatrixView());
			page.setFilterDimension(FilterDimension.COLUMNS);
			int[] selectedIndices = hm.getMatrixView().getSelectedColumns();
			for (int i=0; i < selectedIndices.length; i++)
				selected.add(labelProvider.getLabel(selectedIndices[i]));
		}
		if(!selected.isEmpty()) {
			page.setValues(selected);
		}*/
        if(hm.getMatrixView().getSelectedColumns().length > 0)
            page.setFilterDimension(FilterDimension.COLUMNS);
            

		dlg.setVisible(true);
		
		if (dlg.isCancelled())
			return;

		// Aggregators

		int aggrIndex = -1;
		IAggregator[] aggregators = AggregatorFactory.getAggregatorsArray();
		for (int i = 0; i < aggregators.length && aggrIndex == -1; i++)
			if (aggregators[i].getClass().equals(MultAggregator.class))
				aggrIndex = i;


		final IMatrixView matrixView = hm.getMatrixView();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				monitor.begin("Sorting ...", 1);

				AnnotationMatrix am = null;
				FilterDimension dim = page.getFilterDimension();
				switch (dim) {
					case ROWS: am = hm.getRowDim().getAnnotations(); break;
					case COLUMNS: am = hm.getColumnDim().getAnnotations(); break;
				}

				MatrixViewSorter.sortByMutualExclusion(
						matrixView,
						page.getPattern(),
						am,
						page.getValues(),
						page.isUseRegexChecked(),
						dim.equals(FilterDimension.ROWS),
						dim.equals(FilterDimension.COLUMNS));

				monitor.end();
			}
		});


/*
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
		});*/

		AppFrame.instance().setStatusText("Sorted.");
	}
}
