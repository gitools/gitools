/*
 * #%L
 * org.gitools.matrix
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.matrix.model.matrix.element;

import org.gitools.api.matrix.*;

import java.util.HashMap;
import java.util.Map;

public class MapLayerAdapter<T> implements ILayerAdapter<Map<String, T>> {

    private IMatrixDimension mappedDimension;
    private ILayerAdapter<T> innerAdapter;

    public MapLayerAdapter(IMatrixDimension mappedDimension, ILayerAdapter<T> innerAdapter) {
        this.mappedDimension = mappedDimension;
        this.innerAdapter = innerAdapter;
    }

    @Override
    public IMatrixLayers<? extends IMatrixLayer> getMatrixLayers() {
        return innerAdapter.getMatrixLayers();
    }

    @Override
    public <L> IMatrixLayer<L> getLayer(Class<L> layerClass, String layerName) {
        return innerAdapter.getLayer(layerClass, layerName);
    }

    @Override
    public void set(IMatrix matrix, Map<String, T> value, IMatrixPosition position) {

        for (String key : value.keySet()) {
            innerAdapter.set(matrix, value.get(key), position.set(mappedDimension, key));
        }

    }

    @Override
    public void set(IMatrix matrix, Map<String, T> value, String... identifiers) {
        throw new UnsupportedOperationException("This layer adapter requires a IMatrixPosition instead of a identifiers array");
    }

    @Override
    public Map<String, T> get(IMatrix matrix, IMatrixPosition position) {

        Map<String, T> results = new HashMap();
        for (String key : mappedDimension) {
            results.put(key, innerAdapter.get(matrix, position.set(mappedDimension, key)));
        }

        return results;
    }

    @Override
    public Map<String, T> get(IMatrix matrix, String... identifiers) {
        throw new UnsupportedOperationException("This layer adapter requires a IMatrixPosition instead of a identifiers array");
    }
}
