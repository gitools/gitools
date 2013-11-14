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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractMatrix<ML extends IMatrixLayers, MD extends IMatrixDimension> extends Resource implements IMatrix {

    private List<MatrixDimension> dimensions;
    private Map<MatrixDimension, MD> identifiers;
    private ML layers;

    public AbstractMatrix(ML layers, MD... identifiers) {

        this.layers = layers;
        this.dimensions = new ArrayList<>(identifiers.length);
        this.identifiers = new HashMap<>(identifiers.length);

        for (MD identifier : identifiers) {
            this.dimensions.add( identifier.getId() );
            this.identifiers.put( identifier.getId(), identifier);
        }
    }

    @Override
    public ML getLayers() {
        return layers;
    }

    @Override
    public MD getIdentifiers(MatrixDimension dimension) {
        return identifiers.get(dimension);
    }

    @Override
    public List<MatrixDimension> getDimensions() {
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
        return getIdentifiers(dimensions.get(0));
    }

    @Override
    @Deprecated
    public MD getColumns() {
        return getIdentifiers(dimensions.get(1));
    }

    @Override
    @Deprecated
    public final Object getValue(int row, int column, int layerIndex) {
        return get(getLayer(layerIndex), getPostion(row, column));
    }

    @Override
    @Deprecated
    public final void setValue(int row, int column, int layer, Object value) {
        set(getLayer(layer), value, getPostion(row, column));
    }

    @Deprecated
    private IMatrixLayer<Object> getLayer(int layerIndex) {
        return (IMatrixLayer<Object>) getLayers().get(layerIndex);
    }

    @Deprecated
    private MatrixPosition getPostion(int row, int column) {

        MatrixDimension rowsDimension = getDimensions().get(0);
        String rowIdentifier = getIdentifiers(rowsDimension).getLabel(row);

        MatrixDimension columnsDimensions = getDimensions().get(1);
        String columnIdentifier = getIdentifiers(columnsDimensions).getLabel(column);

        return new MatrixPosition(getDimensions().get(0), getDimensions().get(1)).set(rowIdentifier, columnIdentifier);
    }

    @Override
    public void detach() {
    }

}
