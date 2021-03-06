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

import org.gitools.api.matrix.*;
import org.gitools.resource.Resource;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMatrix<ML extends IMatrixLayers<? extends IMatrixLayer>, MD extends IMatrixDimension> extends Resource implements IMatrix {

    private MatrixDimensionKey[] dimensions;
    private Map<MatrixDimensionKey, MD> identifiers;
    private ML layers;

    private transient Map<IKey, Object> cache;

    @Override
    public IMatrixPosition newPosition() {
        return new MatrixPosition(this);
    }

    @Override
    public IMatrix subset(IMatrixDimension... dimensionSubsets) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " don't support subsetting");
    }

    public AbstractMatrix(ML layers, MD... identifiers) {

        this.layers = layers;
        this.dimensions = new MatrixDimensionKey[identifiers.length];
        this.identifiers = new HashMap<>(identifiers.length);

        for (int i = 0; i < identifiers.length; i++) {
            MD identifier = identifiers[i];
            this.dimensions[i] = identifier.getId();
            this.identifiers.put(identifier.getId(), identifier);
        }
    }

    @Override
    public ML getLayers() {
        return layers;
    }

    public void setLayers(ML layers) {
        this.layers = layers;
    }

    @Override
    public MD getDimension(MatrixDimensionKey dimension) {
        return identifiers.get(dimension);
    }

    @Override
    public MatrixDimensionKey[] getDimensionKeys() {
        return dimensions;
    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, IMatrixPosition position) {
        return get(layer, position.toVector());
    }

    @Override
    public <T> void set(IMatrixLayer<T> layer, T value, IMatrixPosition position) {
        set(layer, value, position.toVector());
    }

    @Override
    public MD getRows() {
        return getDimension(dimensions[0]);
    }

    @Override
    public MD getColumns() {
        return getDimension(dimensions[1]);
    }

    @Override
    public void detach(IMatrixLayer topLayer) {
        this.cache = null;

        for (IMatrixLayer layer : layers) {
            if (topLayer == layer) {
                continue;
            }
            layer.detach();
        }
    }

    public <T> void setMetadata(IKey<T> key, T value) {
        this.getCacheMap().put(key, value);
    }

    public <T> T getMetadata(IKey<T> key) {
        return (T) this.getCacheMap().get(key);
    }

    private Map<IKey, Object> getCacheMap() {
        if (cache == null) {
            cache = new HashMap<>();
        }
        return cache;
    }


}
