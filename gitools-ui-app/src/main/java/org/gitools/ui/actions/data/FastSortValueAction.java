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
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.matrix.sort.MatrixViewSorter;
import org.gitools.core.matrix.sort.ValueSortCriteria;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.utils.HeaderEnum;
import org.gitools.utils.aggregation.IAggregator;
import org.gitools.utils.aggregation.MultAggregator;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FastSortValueAction extends BaseAction {

    private static final long serialVersionUID = -582380114189586206L;

    private ValueSortCriteria.SortDirection currentSort;
    private boolean applyToRows;

    public FastSortValueAction(HeaderEnum.Dimension dimension) {
        super("Sort");

        applyToRows = (dimension == HeaderEnum.Dimension.ROW);

        String title = "Sort " + (applyToRows ? "rows" : "columns");
        setName(title);
        setDesc(title);

        currentSort = ValueSortCriteria.SortDirection.ASCENDING;
        updateIcon();

        setMnemonic(KeyEvent.VK_S);
    }

    private void updateIcon() {

        if (currentSort == ValueSortCriteria.SortDirection.ASCENDING) {
            setSmallIconFromResource(IconNames.sortSelectedColumns16Desc);
            setLargeIconFromResource(IconNames.sortSelectedColumns24Desc);
        } else {
            setSmallIconFromResource(IconNames.sortSelectedColumns16Asc);
            setLargeIconFromResource(IconNames.sortSelectedColumns24Asc);
        }

    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final IMatrixView matrixView = ActionUtils.getMatrixView();


        // Deduce default Aggregator from the associated ColorScale
        IAggregator defaultAggregator;
        try {
            Heatmap heatmap = ActionUtils.getHeatmap();
            defaultAggregator = heatmap.getLayers().getTopLayer().getDecorator().getScale().defaultAggregator();
        } catch (Exception ex) {
            defaultAggregator = MultAggregator.INSTANCE;
        }
        final IAggregator aggregator = defaultAggregator;

        if (matrixView == null) {
            return;
        }

        final IMatrixLayer layer = matrixView.getLayers().getTopLayer();

        final ValueSortCriteria.SortDirection sort = currentSort;
        currentSort = (currentSort == ValueSortCriteria.SortDirection.ASCENDING ? ValueSortCriteria.SortDirection.DESCENDING : ValueSortCriteria.SortDirection.ASCENDING);
        updateIcon();

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(@NotNull IProgressMonitor monitor) {
                ValueSortCriteria[] criteriaArray = new ValueSortCriteria[]{new ValueSortCriteria(layer, aggregator, sort)};

                monitor.begin("Sorting ...", 1);

                MatrixViewSorter.sortByValue(matrixView, criteriaArray, applyToRows, !applyToRows, monitor);

                monitor.end();
            }
        });

        AppFrame.get().setStatusText("Rows sorted.");
    }
}
