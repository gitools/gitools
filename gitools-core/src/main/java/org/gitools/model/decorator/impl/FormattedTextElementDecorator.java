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
package org.gitools.model.decorator.impl;

import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.utils.colorscale.IColorScale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IllegalFormatException;

/**
 * @noinspection ALL
 */
public class FormattedTextElementDecorator extends ElementDecorator
{

    private static final long serialVersionUID = -8595819997133940913L;

    private int valueIndex = -1;
    @Nullable
    private String formatString = null;

    public FormattedTextElementDecorator()
    {
        this.formatString = "%1$s";
    }

    public FormattedTextElementDecorator(IElementAdapter elementAdapter)
    {
        super(elementAdapter);
        this.formatString = "%1$s";
    }

    @Nullable
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        FormattedTextElementDecorator obj = null;
        try
        {
            obj = (FormattedTextElementDecorator) super.clone();
        } catch (CloneNotSupportedException ex)
        {
        }
        return obj;
    }

    public void setFormatString(String formatString)
    {
        this.formatString = formatString;
    }

    @Nullable
    public String getFormatString()
    {
        return formatString;
    }


    public final int getValueIndex()
    {
        return valueIndex;
    }

    public final void setValueIndex(int valueIndex)
    {
        this.valueIndex = valueIndex;
        firePropertyChange(PROPERTY_CHANGED);
    }

    @Override
    public void decorate(@NotNull ElementDecoration decoration, @NotNull Object element)
    {
        String cellText;
        try
        {
            if (this.valueIndex >= 0)
            {
                cellText = String.format(formatString, this.adapter.getValue(element, valueIndex));
            }
            else
            {
                cellText = String.format(formatString, element);
            }

        } catch (IllegalFormatException e)
        {
            cellText = element.toString();
        }
        decoration.setText(cellText);
        decoration.setToolTip(cellText);
    }

    @Nullable
    @Override
    public IColorScale getScale()
    {
        return null;
    }

}
