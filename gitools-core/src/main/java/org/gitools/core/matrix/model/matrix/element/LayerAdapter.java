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
package org.gitools.core.matrix.model.matrix.element;

import org.gitools.api.matrix.ILayerAdapter;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.position.IMatrixPosition;
import org.gitools.core.matrix.model.MatrixLayer;
import org.gitools.core.matrix.model.MatrixLayers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LayerAdapter<T> implements ILayerAdapter<T> {

    private Class<? extends T> elementClass;
    private MatrixLayers<BeanMatrixLayer> matrixLayers;

    public LayerAdapter(Class<? extends T> elementClass) {
        super();
        this.elementClass = elementClass;
        createLayers();
    }

    @Override
    public Class<? extends T> getElementClass() {
        return elementClass;
    }

    @Override
    public MatrixLayers<BeanMatrixLayer> getMatrixLayers() {
        return matrixLayers;
    }

    @Override
    public <T> BeanMatrixLayer<T> getLayer(Class<T> layerClass, String layerName) {
        return (BeanMatrixLayer<T>) getMatrixLayers().get(layerName);
    }

    @Override
    public void set(IMatrix matrix, T value, IMatrixPosition position) {
        set(matrix, value, position.toVector());
    }

    @Override
    public void set(IMatrix matrix, T value, String... identifiers) {

        if (value == null) {
            return;
        }

        try {

            for (BeanMatrixLayer layer : matrixLayers) {
                Method getter = layer.getGetterMethod();
                matrix.set(layer, getter.invoke(value), identifiers);
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    public T get(IMatrix matrix, IMatrixPosition position) {
        return get(matrix, position.toVector());
    }

    @Override
    public T get(IMatrix matrix, String... identifiers) {

        T value = null;

        try {
            value = getElementClass().newInstance();

            for (BeanMatrixLayer layer : matrixLayers) {

                Method setter = layer.getSetterMethod();
                Object result = matrix.get(layer, identifiers);

                if (result != null) {
                    setter.invoke(value, result);
                }
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return value;
    }

    private void createLayers() {

        List<BeanMatrixLayer> layers = new ArrayList<>();
        for (Method m : getElementClass().getMethods()) {
            boolean isGet = m.getName().startsWith("get");
            if (m.getParameterTypes().length == 0 && !m.getName().equals("getClass") && (isGet || m.getName().startsWith("is"))) {

                final String getterName = isGet ? m.getName().substring(3) : m.getName().substring(2);

                final Class<?> propertyClass = m.getReturnType();

                String id = getterName;
                String name = id;
                String description = "";

                LayerDef a = m.getAnnotation(LayerDef.class);
                if (a != null) {
                    if (a.id() != null) {
                        id = a.id();
                    }
                    if (a.name() != null) {
                        name = a.name();
                    }
                    if (a.description() != null) {
                        description = a.description();
                    }
                }

                Method setterMethod = null;
                try {
                    setterMethod = getElementClass().getMethod("set" + getterName, new Class<?>[]{propertyClass});
                } catch (Exception e) {
                }

                BeanMatrixLayer prop = new BeanMatrixLayer(id, name, description, propertyClass, m, setterMethod);
                layers.add(prop);
            }
        }

        Collections.sort(layers, new Comparator<MatrixLayer>() {
            @Override
            public int compare(MatrixLayer o1, MatrixLayer o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        this.matrixLayers = new MatrixLayers<>(layers);
    }

}
