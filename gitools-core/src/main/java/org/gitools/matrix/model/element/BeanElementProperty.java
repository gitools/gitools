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
package org.gitools.matrix.model.element;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.lang.reflect.Method;

@XmlRootElement
public class BeanElementProperty extends AbstractElementAttribute
{

    private static final long serialVersionUID = 1735870808859461498L;

    protected Method getterMethod;
    protected Method setterMethod;

    protected BeanElementProperty()
    {
    }

    public BeanElementProperty(
            String id, String name, String description,
            Class<?> valueClass, Method getterMethod, Method setterMethod)
    {

        super(id, name, description, valueClass);

        this.getterMethod = getterMethod;
        this.setterMethod = setterMethod;
    }

    @XmlElement(name = "Class")
    @Override
    public Class<?> getValueClass()
    {
        return valueClass;
    }

    static class MethodAdapter extends XmlAdapter<String, Method>
    {

        @NotNull
        @Override
        public String marshal(@NotNull Method v) throws Exception
        {
            final String className = v.getDeclaringClass().getCanonicalName();
            Class<?> returnType = v.getReturnType();
            Class<?>[] paramTypes = v.getParameterTypes();
            Class<?> type = returnType != null ? returnType : paramTypes[0];

            return className + ":" + v.getName() + ":" + type.getCanonicalName();
        }

        @Override
        public Method unmarshal(@NotNull String v) throws Exception
        {
            final String[] names = v.split(":");
            final String className = names[0];
            final String methodName = names[1];
            final String paramClassName = names[2];

            Class<?> paramClass = Class.forName(paramClassName);

            Class<?> cls = Class.forName(className);
            return cls.getMethod(methodName, paramClass);
        }
    }

    @XmlElement(name = "getter")
    @XmlJavaTypeAdapter(MethodAdapter.class)
    public Method getGetterMethod()
    {
        return getterMethod;
    }

    @XmlElement(name = "setter")
    @XmlJavaTypeAdapter(MethodAdapter.class)
    public Method getSetterMethod()
    {
        return setterMethod;
    }
}
