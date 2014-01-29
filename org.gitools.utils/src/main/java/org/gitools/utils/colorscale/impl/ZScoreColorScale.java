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

import org.gitools.api.analysis.IAggregator;
import org.gitools.utils.aggregation.SumAggregator;
import org.gitools.utils.color.Colors;
import org.gitools.utils.colorscale.ColorScaleRange;
import org.gitools.utils.colorscale.NumericColorScale;
import org.gitools.utils.colorscale.util.ColorConstants;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.ArrayList;

/**
 * @noinspection ALL
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ZScoreColorScale extends NumericColorScale {

    private double center;
    private double halfAmplitude;
    private double sigHalfAmplitude;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color leftMinColor;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color leftMaxColor;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color rightMinColor;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color rightMaxColor;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color nonSignificantColor;

    private ZScoreColorScale(double center, double halfAmplitude, double sigHalfAmplitude, Color leftMinColor, Color leftMaxColor, Color rightMinColor, Color rightMaxColor, Color nonSignificantColor) {
        this.center = center;
        this.halfAmplitude = halfAmplitude;
        this.sigHalfAmplitude = sigHalfAmplitude;
        this.leftMinColor = leftMinColor;
        this.leftMaxColor = leftMaxColor;
        this.rightMinColor = rightMinColor;
        this.rightMaxColor = rightMaxColor;
        this.nonSignificantColor = nonSignificantColor;
    }

    public ZScoreColorScale() {
        this(0, 10, 1.96, Color.BLUE, Color.CYAN, Color.YELLOW, Color.RED, ColorConstants.nonSignificantColor);
    }

    @Override
    protected Color colorOf(double value) {

        double absValue = Math.abs(value);

        if (absValue < sigHalfAmplitude) {
            return nonSignificantColor;
        }

        double f = (absValue - sigHalfAmplitude) / (halfAmplitude - sigHalfAmplitude);


        if (value > getMaxValue()) {
            return rightMaxColor;
        } else if (value < getMinValue()) {
            return leftMinColor;
        } else if (value > center) {
            return Colors.mix(rightMaxColor, rightMinColor, f);
        } else {
            return Colors.mix(leftMinColor, leftMaxColor, f);
        }

    }


    @Override
    public double[] getPoints() {
        return new double[]{center - halfAmplitude, center - sigHalfAmplitude, center + sigHalfAmplitude, center + halfAmplitude};
    }

    @Override
    protected void updateRangesList() {

        ArrayList<ColorScaleRange> rangesList = getInternalScaleRanges();
        rangesList.clear();

        ColorScaleRange left = new ColorScaleRange(getMinValue(), -getSigHalfAmplitude(), -getSigHalfAmplitude() - getMinValue());
        ColorScaleRange middle = new ColorScaleRange(-getSigHalfAmplitude(), getSigHalfAmplitude() - 0.01, getSigHalfAmplitude() * 2);
        ColorScaleRange right = new ColorScaleRange(getSigHalfAmplitude(), getMaxValue(), getMaxValue() - getSigHalfAmplitude());

        left.setLeftLabel(getMinValue());
        left.setRightLabel(-getSigHalfAmplitude());
        middle.setType(ColorScaleRange.CONSTANT_TYPE);
        right.setLeftLabel(getSigHalfAmplitude());
        right.setRightLabel(getMaxValue());

        rangesList.add(left);
        rangesList.add(middle);
        rangesList.add(right);
    }

    public double getCenter() {
        return center;
    }

    public void setCenter(double center) {
        this.center = center;
        updateRangesList();
    }

    /**
     * @noinspection UnusedDeclaration
     */
    public double getHalfAmplitude() {
        return halfAmplitude;
    }

    public void setHalfAmplitude(double halfAmplitude) {
        this.halfAmplitude = halfAmplitude;
        updateRangesList();
    }

    public double getSigHalfAmplitude() {
        return sigHalfAmplitude;
    }

    public void setSigHalfAmplitude(double sigHalfAmplitude) {
        this.sigHalfAmplitude = sigHalfAmplitude;
    }

    public Color getLeftMinColor() {
        return leftMinColor;
    }

    public void setLeftMinColor(Color leftMinColor) {
        this.leftMinColor = leftMinColor;
        updateRangesList();
    }

    public Color getLeftMaxColor() {
        return leftMaxColor;
    }

    public void setLeftMaxColor(Color leftMaxColor) {
        this.leftMaxColor = leftMaxColor;
        updateRangesList();
    }

    public Color getRightMinColor() {
        return rightMinColor;
    }

    public void setRightMinColor(Color rightMinColor) {
        this.rightMinColor = rightMinColor;
        updateRangesList();
    }

    public Color getRightMaxColor() {
        return rightMaxColor;
    }

    public void setRightMaxColor(Color rightMaxColor) {
        this.rightMaxColor = rightMaxColor;
        updateRangesList();
    }

    public Color getNonSignificantColor() {
        return nonSignificantColor;
    }

    public void setNonSignificantColor(Color nonSignificantColor) {
        this.nonSignificantColor = nonSignificantColor;
    }


    @Override
    public IAggregator defaultAggregator() {
        return SumAggregator.INSTANCE;
    }
}
