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
package org.gitools.core.matrix.sort.mutualexclusion;


import org.gitools.core.utils.MatrixUtils;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.matrix.sort.ValueSortCriteria;
import org.gitools.utils.aggregation.SumAbsAggregator;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MutualExclusionComparator implements Comparator<Integer> {

    private final IMatrixView matrixView;
    private final int[] selectedColumns;
    private final double[] valueBuffer;
    private final Map<Integer, Double> aggregationCache;

    public MutualExclusionComparator(IMatrixView matrixView, int numRows, int[] selectedColumns, IProgressMonitor monitor) {
        this.matrixView = matrixView;
        this.selectedColumns = selectedColumns;
        this.valueBuffer = new double[selectedColumns.length];

        this.aggregationCache = new HashMap<Integer, Double>(numRows);

        monitor.begin("Aggregating values...", numRows);
        for (int i = 0; i < numRows; i++) {
            this.aggregationCache.put(i, aggregateValue(i));
            monitor.worked(1);
            if (monitor.isCancelled()) {
                return;
            }
        }
    }

    @Override
    public int compare(Integer idx1, Integer idx2) {
        Double value1 = aggregationCache.get(idx1);
        Double value2 = aggregationCache.get(idx2);

        int res;
        if (value1 == null) {
            res = -1;
        } else if (value2 == null) {
            res = 1;
        } else {
            res = (int) Math.signum(value1 - value2);
        }

        return res * ValueSortCriteria.SortDirection.DESCENDING.getFactor();
    }

    private double aggregateValue(int idx) {

        for (int i = 0; i < selectedColumns.length; i++) {
            int col = selectedColumns[i];

            Object valueObject = matrixView.getValue(idx, col, matrixView.getLayers().getTopLayerIndex());
            valueBuffer[i] = MatrixUtils.doubleValue(valueObject);
        }

        return SumAbsAggregator.INSTANCE.aggregate(valueBuffer);
    }
}
