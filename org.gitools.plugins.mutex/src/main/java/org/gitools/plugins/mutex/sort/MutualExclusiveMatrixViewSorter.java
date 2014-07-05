/*
 * #%L
 * org.gitools.mutex
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.plugins.mutex.sort;


import com.google.common.collect.Lists;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.SortDirection;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.matrix.model.iterable.IdentifiersPredicate;
import org.gitools.matrix.sort.AggregationFunction;
import org.gitools.utils.aggregation.NonNullCountAggregator;

import java.beans.PropertyChangeListener;
import java.util.Set;

public class MutualExclusiveMatrixViewSorter {


    public static void sortByMutualExclusion(final Heatmap heatmap, String pattern, Set<String> values, boolean regExChecked, boolean applyToColumns, IProgressMonitor monitor, boolean showProgress) {

        HeatmapLayer layer = heatmap.getLayers().getTopLayer();
        HeatmapDimension rows = heatmap.getRows();
        HeatmapDimension columns = heatmap.getColumns();

        if (applyToColumns) {
            rows = heatmap.getColumns();
            columns = heatmap.getRows();
        }

        AggregationFunction function = new AggregationFunction(layer, NonNullCountAggregator.INSTANCE, columns, layer.getEventFunction());

        rows.sort(new MutualExclusionComparator(
                heatmap,
                layer,
                rows,
                new IdentifiersPredicate<String>(rows, values, pattern, rows.getAnnotations()),
                function,
                monitor));

        monitor.begin("Sorting rows...", values.size());

        PropertyChangeListener[] listeners = columns.getPropertyChangeListeners();
        if (!showProgress) {
            // Remove listeners to avoid heatmap refresh at each iteration
            for (PropertyChangeListener listener : listeners) {
                columns.removePropertyChangeListener(listener);
            }
        }

        IMatrixPosition position = heatmap.newPosition();
        for (String row : Lists.reverse(Lists.newArrayList(rows))) {
            monitor.worked(1);

            if (monitor.isCancelled()) {
                break;
            }

            if (!values.contains(row)) {
                continue;
            }

            position.set(rows, row);
            columns.sort(new MutualExclusiveSingleValueComparator(position, layer, columns, SortDirection.DESCENDING,
                    layer.getEventFunction()));
        }

        if (!showProgress) {
            for (PropertyChangeListener listener : listeners) {
                columns.addPropertyChangeListener(listener);
            }

            // Force to fire the events
            columns.show(columns.toList());
        }

    }
}
