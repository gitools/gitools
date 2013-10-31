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

import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.MatrixLayer;
import org.gitools.core.matrix.model.MatrixLayers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BeanElementAdapter extends ElementAdapter<BeanMatrixLayer> {

    private static final long serialVersionUID = 2174377187447656241L;

    public BeanElementAdapter(Class<?> elementClass) {
        super(elementClass);

        readProperties();
    }

    @Override
    protected void setElementClass(Class<?> elementClass) {
        super.setElementClass(elementClass);

        readProperties();
    }

    void readProperties() {
        List<BeanMatrixLayer> properties = new ArrayList<>();

        for (Method m : getElementClass().getMethods()) {
            boolean isGet = m.getName().startsWith("get");
            if (m.getParameterTypes().length == 0 && !m.getName().equals("getClass") && (isGet || m.getName().startsWith("is"))) {

                final String getterName = isGet ? m.getName().substring(3) : m.getName().substring(2);

                final Class<?> propertyClass = m.getReturnType();

                String id = getterName;
                String name = id;
                String description = "";

                AttributeDef a = m.getAnnotation(AttributeDef.class);
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
                properties.add(prop);
            }
        }

        Collections.sort(properties, new Comparator<MatrixLayer>() {
            @Override
            public int compare(MatrixLayer o1, MatrixLayer o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        setMatrixLayers(new MatrixLayers<>(properties));
    }

    @Override
    public void setCell(IMatrix resultsMatrix, int row, int column, Object result) {

        try {
            for (int layerIndex = 0; layerIndex < getMatrixLayers().size(); layerIndex++) {

                BeanMatrixLayer layer = getMatrixLayers().get(layerIndex);
                Method getter = layer.getGetterMethod();

                resultsMatrix.setValue(row, column, layerIndex, getter.invoke(result));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Object getCell(IMatrix resultsMatrix, int row, int column) {

        Object value = null;
        try {
            value = getElementClass().newInstance();

            for (int layerIndex = 0; layerIndex < getMatrixLayers().size(); layerIndex++) {

                BeanMatrixLayer layer = getMatrixLayers().get(layerIndex);
                Method setter = layer.getSetterMethod();

                setter.invoke(value, resultsMatrix.getValue(row, column, layerIndex));
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
}
