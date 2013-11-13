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
package org.gitools.core.matrix.sort;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.matrix.model.MatrixPosition;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.util.Comparator;
import java.util.concurrent.CancellationException;

public class SortByValueComparator implements Comparator<Integer> {

    private IProgressMonitor progressMonitor;
    private IMatrixDimension sortDimension;
    private IMatrixDimension aggregationDimension;
    private ValueSortCriteria[] criteriaArray;
    private IMatrixView matrixView;
    private int[] aggregatingIndices;
    private double[] valueBuffer;
    private MatrixPosition position;

    private LoadingCache<Long, Double> cache;

    public SortByValueComparator(IMatrixDimension sortDimension, ValueSortCriteria[] criteriaArray, IMatrixDimension aggregationDimension, int[] aggregatingIndices, IMatrixView matrixView, IProgressMonitor progressMonitor, int totalItemsToSort) {
        this.sortDimension = sortDimension;
        this.criteriaArray = criteriaArray;
        this.aggregationDimension = aggregationDimension;
        this.aggregatingIndices = aggregatingIndices;
        this.matrixView = matrixView;
        this.progressMonitor = progressMonitor;
        this.valueBuffer = new double[aggregatingIndices.length];
        this.position = new MatrixPosition();

        this.cache = CacheBuilder.newBuilder()
                .maximumSize(totalItemsToSort + 10)
                .build(
                        new CacheLoader<Long, Double>() {
                            public Double load(Long index) {
                                return aggregateValue(index);
                            }
                        });

        progressMonitor.begin("Sort by value...", totalItemsToSort);
    }

    @Override
    public int compare(Integer idx1, Integer idx2) {

        double aggr1 = 0.0;
        double aggr2 = 0.0;

        ValueSortCriteria criteria = null;
        int criteriaIndex = 0;

        while (criteriaIndex < criteriaArray.length && aggr1 == aggr2) {

            criteria = criteriaArray[criteriaIndex];

            aggr1 = getAggregatedValue(idx1, criteriaIndex);

            aggr2 = getAggregatedValue(idx2, criteriaIndex);

            criteriaIndex++;
        }

        if (Double.isNaN(aggr1)) {
            if (Double.isNaN(aggr2)) {
                return 0;
            } else {
                return 1;
            }
        }

        if (Double.isNaN(aggr2)) {
            return -1;
        }

        int res = (int) Math.signum(aggr1 - aggr2);
        return res * criteria.getDirection().getFactor();
    }

    private double getAggregatedValue(int index, int criteriaIndex) {
        long value = (((long)index) << 32) | (criteriaIndex & 0xffffffffL);
        return cache.getUnchecked(value);
    }

    private double aggregateValue(long value) {

        int index = (int)(value >> 32);
        int criteriaIndex = (int) value;

        progressMonitor.worked(1);

        if (progressMonitor.isCancelled()) {
            throw new CancellationException();
        }

        position.set(sortDimension, sortDimension.getLabel(index));
        ValueSortCriteria criteria = criteriaArray[criteriaIndex];

        position.set(matrixView.getLayers(), matrixView.getLayers().getLabel(criteria.getAttributeIndex()));

        for (int i = 0; i < aggregatingIndices.length; i++) {
            position.set(aggregationDimension, aggregationDimension.getLabel(aggregatingIndices[i]));
            valueBuffer[i] = MatrixUtils.doubleValue(matrixView.getValue(position));
        }

        return criteria.getAggregator().aggregate(valueBuffer);
    }

}
