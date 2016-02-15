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
package org.gitools.matrix.model.hashmatrix;

import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.matrix.model.AbstractMatrix;
import org.gitools.matrix.model.MatrixLayers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HashMatrix extends AbstractMatrix<MatrixLayers<IMatrixLayer>, HashMatrixDimension> {

    private Map<String, Map> values;

    public HashMatrix(MatrixLayers layers, MatrixDimensionKey... dimensions) {
        this(layers, createHashMatrixDimensions(dimensions));
    }

    public HashMatrix(MatrixLayers<? extends IMatrixLayer> layers, IMatrixDimension... dimensions) {
        this(layers, createHashMatrixDimensions(dimensions));
    }

    public HashMatrix(MatrixLayers<? extends IMatrixLayer> layers, HashMatrixDimension... dimensions) {
        super((MatrixLayers<IMatrixLayer>) layers, dimensions);

        this.values = new ConcurrentHashMap<>();
        for (IMatrixLayer layer : layers) {
            this.values.put(layer.getId(), new ConcurrentHashMap());
        }
    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, String... identifiers) {

        Map result;
        try {
            result = values.get(layer.getId());
        } catch (NullPointerException e) {
            return null;
        }

        if (result == null) {
            return null;
        }

        String identifier = identifiers[0];
        for (int i = 1; i < identifiers.length; i++) {

            if (identifier == null || !result.containsKey(identifier)) {
                return null;
            }

            result = (Map) result.get(identifier);
            identifier = identifiers[i];
        }

        if (identifier == null || !result.containsKey(identifier)) {
            return null;
        }

        return (T) result.get(identifier);
    }

    public <T> void set(IMatrixLayer<T> layer, T value, String... identifiers) {
        assert identifiers.length == getDimensionKeys().length : "Matrix of dimension " + getDimensionKeys().length + " and position of " + identifiers.length + " elements";

        if (value == null) {
            return;
        }

        Map result = values.get(layer.getId());
        if (result == null) {
            throw new UnsupportedOperationException("Layer '" + layer.getId() + "' not found");
        }

        String identifier = identifiers[0];
        HashMatrixDimension dimension = getDimension(getDimensionKeys()[0]);

        for (int i = 1; i < identifiers.length; i++) {

            if (!result.containsKey(identifier)) {
                result.put(identifier, new ConcurrentHashMap());
                dimension.add(identifier);
            }

            result = (Map) result.get(identifier);

            identifier = identifiers[i];
            dimension = getDimension(getDimensionKeys()[i]);
        }

        if (!result.containsKey(identifier)) {
            dimension.add(identifier);
        }

        result.put(identifier, value);
        hasChanged = true;

    }

    /**
     *  You must call this method after load all values into the matrix.
     *  Some implementations must prepare the matrix to be read and improve
     *  the read performance. Also the @see isChanged() will be true only
     *  if there is any change after this method has been call.
     */
    public void init() {
        this.hasChanged = false;
    }

    public void addLayer(IMatrixLayer layer) {
        getLayers().add(layer);
        values.put(layer.getId(), new ConcurrentHashMap());
    }

    public void removeLayer(IMatrixLayer layer) {
        getLayers().remove(layer);
        values.remove(layer.getId());
        hasChanged = true;
    }

    public void copyLayerValues(String layerId, HashMatrix fromMatrix) {

        if (getLayers().get(layerId) == null) {
            throw new UnsupportedOperationException("Unknown '" + layerId + "' layer in this matrix");
        }

        // Copy values map
        Map values = fromMatrix.values.get(layerId);
        this.values.put(layerId, values);

        // Update dimension identifiers
        for (MatrixDimensionKey dimensionKey : getDimensionKeys()) {
            HashMatrixDimension thisDimension = getDimension(dimensionKey);
            HashMatrixDimension fromDimension = fromMatrix.getDimension(dimensionKey);

            if (fromDimension == null) {
                throw new UnsupportedOperationException("Impossible to copy a matrix with different dimensions.");
            }

            for (String identifier : fromDimension) {
                thisDimension.add(identifier);
            }
        }

        this.hasChanged = true;
    }

    private static HashMatrixDimension[] createHashMatrixDimensions(MatrixDimensionKey[] identifiers) {
        HashMatrixDimension[] dimensions = new HashMatrixDimension[identifiers.length];

        for (int i = 0; i < identifiers.length; i++) {
            dimensions[i] = new HashMatrixDimension(identifiers[i]);
        }

        return dimensions;
    }

    private static HashMatrixDimension[] createHashMatrixDimensions(IMatrixDimension[] dimensions) {
        HashMatrixDimension[] hashDimensions = new HashMatrixDimension[dimensions.length];

        for (int i = 0; i < dimensions.length; i++) {
            IMatrixDimension dimension = dimensions[i];

            if (dimension instanceof HashMatrixDimension) {
                hashDimensions[i] = (HashMatrixDimension) dimension;
            } else {
                hashDimensions[i] = new HashMatrixDimension(dimensions[i].getId(), dimensions[i]);
            }
        }

        return hashDimensions;
    }

    @Override
    public IMatrix subset(IMatrixDimension... dimensionSubsets) {

        Map<MatrixDimensionKey, HashMatrixDimension> allDimensions = new HashMap<>(getDimensionKeys().length);

        // Load all dimensions
        for (MatrixDimensionKey key : getDimensionKeys()) {
            allDimensions.put(key, getDimension(key));
        }

        // Override subset dimensions
        for (IMatrixDimension dimension : dimensionSubsets) {

            if (dimension instanceof HashMatrixDimension) {
                allDimensions.put(dimension.getId(), (HashMatrixDimension) dimension);
            } else {
                allDimensions.put(dimension.getId(), new HashMatrixDimension(dimension.getId(), dimension));
            }
        }

        return new SubMatrix(getLayers(), allDimensions.values());
    }

    private class SubMatrix extends AbstractMatrix<MatrixLayers<IMatrixLayer>, HashMatrixDimension> {

        public SubMatrix(MatrixLayers layers, Collection<HashMatrixDimension> identifiers) {
            super(layers, (HashMatrixDimension[]) identifiers.toArray());
        }

        @Override
        public <T> T get(IMatrixLayer<T> layer, String... identifiers) {
            return HashMatrix.this.get(layer, identifiers);
        }

        @Override
        public <T> void set(IMatrixLayer<T> layer, T value, String... identifiers) {
            throw new UnsupportedOperationException("The subset matrix are read only matrix");
        }

        @Override
        public boolean isChanged() {
            return false;
        }
    }
}
