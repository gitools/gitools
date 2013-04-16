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
import org.gitools.model.decorator.Decoration;
import org.gitools.model.decorator.Decorator;
import org.gitools.utils.colorscale.ColorScalePoint;
import org.gitools.utils.colorscale.impl.CategoricalColorScale;
import org.gitools.utils.formatter.GenericFormatter;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;

@XmlAccessorType(XmlAccessType.NONE)
public class CategoricalDecorator extends Decorator<CategoricalColorScale>
{
    private final static GenericFormatter fmt = new GenericFormatter("<");
    public static final String PROPERTY_COLOR = "color";
    public static final String PROPERTY_EMPTY_COLOR = "emptyColor";
    public static final String PROPERTY_CATEGORIES = "categories";
    public static final String PROPERTY_CATEGORIES_COUNT = "categoriesCount";

    private CategoricalColorScale scale;

    public CategoricalDecorator()
    {
        super();

        this.scale = new CategoricalColorScale();
    }

    public CategoricalColorScale getScale()
    {
        return scale;
    }

    public void setScale(CategoricalColorScale scale)
    {
        this.scale = scale;
    }

    public final void setColor(double value, Color valueColor)
    {
        if (getScale().getColorScalePoint(value) != null)
        {
            Color old = getScale().getColorScalePoint(value).getColor();
            getScale().getColorScalePoint(value).setColor(valueColor);
            firePropertyChange(PROPERTY_COLOR, old, valueColor);
        }
    }

    @XmlElement(name = "empty-color")
    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    public Color getEmptyColor()
    {
        return getScale().getEmptyColor();
    }

    public void setEmptyColor(Color color)
    {
        Color old = getScale().getEmptyColor();
        getScale().setEmptyColor(color);
        firePropertyChange(PROPERTY_EMPTY_COLOR, old, color);
    }

    @Override
    public void decorate(Decoration decoration, IMatrix matrix, int row, int column, int layer)
    {
        decoration.reset();

        double v = toDouble(matrix, row, column, layer);

        if (Double.isNaN(v))
        {
            decoration.setBgColor(getScale().getEmptyColor());
            decoration.setToolTip("Empty cell");
            return;
        }

        final Color color = getScale().valueColor(v);

        decoration.setBgColor(color);
        decoration.setToolTip(fmt.pvalue(v));
    }

    @XmlElementWrapper(name = "categories")
    @XmlElement(name = "category")
    public ColorScalePoint[] getCategories()
    {
        return getScale().getPointObjects();
    }

    public void setCategories(ColorScalePoint[] newScalePoints)
    {
        ColorScalePoint[] old = getScale().getPointObjects();
        getScale().setPointObjects(newScalePoints);
        firePropertyChange(PROPERTY_CATEGORIES, old, newScalePoints);
        firePropertyChange(PROPERTY_CATEGORIES_COUNT, old.length, newScalePoints.length);
    }

    public String getCategoriesCount()
    {
        return String.valueOf(getScale().getPoints().length);
    }


}
