/*
 * #%L
 * gitools-core
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
package org.gitools.heatmap;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.SortDirection;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.api.matrix.view.IMatrixViewDimension;
import org.gitools.matrix.filter.PatternFunction;
import org.gitools.matrix.sort.AggregationSortByValueComparator;
import org.gitools.matrix.sort.SortByLabelComparator;
import org.gitools.utils.progressmonitor.NullProgressMonitor;

import java.util.Set;

public abstract class MatrixViewSorter {

    public static void sortByValue(IMatrixView matrixView, Iterable<IMatrixLayer> layers, boolean applyToRows, boolean applyToColumns, IProgressMonitor progressMonitor) {

        IMatrixViewDimension rows = matrixView.getRows();
        IMatrixViewDimension columns = matrixView.getColumns();

        if (progressMonitor == null) {
            progressMonitor = new NullProgressMonitor();
        }

        if (applyToRows) {
            sortByValue(matrixView, rows, rows.getSelected(), columns, columns.getSelected(), layers, progressMonitor);
        }

        if (applyToColumns) {
            sortByValue(matrixView, columns, columns.getSelected(), rows, rows.getSelected(), layers, progressMonitor);
        }
    }

    private static void sortByValue(final IMatrixView matrixView,
                                    final IMatrixViewDimension sortDimension, Set<String> sortSelection,
                                    final IMatrixViewDimension aggregationDimension, Set<String> aggregationSelection,
                                    final Iterable<IMatrixLayer> criteriaArray,
                                    final IProgressMonitor progressMonitor
    ) {


        sortDimension.sort(new AggregationSortByValueComparator(
                matrixView,
                sortDimension,
                sortSelection,
                aggregationDimension,
                aggregationSelection,
                progressMonitor,
                (Iterable) criteriaArray)
        );

    }

    public static void sortByLabel(Heatmap heatmap, boolean sortRows, String rowsPattern, SortDirection rowsDirection, boolean rowsNumeric, boolean sortCols, String colsPattern, SortDirection colsDirection, boolean colsNumeric) {

        if (sortRows) {
            sortByLabel(heatmap.getRows(), rowsDirection, rowsPattern, rowsNumeric);
        }

        if (sortCols) {
            sortByLabel(heatmap.getColumns(), colsDirection, colsPattern, colsNumeric);
        }
    }

    private static void sortByLabel(HeatmapDimension sortDimension, SortDirection direction, String pattern, boolean asNumeric) {
        sortDimension.sort(new SortByLabelComparator(direction, new PatternFunction(pattern, sortDimension.getAnnotations()), asNumeric));
    }

}