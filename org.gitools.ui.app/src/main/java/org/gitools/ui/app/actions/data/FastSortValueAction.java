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

import com.google.common.collect.Lists;
import org.gitools.api.analysis.IAggregator;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.SortDirection;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.MatrixViewSorter;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.actions.HeatmapDimensionAction;
import org.gitools.ui.app.heatmap.drawer.HeatmapPosition;
import org.gitools.ui.app.heatmap.popupmenus.dynamicactions.IHeatmapDimensionAction;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.utils.aggregation.MultAggregator;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class FastSortValueAction extends HeatmapDimensionAction implements IHeatmapDimensionAction {

    private SortDirection currentSort;
    private MatrixDimensionKey dimension;

    public FastSortValueAction(MatrixDimensionKey dimension) {
        super(dimension, "<html><i>Sort</i> " + dimension.getLabel() + " by values</html>");
        this.dimension = dimension;
        setMnemonic(KeyEvent.VK_S);
        currentSort = SortDirection.ASCENDING;
        updateIcon();

    }

    private void updateIcon() {

        if (currentSort == SortDirection.ASCENDING) {
            setSmallIconFromResource(IconNames.sortSelectedColumns16Desc);
            setLargeIconFromResource(IconNames.sortSelectedColumns24Desc);
        } else {
            setSmallIconFromResource(IconNames.sortSelectedColumns16Asc);
            setLargeIconFromResource(IconNames.sortSelectedColumns24Asc);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final Heatmap heatmap = getHeatmap();

        // Deduce default Aggregator from the associated ColorScale
        IAggregator defaultAggregator;
        try {
            defaultAggregator = heatmap.getLayers().getTopLayer().getDecorator().getScale().defaultAggregator();
        } catch (Exception ex) {
            defaultAggregator = MultAggregator.INSTANCE;
        }
        final IAggregator aggregator = defaultAggregator;
        final IMatrixLayer layer = heatmap.getLayers().getTopLayer();

        final SortDirection sort = currentSort;
        currentSort = (currentSort == SortDirection.ASCENDING ? SortDirection.DESCENDING : SortDirection.ASCENDING);
        updateIcon();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {

                layer.setAggregator(aggregator);
                layer.setSortDirection(sort);

                List<IMatrixLayer> criteriaArray = Lists.newArrayList(layer);

                monitor.begin("Sorting ...", 1);

                MatrixViewSorter.sortByValue(heatmap, criteriaArray, getDimension().getId() == ROWS, getDimension().getId() != ROWS, monitor);

                monitor.end();
            }
        });

        Application.get().setStatusText("Sorted.");
    }

    @Override
    public void onConfigure(HeatmapDimension object, HeatmapPosition position) {
        String selected1 = getHeatmap().getDimension(dimension).getSelected().size() > 0 ? "sel. " : "";
        String selected2 = object.getSelected().size() > 0 ? "selected " : "";

        setName("<html><i>Sort</i> " + selected1 + dimension.getLabel() + "s by " + selected2 + " values</html>");

    }
}
