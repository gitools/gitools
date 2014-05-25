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
package org.gitools.heatmap.decorator.impl;

import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.heatmap.decorator.Decoration;
import org.gitools.heatmap.decorator.Decorator;
import org.gitools.utils.colorscale.ColorScalePoint;
import org.gitools.utils.colorscale.impl.CategoricalColorScale;
import org.gitools.utils.formatter.ITextFormatter;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;

@XmlAccessorType(XmlAccessType.NONE)
public class CategoricalDecorator extends Decorator<CategoricalColorScale> {
    public static final String PROPERTY_COLOR = "color";
    public static final String PROPERTY_EMPTY_COLOR = "emptyColor";
    public static final String PROPERTY_CATEGORIES = "categories";

    private CategoricalColorScale scale;

    public CategoricalDecorator() {
        super();

        this.scale = new CategoricalColorScale();
    }

    public CategoricalColorScale getScale() {
        return scale;
    }

    public void setScale(CategoricalColorScale scale) {
        this.scale = scale;
    }

    public final void setColor(double value, Color valueColor) {
        if (getScale().getColorScalePoint(value) != null) {
            Color old = getScale().getColorScalePoint(value).getColor();
            getScale().getColorScalePoint(value).setColor(valueColor);
            firePropertyChange(PROPERTY_COLOR, old, valueColor);
        }
    }

    @XmlElement(name = "empty-color")
    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    public Color getEmptyColor() {
        return getScale().getEmptyColor();
    }

    public void setEmptyColor(Color color) {
        Color old = getScale().getEmptyColor();
        getScale().setEmptyColor(color);
        firePropertyChange(PROPERTY_EMPTY_COLOR, old, color);
    }

    @Override
    public void decorate(Decoration decoration, ITextFormatter textFormatter, IMatrix matrix, IMatrixLayer layer, String... identifiers) {

        Object value = matrix.get(layer, identifiers);
        double v = toDouble(value);

        if (Double.isNaN(v)) {
            decoration.setBgColor(getScale().getEmptyColor());
            return;
        }

        final Color color = getScale().valueColor(v);

        decoration.setBgColor(color);
        if (isShowValue()) {
            if (scale.getColorScalePoint(v) == null) return;
            String category = scale.getColorScalePoint(v).getName();
            decoration.setValue(
                    category.equals("") ? textFormatter.format(value) : category
            );
        }
    }

    @XmlElementWrapper(name = "categories")
    @XmlElement(name = "category")
    public ColorScalePoint[] getCategories() {
        return getScale().getPointObjects();
    }

    public void setCategories(ColorScalePoint[] newScalePoints) {
        ColorScalePoint[] old = getScale().getPointObjects();
        getScale().setPointObjects(newScalePoints);
        firePropertyChange(PROPERTY_CATEGORIES, old, newScalePoints);
    }

    @Override
    public NonEventToNullFunction getEventFunction() {
        return new NonEventToNullFunction<CategoricalColorScale>(scale, "All values represented in the categorical scale are events ") {

            @Override
            public Double apply(Double value, IMatrixPosition position) {
                this.position = position;
                if (value == null) {
                    return null;
                }

                for (double pointValue : getColorScale().getPoints()) {
                    if (pointValue == value) {
                        return value;
                    }
                }
                return null;
            }
        };
    }

}
