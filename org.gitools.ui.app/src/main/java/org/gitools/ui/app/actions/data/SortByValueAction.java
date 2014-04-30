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
package org.gitools.ui.app.actions.data;

import org.gitools.api.analysis.IAggregator;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.SortDirection;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.heatmap.MatrixViewSorter;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.app.sort.ValueSortDialog;
import org.gitools.ui.core.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.utils.aggregation.AggregatorFactory;
import org.gitools.utils.aggregation.MultAggregator;

import java.awt.event.ActionEvent;
import java.util.List;

public class SortByValueAction extends HeatmapAction {

    private static final long serialVersionUID = -1582437709508438222L;
    private MatrixDimensionKey dimension;

    public SortByValueAction(MatrixDimensionKey dimension) {
        super("<html><i>Sort</i> by values");
        this.dimension = dimension;

        setDesc("Sort by heatmap values ...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final IMatrixView matrixView = getHeatmap();

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
                Application.get(),
                layers,
                dimension,
                aggregators,
                SortDirection.values(),
                getHeatmap().getLayers().getTopLayer()
        );

        dlg.setVisible(true);

        if (dlg.isCancelled()) {
            return;
        }

        final List<IMatrixLayer> criteriaList = dlg.getCriteriaList();
        if (criteriaList.size() == 0) {
            Application.get().setStatusText("No criteria specified.");
            return;
        }

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Sorting ...", 1);
                MatrixViewSorter.sortByValue(matrixView, criteriaList, dlg.isApplyToRowsChecked(), dlg.isApplyToColumnsChecked(), monitor);
            }
        });

        Application.get().setStatusText("Sorted.");
    }
}
