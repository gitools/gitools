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
package org.gitools.core.model.decorator.impl;

import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.model.decorator.Decoration;
import org.gitools.core.model.decorator.Decorator;
import org.gitools.utils.colorscale.impl.LinearTwoSidedColorScale;
import org.gitools.utils.formatter.ITextFormatter;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;

@XmlAccessorType(XmlAccessType.NONE)
public class LinearDecorator extends Decorator<LinearTwoSidedColorScale> {

    public static final String PROPERTY_MIN_COLOR = "minColor";
    public static final String PROPERTY_MID_COLOR = "midColor";
    public static final String PROPERTY_MAX_COLOR = "maxColor";
    public static final String PROPERTY_MIN_VALUE = "minValue";
    public static final String PROPERTY_MID_VALUE = "midValue";
    public static final String PROPERTY_MAX_VALUE = "maxValue";
    public static final String PROPERTY_EMPTY_COLOR = "emptyColor";

    private LinearTwoSidedColorScale scale;

    public LinearDecorator() {
        this(new LinearTwoSidedColorScale());
    }

    public LinearDecorator(LinearTwoSidedColorScale scale) {
        super();
        this.scale = scale;
    }

    public LinearTwoSidedColorScale getScale() {
        return scale;
    }

    public void setScale(LinearTwoSidedColorScale scale) {
        this.scale = scale;
    }

    @XmlElement(name = "min-value")
    public final double getMinValue() {
        return getScale().getMin().getValue();
    }

    public final void setMinValue(double minValue) {
        double old = getScale().getMin().getValue();
        getScale().getMin().setValue(minValue);
        firePropertyChange(PROPERTY_MIN_VALUE, old, minValue);
    }

    @XmlElement(name = "mid-value")
    public final double getMidValue() {
        return getScale().getMid().getValue();
    }

    public final void setMidValue(double midValue) {
        double old = getScale().getMid().getValue();
        getScale().getMid().setValue(midValue);
        firePropertyChange(PROPERTY_MID_VALUE, old, midValue);
    }

    @XmlElement(name = "max-value")
    public final double getMaxValue() {
        return getScale().getMax().getValue();
    }

    public final void setMaxValue(double maxValue) {
        double old = getScale().getMax().getValue();
        getScale().getMax().setValue(maxValue);
        firePropertyChange(PROPERTY_MAX_VALUE, old, maxValue);
    }

    @XmlElement(name = "min-color")
    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    public final Color getMinColor() {
        return getScale().getMin().getColor();
    }

    public final void setMinColor(Color minColor) {
        Color old = getScale().getMin().getColor();
        getScale().getMin().setColor(minColor);
        firePropertyChange(PROPERTY_MIN_COLOR, old, minColor);
    }

    @XmlElement(name = "mid-color")
    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    public final Color getMidColor() {
        return getScale().getMid().getColor();
    }

    public final void setMidColor(Color midColor) {
        Color old = getScale().getMid().getColor();
        getScale().getMid().setColor(midColor);
        firePropertyChange(PROPERTY_MID_COLOR, old, midColor);
    }

    @XmlElement(name = "max-color")
    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    public final Color getMaxColor() {
        return getScale().getMax().getColor();
    }

    public final void setMaxColor(Color maxColor) {
        Color old = getScale().getMax().getColor();
        getScale().getMax().setColor(maxColor);
        firePropertyChange(PROPERTY_MAX_COLOR, old, maxColor);
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

    public void decorate(@NotNull Decoration decoration, ITextFormatter textFormatter, IMatrix matrix, IMatrixLayer layer, String... identifiers) {
        Object value = matrix.get(layer, identifiers);

        double v = toDouble(value);

        if (Double.isNaN(v)) {
            decoration.setBgColor(getScale().getEmptyColor());
            return;
        }

        final Color color = getScale().valueColor(v);

        decoration.setBgColor(color);

        if (isShowValue()) {
            decoration.setValue(textFormatter.format(value));
        }

    }

}
