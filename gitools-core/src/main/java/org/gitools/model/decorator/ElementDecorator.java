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
package org.gitools.model.decorator;

import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.AbstractModel;
import org.gitools.model.decorator.impl.*;
import org.gitools.utils.colorscale.IColorScale;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({BinaryElementDecorator.class, FormattedTextElementDecorator.class, LinearElementDecorator.class, PValueElementDecorator.class, ZScoreElementDecorator.class, CorrelationElementDecorator.class, CategoricalElementDecorator.class})
public abstract class ElementDecorator extends AbstractModel
{

    private static final long serialVersionUID = -2101303088018509837L;

    @XmlTransient
    private IElementAdapter adapter;

    private int valueIndex;

    @XmlAttribute
    private String name;

    public ElementDecorator()
    {
    }

    public ElementDecorator(IElementAdapter adapter)
    {
        this.adapter = adapter;
    }

    public ElementDecorator(IColorScale scale, IElementAdapter adapter)
    {
        this.adapter = adapter;
    }

    public IElementAdapter getAdapter()
    {
        return adapter;
    }

    public abstract void setValueIndex(int valueIndex);

    public int getValueIndex()
    {
        return this.valueIndex;
    }

    public void setAdapter(IElementAdapter adapter)
    {
        this.adapter = adapter;
    }

    public abstract void decorate(ElementDecoration decoration, IMatrix matrix, int row, int column);

    public abstract IColorScale getScale();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    protected int getPropertyIndex(@NotNull String[] names)
    {
        int index = -1;
        int nameIndex = 0;

        while (index == -1 && nameIndex < names.length)
        {
            try
            {
                index = adapter.getPropertyIndex(names[nameIndex++]);
            } catch (Exception e)
            {
            }
        }

        if (index == -1)
        {
            index = 0;
        }

        return index;
    }

    protected static double toDouble(IMatrix matrix, int row, int column, int layer)
    {
        boolean empty = matrix.isEmpty(row, column);

        if (empty)
        {
            return Double.NaN;
        }

        return MatrixUtils.doubleValue(
                matrix.getCellValue(row, column, layer)
        );
    }
}
