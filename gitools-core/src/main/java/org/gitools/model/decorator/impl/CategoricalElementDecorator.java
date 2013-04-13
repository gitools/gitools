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
import org.gitools.utils.colorscale.ColorScalePoint;
import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.colorscale.impl.CategoricalColorScale;
import org.gitools.utils.formatter.GenericFormatter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CategoricalElementDecorator extends ElementDecorator
{

    private static final long serialVersionUID = -181427286948958314L;

    private int valueIndex;

    private CategoricalColorScale scale;

    private final static GenericFormatter fmt = new GenericFormatter("<");

    public CategoricalElementDecorator()
    {
        super();
    }

    public CategoricalElementDecorator(double[] points)
    {
        this(null, new CategoricalColorScale(points));
    }

    public CategoricalElementDecorator(IElementAdapter adapter)
    {
        this(adapter, new CategoricalColorScale());
    }

    public CategoricalElementDecorator(IElementAdapter adapter, CategoricalColorScale scale)
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

    public final void setColor(double value, Color valueColor)
    {
        if (scale.getColorScalePoint(value) != null)
        {
            Color old = scale.getColorScalePoint(value).getColor();
            scale.getColorScalePoint(value).setColor(valueColor);
            firePropertyChange(PROPERTY_CHANGED, old, valueColor);
        }
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

    @Override
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

    public void setCategories(ColorScalePoint[] newScalePoints)
    {
        ColorScalePoint[] old = scale.getPointObjects();
        scale.setPointObjects(newScalePoints);
        firePropertyChange(PROPERTY_CHANGED, old, newScalePoints);
    }

    public int getCategoriesCount()
    {
        return scale.getPoints().length;
    }

    @Override
    public IColorScale getScale()
    {
        return scale;
    }

}
