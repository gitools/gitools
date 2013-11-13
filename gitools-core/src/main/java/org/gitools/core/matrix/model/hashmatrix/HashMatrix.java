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

import org.gitools.core.matrix.model.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HashMatrix extends AbstractMatrix {

    private HashMatrixDimension rows;
    private HashMatrixDimension columns;
    private MatrixLayers layers;

    private Map<String, Map<String, Map<String, Object>>> values;

    public HashMatrix() {
        this(new HashMatrixDimension("rows"), new HashMatrixDimension("columns"), new MatrixLayers());
    }

    public HashMatrix(Iterable<String> rows, Iterable<String> columns, MatrixLayers layers) {
        this(new HashMatrixDimension("rows", rows), new HashMatrixDimension("columns", columns), layers);
    }

    private HashMatrix(HashMatrixDimension rows, HashMatrixDimension columns, MatrixLayers<?> layers) {
        this.rows = rows;
        this.columns = columns;
        this.layers = layers;

        this.values = new ConcurrentHashMap<>();
        for (String layer : layers) {
            values.put(layer, new ConcurrentHashMap<String, Map<String, Object>>());
        }
    }

    @Override
    public HashMatrixDimension getRows() {
        return rows;
    }

    @Override
    public HashMatrixDimension getColumns() {
        return columns;
    }

    @Override
    public IMatrixLayers getLayers() {
        return layers;
    }

    @Override
    public Object getValue(IMatrixPosition position) {

        String layer = position.get(getLayers());
        String row = position.get(getRows());
        String column = position.get(getColumns());

        return getRow(layer, row).get(column);
    }

    @Override
    public void setValue(IMatrixPosition position, Object value) {

        String layer = position.get(getLayers());
        String row = position.get(getRows());
        String column = position.get(getColumns());

        getRow(layer, row).put(column, value);
    }

    public void setValue(String rowId, String columnId, String layerId, Object value) {

        if (value == null) {
            return;
        }

        // Check that the layers exists
        int layerIndex = layers.getIndex(layerId);
        if (layerIndex == -1) {
            layers.add(new MatrixLayer(layerId, value.getClass()));
            values.put(layerId, new ConcurrentHashMap<String, Map<String, Object>>());
        }

        // Check that the row exists
        if (rows.getIndex(rowId) == -1) {
            rows.addLabel(rowId);
        }

        // Check that the column exists
        if (columns.getIndex(columnId) == -1) {
            columns.addLabel(columnId);
        }

        getRow(layerId, rowId).put(columnId, value);
    }

    private Map<String, Object> getRow(String layer, String row) {

        if (!values.get(layer).containsKey(row)) {
            values.get(layer).put(row, new ConcurrentHashMap<String, Object>());
        }

        return values.get(layer).get(row);
    }


}
