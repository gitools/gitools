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

import org.gitools.core.matrix.model.AbstractMatrix;
import org.gitools.core.matrix.model.IMatrixLayers;
import org.gitools.core.matrix.model.MatrixLayer;
import org.gitools.core.matrix.model.MatrixLayers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMatrix extends AbstractMatrix {

    private HashMatrixDimension rows;
    private HashMatrixDimension columns;
    private MatrixLayers layers;

    private Map<Integer, Map<String, Map<String, Object>>> values;

    public HashMatrix() {
        this(new HashMatrixDimension(), new HashMatrixDimension(), new MatrixLayers());
    }

    public HashMatrix(String[] rows, String[] columns, MatrixLayers layers) {
        this(new HashMatrixDimension(rows), new HashMatrixDimension(columns), layers);
    }

    private HashMatrix(HashMatrixDimension rows, HashMatrixDimension columns, MatrixLayers layers) {
        this.rows = rows;
        this.columns = columns;
        this.layers = layers;

        for (int i=0; i < layers.size(); i++) {
            values.put(i, new HashMap<String, Map<String, Object>>());
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
    public Object getValue(int[] position, int layer) {

        String columnIdentifier = columns.getLabel(columns.getPosition(position));
        String rowIdentifier = rows.getLabel(rows.getPosition(position));

        return getRow(layer, rowIdentifier).get(columnIdentifier);
    }

    @Override
    public void setValue(int[] position, int layer, Object value) {

        String columnIdentifier = columns.getLabel(columns.getPosition(position));
        String rowIdentifier = rows.getLabel(rows.getPosition(position));

        getRow(layer, rowIdentifier).put(columnIdentifier, value);
    }

    public void setValue(String rowId, String columnId, String layerId, Object value) {

        // Check that the layers exists
        int layerIndex = layers.findId(layerId);
        if (layerIndex == -1) {
            layers.add(new MatrixLayer(layerId, value.getClass()));
            layerIndex = layers.findId(layerId);
            values.put(layerIndex, new HashMap<String, Map<String, Object>>());
        }

        // Check that the row exists
        if (rows.getIndex(rowId) == -1) {
            rows.addLabel(rowId);
        }

        // Check that the column exists
        if (columns.getIndex(columnId) == -1) {
            columns.addLabel(columnId);
        }

        getRow(layerIndex, rowId).put(columnId, value);
    }

    private Map<String, Object> getRow(int layer, String row) {

        if (!values.get(layer).containsKey(row)) {
            values.get(layer).put(row, new HashMap<String, Object>());
        }

        return values.get(layer).get(row);
    }


}
