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

import java.util.*;

public class MatrixLayers<T extends MatrixLayer> implements IMatrixLayers<T> {

    private List<T> layers = new ArrayList<>();
    private Map<String, Integer> idToIndex = new HashMap<>();

    public MatrixLayers() {
    }

    public MatrixLayers(List<T> descriptors) {
        for (T layer : descriptors) {
            add(layer);
        }
    }


    private void init() {
        this.idToIndex = new HashMap<>(layers.size());
        for (int i = 0; i < layers.size(); i++) {
            this.idToIndex.put(layers.get(i).getId(), i);
        }
    }

    @Override
    public T get(int layerIndex) {
        return layers.get(layerIndex);
    }

    @Override
    public int findId(String id) {
        if (idToIndex.containsKey(id)) {
            return idToIndex.get(id);
        }

        return -1;
    }

    @Override
    public int size() {
        return layers.size();
    }

    @Override
    public Iterator<T> iterator() {
        return layers.iterator();
    }

    public void add(T matrixLayer) {
        layers.add(matrixLayer);
        idToIndex.put(matrixLayer.getId(), layers.indexOf(matrixLayer));
    }
}
