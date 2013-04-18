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
package org.gitools.matrix.model.matrix.element;

import org.gitools.matrix.model.SimpleMatrixLayer;
import org.gitools.matrix.model.SimpleMatrixLayers;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
@XmlRootElement
public class BeanElementAdapter extends AbstractElementAdapter {

    private static final long serialVersionUID = 2174377187447656241L;

    protected BeanElementAdapter() {
    }

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
        List<SimpleMatrixLayer> properties = new ArrayList<SimpleMatrixLayer>();

        for (Method m : elementClass.getMethods()) {
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
                    setterMethod = elementClass.getMethod("set" + getterName, new Class<?>[]{propertyClass});
                } catch (Exception e) {
                }

                SimpleMatrixLayer prop = new BeanElementProperty(id, name, description, propertyClass, m, setterMethod);

                properties.add(prop);
            }
        }

        setProperties(new SimpleMatrixLayers(properties));
    }

    @Nullable
    @Override
    public Object getValue(Object element, int index) {
        BeanElementProperty prop = (BeanElementProperty) getProperty(index);
        Method m = prop.getGetterMethod();
        Object value = null;
        try {
            value = m.invoke(element, (Object[]) null);
        } catch (Exception e) {
        }
        return value;
    }

    @Override
    public void setValue(Object element, int index, Object value) {
        BeanElementProperty prop = (BeanElementProperty) getProperty(index);
        Method m = prop.getSetterMethod();
        try {
            value = m.invoke(element, value);
        } catch (Exception e) {

        }
    }
}
