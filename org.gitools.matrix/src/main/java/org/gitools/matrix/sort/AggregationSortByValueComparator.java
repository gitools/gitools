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
import org.gitools.matrix.sort.mutualexclusion.AggregationFunction;

import java.util.Comparator;
import java.util.Set;

import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class AggregationSortByValueComparator implements Comparator<String> {

    private MatrixLayers<IMatrixLayer<Double>> aggregationLayers;
    private IMatrix aggregationMatrix;

    public AggregationSortByValueComparator(IMatrix matrix, IMatrixDimension sortDimension, Set<String> sortIdentifiers, IMatrixDimension aggregationDimension, Set<String> aggregationIdentifiers, IProgressMonitor progressMonitor, Iterable<IMatrixLayer<Double>> layers) {

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
                    .iterate(sortDimension, sortIdentifiers)
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
