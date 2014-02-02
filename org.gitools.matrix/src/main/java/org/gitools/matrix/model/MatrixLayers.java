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

import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixLayers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MatrixLayers<T extends IMatrixLayer> implements IMatrixLayers<T> {

    public static final String LAYERS_ID = "layers";

    private List<T> layers = new ArrayList<>();
    private Map<String, Integer> idToIndex = new HashMap<>();

    public MatrixLayers() {
        super();
    }

    public MatrixLayers(T... layers) {
        this(Arrays.asList(layers));
    }

    public MatrixLayers(Iterable<T> layers) {
        this();

        for (T layer : layers) {
            add(layer);
        }
    }

    @Override
    public String[] getIds() {
        return idToIndex.keySet().toArray(new String[size()]);
    }

    public int indexOf(String label) {
        if (idToIndex.containsKey(label)) {
            return idToIndex.get(label);
        }

        return -1;
    }

    @Override
    public int size() {
        return layers.size();
    }

    public String getLabel(int index) {

        if (!layers.contains(index)) {
            return null;
        }

        return layers.get(index).getId();
    }

    @Override
    public T get(String layer) {

        int index = indexOf(layer);

        if (index == -1) {
            return null;
        }

        return layers.get(index);
    }

    @Override
    public T get(int layer) {
        return layers.get(layer);
    }

    public void add(T matrixLayer) {
        layers.add(matrixLayer);
        idToIndex.put(matrixLayer.getId(), layers.indexOf(matrixLayer));
    }


    @Override
    public Iterator<T> iterator() {
        return layers.iterator();
    }
}
