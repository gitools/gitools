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
import org.gitools.core.model.decorator.Decoration;
import org.gitools.core.model.decorator.Decorator;
import org.gitools.utils.colorscale.impl.PValueColorScale;
import org.gitools.utils.formatter.GenericFormatter;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;

@XmlAccessorType(XmlAccessType.NONE)
public class PValueDecorator extends Decorator<PValueColorScale> {
    public static final String PROPERTY_SIGNIFICANCE = "significanceLevel";
    public static final String PROPERTY_USE_CORRECTION = "useCorrection";
    public static final String PROPERTY_CORRECTED_VALUE = "correctedValueIndex";
    public static final String PROPERTY_MIN_COLOR = "minColor";
    public static final String PROPERTY_MAX_COLOR = "maxColor";
    public static final String PROPERTY_NON_SIGNIFICANT_COLOR = "nonSignificantColor";
    public static final String PROPERTY_EMPTY_COLOR = "emptyColor";

    private int correctedValueIndex;
    private boolean useCorrection;
    private double significanceLevel;

    private final static GenericFormatter fmt = new GenericFormatter();

    private PValueColorScale scale;

    public PValueDecorator() {
        super();

        scale = new PValueColorScale();
        correctedValueIndex = -1;
        useCorrection = false;
        significanceLevel = 0.05;
    }

    public PValueColorScale getScale() {
        return scale;
    }

    public void setScale(PValueColorScale scale) {
        this.scale = scale;
    }

    @XmlElement(name = "filter-layer-index")
    public final int getCorrectedValueIndex() {
        return correctedValueIndex;
    }

    public final void setCorrectedValueIndex(int correctedValueIndex) {
        int old = this.correctedValueIndex;
        this.correctedValueIndex = correctedValueIndex;
        firePropertyChange(PROPERTY_CORRECTED_VALUE, old, correctedValueIndex);
    }

    @XmlElement(name = "use-filter")
    public final boolean isUseCorrection() {
        return useCorrection;
    }

    public final void setUseCorrection(boolean useCorrection) {
        boolean old = this.useCorrection;
        this.useCorrection = useCorrection;
        firePropertyChange(PROPERTY_USE_CORRECTION, old, useCorrection);
    }

    @XmlElement(name = "significance")
    public final double getSignificanceLevel() {
        return significanceLevel;
    }

    public final void setSignificanceLevel(double significanceLevel) {
        double old = this.significanceLevel;
        this.significanceLevel = significanceLevel;
        getScale().setSignificanceLevel(significanceLevel);
        firePropertyChange(PROPERTY_SIGNIFICANCE, old, significanceLevel);
    }

    @XmlElement(name = "min-color")
    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    public Color getMinColor() {
        return getScale().getMinColor();
    }

    public void setMinColor(Color color) {
        Color old = getScale().getMinColor();
        getScale().setMinColor(color);
        firePropertyChange(PROPERTY_MIN_COLOR, old, color);
    }

    @XmlElement(name = "max-color")
    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    public Color getMaxColor() {
        return getScale().getMaxColor();
    }

    public void setMaxColor(Color color) {
        Color old = getScale().getMaxColor();
        getScale().setMaxColor(color);
        firePropertyChange(PROPERTY_MAX_COLOR, old, color);
    }

    @XmlElement(name = "non-significant-color")
    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    public Color getNonSignificantColor() {
        return getScale().getNonSignificantColor();
    }

    public void setNonSignificantColor(Color color) {
        Color old = getScale().getNonSignificantColor();
        getScale().setNonSignificantColor(color);
        firePropertyChange(PROPERTY_NON_SIGNIFICANT_COLOR, old, color);
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
    public void decorate(@NotNull Decoration decoration, IMatrix matrix, int row, int column, int layer) {

        Object value = matrix.getValue(row, column, layer);
        double v = toDouble(value);

        if (Double.isNaN(v)) {
            decoration.setBgColor(getScale().getEmptyColor());
            return;
        }

        boolean isSig = v <= significanceLevel;

        if (useCorrection && correctedValueIndex != -1) {
            Object cvalue = matrix.getValue(row, column, correctedValueIndex);
            double cv = toDouble(cvalue);

            isSig = cv <= significanceLevel;
        }

        final Color color = isSig ? getScale().valueColor(v) : getScale().getNonSignificantColor();

        decoration.setBgColor(color);
        if (isShowValue()) {
            decoration.setValue(fmt.format(value));
        }
    }


}
