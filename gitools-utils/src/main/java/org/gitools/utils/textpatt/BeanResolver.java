/*
 * #%L
 * gitools-utils
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
package org.gitools.utils.textpatt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * @noinspection ALL
 */
public class BeanResolver implements TextPattern.VariableValueResolver
{

    private static class BeanProperty
    {
        private final Method m;

        /**
         * @noinspection UnusedDeclaration
         */
        public BeanProperty(Method m)
        {
            this.m = m;
        }

        @Nullable
        public Object get(Object beanInstance)
        {
            try
            {
                return m.invoke(beanInstance, (Object[]) null);
            } catch (Exception ex)
            {
                return null;
            }
        }
    }

    private final Class<?> beanClass;
    private Object beanInstance;

    private final Map<String, BeanProperty> beanProperties;

    public BeanResolver(@NotNull Class<?> beanClass)
    {
        this.beanClass = beanClass;
        this.beanProperties = readProperties(beanClass);
    }

    public Class<?> getBeanClass()
    {
        return beanClass;
    }

    public Object getBeanInstance()
    {
        return beanInstance;
    }

    public void setBeanInstance(Object beanInstance)
    {
        this.beanInstance = beanInstance;
    }

    @Nullable
    @Override
    public String resolveValue(@NotNull String variableName)
    {
        BeanProperty p = beanProperties.get(variableName.toLowerCase());
        if (p == null)
        {
            return null;
        }

        Object value = p.get(beanInstance);
        if (value == null)
        {
            return null;
        }

        return value.toString();
    }

    @NotNull
    private Map<String, BeanProperty> readProperties(@NotNull Class<?> beanClass)
    {
        Map<String, BeanProperty> map = new HashMap<String, BeanResolver.BeanProperty>();

        for (Method m : beanClass.getMethods())
        {
            boolean isGet = m.getName().startsWith("get");
            boolean isIs = m.getName().startsWith("is");
            if (m.getParameterTypes().length == 0 && !m.getName().equals("getClass") && (isGet || isIs))
            {

                final String getterName = isGet ? m.getName().substring(3) : m.getName().substring(2);

                //final Class<?> propertyClass = m.getReturnType();

                BeanProperty p = new BeanProperty(m);
                map.put(getterName.toLowerCase(), p);
            }
        }

        return map;
    }
}
