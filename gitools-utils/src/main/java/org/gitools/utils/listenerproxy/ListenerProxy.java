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
package org.gitools.utils.listenerproxy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ListenerProxy<L> implements InvocationHandler
{

    @NotNull
    private List<L> listeners = new ArrayList<L>();

    public void addListener(L listener)
    {
        listeners.add(listener);
    }

    @Nullable
    @Override
    public Object invoke(Object proxy, @NotNull Method method, Object[] args) throws Throwable
    {
        for (L listener : listeners)
        {
            try
            {
                /*Object result =*/
                method.invoke(listener, args);
            } catch (InvocationTargetException ex)
            {
                throw ex.getTargetException();
            }
        }
        return null;
    }
}
