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

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.matrix.model.IMatrixLayers;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.matrix.sort.MatrixViewSorter;
import org.gitools.core.matrix.sort.ValueSortCriteria;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.sort.ValueSortDialog;
import org.gitools.utils.aggregation.AggregatorFactory;
import org.gitools.utils.aggregation.IAggregator;
import org.gitools.utils.aggregation.MultAggregator;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.util.List;

public class SortByValueAction extends BaseAction {

    private static final long serialVersionUID = -1582437709508438222L;

    public SortByValueAction() {
        super("Sort by values ...");

        setDesc("Sort by heatmap values ...");
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final IMatrixView matrixView = ActionUtils.getMatrixView();
        if (matrixView == null) {
            return;
        }

        // Aggregators

        int aggrIndex = -1;
        IAggregator[] aggregators = AggregatorFactory.getAggregatorsArray();
        for (int i = 0; i < aggregators.length && aggrIndex == -1; i++)
            if (aggregators[i].getClass().equals(MultAggregator.class)) {
                aggrIndex = i;
            }

        // Default criteria
        IMatrixLayers<IMatrixLayer> layers = matrixView.getLayers();
        final ValueSortDialog dlg = new ValueSortDialog(
                AppFrame.get(),
                layers,
                aggregators,
                ValueSortCriteria.SortDirection.values(),
                new ValueSortCriteria(layers.iterator().next(), aggregators[aggrIndex], ValueSortCriteria.SortDirection.ASCENDING)
        );
        dlg.setVisible(true);

        if (dlg.isCancelled()) {
            return;
        }

        final List<ValueSortCriteria> criteriaList = dlg.getCriteriaList();
        if (criteriaList.size() == 0) {
            AppFrame.get().setStatusText("No criteria specified.");
            return;
        }

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(@NotNull IProgressMonitor monitor) {
                monitor.begin("Sorting ...", 1);

                ValueSortCriteria[] criteriaArray = new ValueSortCriteria[criteriaList.size()];

                MatrixViewSorter.sortByValue(matrixView, criteriaList.toArray(criteriaArray), dlg.isApplyToRowsChecked(), dlg.isApplyToColumnsChecked(), monitor);

                monitor.end();
            }
        });

        AppFrame.get().setStatusText("Sorted.");
    }
}
