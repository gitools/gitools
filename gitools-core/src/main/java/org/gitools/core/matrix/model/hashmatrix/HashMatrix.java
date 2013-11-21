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
package org.gitools.core.matrix.model.hashmatrix;

import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.core.matrix.model.AbstractMatrix;
import org.gitools.core.matrix.model.MatrixLayer;
import org.gitools.core.matrix.model.MatrixLayers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HashMatrix extends AbstractMatrix<MatrixLayers, HashMatrixDimension> {

    private Map<String, Map> values;

    public HashMatrix(MatrixDimensionKey... dimensions) {
        this(new MatrixLayers(), createHashMatrixDimensions(dimensions));
    }

    public HashMatrix(MatrixLayers<? extends IMatrixLayer> layers, HashMatrixDimension... dimensions) {
        super(layers, dimensions);

        this.values = new ConcurrentHashMap<>();
        for (IMatrixLayer layer : layers) {
            this.values.put(layer.getId(), new ConcurrentHashMap());
        }
    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, String... identifiers) {

        Map result = values.get(layer.getId());
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

    }

    public void set(String layerId, Object value, String... identifiers) {

        // Check that the layers exists
        IMatrixLayer<Object> layer = getLayers().get(layerId);
        if (layer == null) {
            layer = new MatrixLayer(layerId, value.getClass());
            getLayers().add(layer);
            values.put(layerId, new ConcurrentHashMap());
        }

        set(layer, value, identifiers);
    }

    private static HashMatrixDimension[] createHashMatrixDimensions(MatrixDimensionKey[] identifiers) {
        HashMatrixDimension[] dimensions = new HashMatrixDimension[identifiers.length];

        for (int i = 0; i < identifiers.length; i++) {
            dimensions[i] = new HashMatrixDimension(identifiers[i]);
        }

        return dimensions;
    }


}
