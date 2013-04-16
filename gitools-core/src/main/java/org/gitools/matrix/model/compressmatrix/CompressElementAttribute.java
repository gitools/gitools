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
package org.gitools.matrix.model.compressmatrix;

import org.gitools.matrix.model.IMatrixLayer;


/**
 * The type Compress element attribute.
 */
public class CompressElementAttribute implements IMatrixLayer
{

    private String id;
    private String name;
    private String description;
    private Class<?> valueClass;

    /**
     * Instantiates a new Compress element attribute.
     *
     * @param id the identifier of the attribute
     * @param valueClass the class of the attribute value
     */
    public CompressElementAttribute(String id, Class<?> valueClass)
    {
        this.id = id;
        this.valueClass = valueClass;
        this.name = id;
        this.description = id;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Class<?> getValueClass()
    {
        return valueClass;
    }

    @Override
    public String getDescription()
    {
        return description;
    }
}
