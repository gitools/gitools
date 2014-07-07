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
package org.gitools.matrix.sort;

import com.google.common.collect.Sets;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.matrix.model.MatrixLayers;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.matrix.model.iterable.PositionMapping;

import java.util.Comparator;
import java.util.Set;

import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class AggregationSortByValueComparator implements Comparator<String> {

    private MatrixLayers<IMatrixLayer<Double>> aggregationLayers;
    private IMatrix aggregationMatrix;
    private final IMatrixDimension sortDimension;
    private Set sortIdentifiers;
    private final int firstPosition;

    /**
     * @param matrix                 data matrix
     * @param sortDimension          Sorted dimension
     * @param sortIdentifiers        Identifiers to be sorted  (all if empty)
     * @param firstPosition          Indicate if only sorting selected items, otherwise -1
     * @param aggregationDimension   Aggregated dimension
     * @param aggregationIdentifiers Identifiers to get aggregation data from (all if empty)
     * @param progressMonitor        monitor
     * @param layers                 Data layers to aggregate and sort (order = relevance)
     */
    public AggregationSortByValueComparator(IMatrix matrix, IMatrixDimension sortDimension, Set<String> sortIdentifiers, int firstPosition, IMatrixDimension aggregationDimension, Set<String> aggregationIdentifiers, IProgressMonitor progressMonitor, Iterable<IMatrixLayer<Double>> layers) {
        this.sortDimension = sortDimension;

        this.sortIdentifiers = sortIdentifiers;
        this.firstPosition = firstPosition;

        aggregationLayers = new MatrixLayers<>(layers);
        aggregationMatrix = new HashMatrix(
                aggregationLayers,
                new HashMatrixDimension(ROWS, sortIdentifiers)
        );

        if (sortIdentifiers.isEmpty()) {
            sortIdentifiers = Sets.newHashSet(sortDimension);
        }

        if (aggregationIdentifiers.isEmpty()) {
            aggregationIdentifiers = Sets.newHashSet(aggregationDimension);
        }

        for (IMatrixLayer<Double> layer : layers) {

            matrix.newPosition()
                    .iterate(sortDimension.subset(sortIdentifiers))
                    .monitor(progressMonitor, "Aggregating values of layer '" + layer.getId() + "'")
                    .transform(new AggregationFunction(layer, aggregationDimension, aggregationIdentifiers))
                    .store(
                            aggregationMatrix,
                            new PositionMapping().map(sortDimension, ROWS),
                            aggregationLayers.get(layer.getId())
                    );

        }

    }

    @Override
    public int compare(String idx1, String idx2) {

        if (firstPosition > -1) {
            // 1. If idx1 or idx2 is not in aggregationMatrix compare position
            boolean before1 = sortDimension.indexOf(idx1) < firstPosition;
            boolean before2 = sortDimension.indexOf(idx2) < firstPosition;

            if (!sortIdentifiers.contains(idx1) && !sortIdentifiers.contains(idx2)) {
                if (before1 != before2) {
                    return before1 ? -1 : 1;
                } else {
                    return 0;
                }
            } else if (!sortIdentifiers.contains(idx1)) {
                return before1 ? -1 : 1;
            }
            else if (!sortIdentifiers.contains(idx2)) {
                return before2 ? 1 : -1;
            }
        }


        // 2. Compare all layers of aggregated items
        for (IMatrixLayer<Double> layer : aggregationLayers) {

            int compare = layer.getSortDirection().compare(
                    aggregationMatrix.get(layer, idx1),
                    aggregationMatrix.get(layer, idx2)
            );

            if (compare != 0) {
                return compare;
            }
        }

        // All the layer are equal
        return 0;
    }

}
