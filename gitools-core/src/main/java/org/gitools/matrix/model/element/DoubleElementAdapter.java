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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class DoubleElementAdapter extends AbstractElementAdapter
{

    private static final long serialVersionUID = 3053254525952874940L;

    private class InternalAttribute extends AbstractElementAttribute implements Serializable
    {

        private static final long serialVersionUID = -6461253830835864744L;

        public InternalAttribute(String id, String name, String description, Class<?> valueClass)
        {
            super(id, name, description, valueClass);
        }

    }

    public DoubleElementAdapter()
    {
        super(double.class);

        this.setProperties(getPropertyList());
    }

    @Override
    public Object getValue(Object element, int index)
    {
        return element;
    }

    @Override
    public void setValue(Object element, int index, Object value)
    {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " doesn't support change string value.");
    }

    @NotNull
    private List<IElementAttribute> getPropertyList()
    {
        final List<IElementAttribute> properties = new ArrayList<IElementAttribute>();
        IElementAttribute property = new InternalAttribute("value", "Value", "", double.class);
        properties.add(property);
        return properties;
    }
}
