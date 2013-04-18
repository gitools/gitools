/*
 * #%L
 * gitools-utils
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
package org.gitools.utils.colorscale.impl;

import org.gitools.utils.aggregation.IAggregator;
import org.gitools.utils.aggregation.MultAggregator;
import org.gitools.utils.color.utils.ColorUtils;
import org.gitools.utils.colorscale.ColorScaleRange;
import org.gitools.utils.colorscale.NumericColorScale;
import org.gitools.utils.colorscale.util.ColorConstants;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.ArrayList;

@XmlAccessorType(XmlAccessType.FIELD)
public class PValueColorScale extends NumericColorScale {

    private static final double defaultLogFactor = 0.25;

    private double significanceLevel;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color minColor;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color maxColor;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color nonSignificantColor;

    private PValueColorScale(double significanceLevel, Color minColor, Color maxColor, Color nonSignificantColor) {

        super();

        this.significanceLevel = significanceLevel;
        this.maxColor = maxColor;
        this.minColor = minColor;
        this.nonSignificantColor = nonSignificantColor;
    }

    public PValueColorScale() {
        this(0.05, ColorConstants.minColor, ColorConstants.maxColor, ColorConstants.nonSignificantColor);
        updateRangesList();
    }

    @Override
    protected Color colorOf(double value) {

        if (value > significanceLevel || value < 0) {
            return nonSignificantColor;
        }

        double f = value / significanceLevel;
        f = (f == 0.0 ? 0.0 : 1.0 + defaultLogFactor * Math.log10(f));
        return ColorUtils.mix(maxColor, minColor, f);

    }

    @NotNull
    @Override
    public double[] getPoints() {
        return new double[]{0, significanceLevel, 1};
    }

    double getSignificanceLevel() {
        return significanceLevel;
    }

    public void setSignificanceLevel(double significanceLevel) {
        this.significanceLevel = significanceLevel;

        if (significanceLevel > 1) {
            this.significanceLevel = 1;
        }

        if (significanceLevel < 0) {
            this.significanceLevel = 0;
        }
        updateRangesList();
    }

    public Color getMinColor() {
        return minColor;
    }

    public void setMinColor(Color minColor) {
        this.minColor = minColor;
    }

    public Color getMaxColor() {
        return maxColor;
    }

    public void setMaxColor(Color maxColor) {
        this.maxColor = maxColor;
    }

    public Color getNonSignificantColor() {
        return nonSignificantColor;
    }

    public void setNonSignificantColor(Color nonSignificantColor) {
        this.nonSignificantColor = nonSignificantColor;
    }

    @Override
    protected void updateRangesList() {

        ArrayList<ColorScaleRange> rangesList = getInternalScaleRanges();
        rangesList.clear();

        double[] points = getPoints();
        double min = getMinValue();
        double max = getMaxValue();
        double mid = getSignificanceLevel();

        rangesList.add(new ColorScaleRange(min, mid, 20, min, null, mid, ColorScaleRange.LOGARITHMIC_TYPE));
        rangesList.add(new ColorScaleRange(mid, max, 10, null, null, max, ColorScaleRange.LOGARITHMIC_TYPE));

    }

    @NotNull
    @Override
    public IAggregator defaultAggregator() {
        return MultAggregator.INSTANCE;
    }
}
