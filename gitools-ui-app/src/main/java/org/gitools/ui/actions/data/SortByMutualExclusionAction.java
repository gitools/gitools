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
package org.gitools.ui.actions.data;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.filter.MatrixViewLabelFilter.FilterDimension;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.sort.MatrixViewSorter;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.PageDialog;
import org.gitools.ui.sort.MutualExclusionSortPage;
import org.gitools.utils.aggregation.AggregatorFactory;
import org.gitools.utils.aggregation.IAggregator;
import org.gitools.utils.aggregation.MultAggregator;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

public class SortByMutualExclusionAction extends BaseAction
{

    private static final long serialVersionUID = -1582437709508438222L;

    public SortByMutualExclusionAction()
    {
        super("Sort by mutual exclusion ...");

        setDesc("Sort by mutual exclusion ...");
    }

    @Override
    public boolean isEnabledByModel(Object model)
    {
        return model instanceof Heatmap
                || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {


        IEditor editor = AppFrame.get()
                .getEditorsPanel()
                .getSelectedEditor();

        Object model = editor != null ? editor.getModel() : null;
        if (model == null || !(model instanceof Heatmap))
        {
            return;
        }
        final Heatmap hm = (Heatmap) model;


        final MutualExclusionSortPage page = new MutualExclusionSortPage(hm);
        PageDialog dlg = new PageDialog(AppFrame.get(), page);


        if (hm.getMatrixView().getSelectedColumns().length > 0)
        {
            page.setFilterDimension(FilterDimension.COLUMNS);
        }


        dlg.setVisible(true);

        if (dlg.isCancelled())
        {
            return;
        }

        // Aggregators

        int aggrIndex = -1;
        IAggregator[] aggregators = AggregatorFactory.getAggregatorsArray();
        for (int i = 0; i < aggregators.length && aggrIndex == -1; i++)
            if (aggregators[i].getClass().equals(MultAggregator.class))
            {
                aggrIndex = i;
            }


        final IMatrixView matrixView = hm.getMatrixView();

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(@NotNull IProgressMonitor monitor)
            {
                monitor.begin("Sorting ...", 1);

                AnnotationMatrix am = null;
                FilterDimension dim = page.getFilterDimension();
                switch (dim)
                {
                    case ROWS:
                        am = hm.getRowDim().getAnnotations();
                        break;
                    case COLUMNS:
                        am = hm.getColumnDim().getAnnotations();
                        break;
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

        AppFrame.get().setStatusText("Sorted.");
    }
}
