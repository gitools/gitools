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

import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.colorscale.impl.LinearTwoSidedColorScale;
import org.gitools.utils.formatter.GenericFormatter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class LinearTwoSidedElementDecorator extends ElementDecorator
{

    private static final long serialVersionUID = -181427286948958314L;

    private int valueIndex;

    private final LinearTwoSidedColorScale scale;

    private final static GenericFormatter fmt = new GenericFormatter("<");

    public LinearTwoSidedElementDecorator()
    {
        this(null, new LinearTwoSidedColorScale());
    }

    public LinearTwoSidedElementDecorator(IElementAdapter adapter)
    {
        this(adapter, new LinearTwoSidedColorScale());
    }

    public LinearTwoSidedElementDecorator(IElementAdapter adapter, LinearTwoSidedColorScale scale)
    {
        super(adapter);

        valueIndex = getPropertyIndex(new String[]{"value", "log2ratio", "score"});

        this.scale = scale;
    }

    public final int getValueIndex()
    {
        return valueIndex;
    }

    public final void setValueIndex(int valueIndex)
    {
        int old = this.valueIndex;
        this.valueIndex = valueIndex;
        firePropertyChange(PROPERTY_CHANGED, old, valueIndex);
    }

    public final double getMinValue()
    {
        return scale.getMin().getValue();
    }

    public final void setMinValue(double minValue)
    {
        double old = scale.getMin().getValue();
        scale.getMin().setValue(minValue);
        firePropertyChange(PROPERTY_CHANGED, old, minValue);
    }

    public final double getMidValue()
    {
        return scale.getMid().getValue();
    }

    public final void setMidValue(double midValue)
    {
        double old = scale.getMid().getValue();
        scale.getMid().setValue(midValue);
        firePropertyChange(PROPERTY_CHANGED, old, midValue);
    }

    public final double getMaxValue()
    {
        return scale.getMax().getValue();
    }

    public final void setMaxValue(double maxValue)
    {
        double old = scale.getMax().getValue();
        scale.getMax().setValue(maxValue);
        firePropertyChange(PROPERTY_CHANGED, old, maxValue);
    }

    public final Color getMinColor()
    {
        return scale.getMin().getColor();
    }

    public final void setMinColor(Color minColor)
    {
        Color old = scale.getMin().getColor();
        scale.getMin().setColor(minColor);
        firePropertyChange(PROPERTY_CHANGED, old, minColor);
    }

    public final Color getMidColor()
    {
        return scale.getMid().getColor();
    }

    public final void setMidColor(Color midColor)
    {
        Color old = scale.getMid().getColor();
        scale.getMid().setColor(midColor);
        firePropertyChange(PROPERTY_CHANGED, old, midColor);
    }

    public final Color getMaxColor()
    {
        return scale.getMax().getColor();
    }

    public final void setMaxColor(Color maxColor)
    {
        Color old = scale.getMax().getColor();
        scale.getMax().setColor(maxColor);
        firePropertyChange(PROPERTY_CHANGED, old, maxColor);
    }

    public Color getEmptyColor()
    {
        return scale.getEmptyColor();
    }

    public void setEmptyColor(Color color)
    {
        Color old = scale.getEmptyColor();
        scale.setEmptyColor(color);
        firePropertyChange(PROPERTY_CHANGED, old, color);
    }

    public void decorate(@NotNull ElementDecoration decoration,  IMatrix matrix, int row, int column)
    {
        decoration.reset();

        double v = toDouble(matrix, row, column, valueIndex);

        if (Double.isNaN(v))
        {
            decoration.setBgColor(scale.getEmptyColor());
            decoration.setToolTip("Empty cell");
            return;
        }

        final Color color = scale.valueColor(v);

        decoration.setBgColor(color);
        decoration.setToolTip(fmt.pvalue(v));
    }

    @Override
    public IColorScale getScale()
    {
        return scale;
    }
}
