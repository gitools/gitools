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
package org.gitools.core.matrix.model;

import org.gitools.core.model.Resource;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMatrix<ML extends IMatrixLayers, MD extends IMatrixDimension> extends Resource implements IMatrix {

    private MatrixDimensionKey[] dimensions;
    private Map<MatrixDimensionKey, MD> identifiers;
    private ML layers;

    @Override
    public IMatrixPosition newPosition() {
        return new MatrixPosition(this);
    }

    public AbstractMatrix(ML layers, MD... identifiers) {

        this.layers = layers;
        this.dimensions = new MatrixDimensionKey[identifiers.length];
        this.identifiers = new HashMap<>(identifiers.length);

        for (int i=0; i < identifiers.length; i++) {
            MD identifier = identifiers[i];
            this.dimensions[i] = identifier.getId();
            this.identifiers.put( identifier.getId(), identifier);
        }
    }

    @Override
    public ML getLayers() {
        return layers;
    }

    @Override
    public MD getIdentifiers(MatrixDimensionKey dimension) {
        return identifiers.get(dimension);
    }

    @Override
    public MatrixDimensionKey[] getDimensions() {
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
    @Deprecated
    public MD getRows() {
        return getIdentifiers(dimensions[0]);
    }

    @Override
    @Deprecated
    public MD getColumns() {
        return getIdentifiers(dimensions[1]);
    }

    @Override
    @Deprecated
    public final Object getValue(int row, int column, int layerIndex) {
        return get(getLayer(layerIndex), getPosition(row, column));
    }

    @Override
    @Deprecated
    public final void setValue(int row, int column, int layer, Object value) {
        set(getLayer(layer), value, getPosition(row, column));
    }

    @Deprecated
    private IMatrixLayer<Object> getLayer(int layerIndex) {
        return (IMatrixLayer<Object>) getLayers().get(layerIndex);
    }

    @Deprecated
    private String[] getPosition(int row, int column) {

        MatrixDimensionKey rowsDimension = getDimensions()[0];
        String rowIdentifier = getIdentifiers(rowsDimension).getLabel(row);

        MatrixDimensionKey columnsDimensions = getDimensions()[1];
        String columnIdentifier = getIdentifiers(columnsDimensions).getLabel(column);

        return new String[] { rowIdentifier, columnIdentifier };
    }

    @Override
    public void detach() {
    }

}
