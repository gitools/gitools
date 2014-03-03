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
package org.gitools.matrix.model;

import com.google.common.base.Joiner;
import org.gitools.api.matrix.*;
import org.gitools.matrix.model.hashmatrix.TriangularHashMatrix;
import org.gitools.matrix.model.iterable.IdentifierSourceIterable;
import org.gitools.matrix.model.iterable.ValueSourceIterable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MatrixPosition implements IMatrixPosition {

    private IMatrix matrix;

    private MatrixDimensionKey[] dimensions;
    private String[] identifiers;
    private Map<MatrixDimensionKey, Integer> positions;
    private boolean diagonal = false;

    public MatrixPosition(IMatrix matrix) {
        super();

        this.matrix = matrix;
        this.dimensions = matrix.getDimensionKeys();

        if (matrix instanceof TriangularHashMatrix) {
            this.diagonal = true;
        }

        this.identifiers = new String[dimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            this.identifiers[i] = matrix.getDimension(dimensions[i]).iterator().next();
        }

        this.positions = new HashMap<>(dimensions.length);

        for (int i = 0; i < dimensions.length; i++) {
            this.positions.put(dimensions[i], i);
        }
    }

    @Override
    public IMatrix getMatrix() {
        return matrix;
    }

    @Override
    public String get(IMatrixDimension dimension) {
        return get(dimension.getId());
    }

    @Override
    public String get(MatrixDimensionKey dimension) {
        return identifiers[positions.get(dimension)];
    }

    @Override
    public IMatrixPosition set(IMatrixDimension dimension, String identifier) {
        return set(dimension.getId(), identifier);
    }

    @Override
    public MatrixPosition set(MatrixDimensionKey dimension, String identifier) {

        if (identifier != null) {
            identifiers[positions.get(dimension)] = identifier;
        }

        return this;
    }

    @Override
    public String[] toVector() {
        if (diagonal) {
            return doInvert(identifiers);
        } else {
            return identifiers;
        }
    }

    private String[] doInvert(String[] identifiers) {
        if (matrix.getDimension(MatrixDimensionKey.COLUMNS).indexOf(identifiers[0]) >=
                matrix.getDimension(MatrixDimensionKey.ROWS).indexOf(identifiers[1])) {
            return new String[]{identifiers[1], identifiers[0]};
        } else {
            return identifiers;
        }
    }

    public MatrixPosition set(String... identifiers) {
        assert identifiers.length == dimensions.length : "This matrix position has " + dimensions.length + " and your identifier vector " + identifiers.length;
        this.identifiers = identifiers;
        return this;
    }

    @Override
    public IMatrixIterable<String> iterate(IMatrixDimension dimension) {
        return iterate(dimension.getId());
    }

    @Override
    public IMatrixIterable<String> iterate(IMatrixDimension dimension, Set<String> identifiers) {
        return iterate(dimension.getId(), identifiers);
    }

    @Override
    public IMatrixIterable<String> iterate(MatrixDimensionKey dimensionKey) {
        return new IdentifierSourceIterable(this, dimensionKey);
    }

    @Override
    public IMatrixIterable<String> iterate(MatrixDimensionKey dimensionKey, Set<String> identifiers) {
        return new IdentifierSourceIterable(this, dimensionKey, identifiers);
    }

    @Override
    public <T> IMatrixIterable<T> iterate(ILayerAdapter<T> layerAdapter, IMatrixDimension dimension) {
        return new AdapterSourceIterable<>(this, dimension.getId(), layerAdapter);
    }

    @Override
    public <T> IMatrixIterable<T> iterate(ILayerAdapter<T> layerAdapter, IMatrixDimension dimension, Set<String> identifiers) {
        return new AdapterSourceIterable<>(this, dimension.getId(), layerAdapter, identifiers);
    }

    @Override
    public <T> IMatrixIterable<T> iterate(IMatrixLayer<T> layer, IMatrixDimension dimension) {
        return new ValueSourceIterable<>(this, dimension.getId(), layer);
    }

    public <T> IMatrixIterable<T> iterate(IMatrixLayer<T> layer, IMatrixDimension dimension, Set<String> identifiers) {
        return new ValueSourceIterable<>(this, dimension.getId(), layer, identifiers);
    }

    @Override
    public String toString() {
        return " (" + Joiner.on(",").join(identifiers) + ")";
    }
}
