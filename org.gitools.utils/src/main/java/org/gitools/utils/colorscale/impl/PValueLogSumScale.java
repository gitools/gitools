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

import org.gitools.utils.color.Colors;
import org.gitools.utils.colorscale.ColorConstants;
import org.gitools.utils.colorscale.ColorScaleRange;
import org.gitools.utils.colorscale.NumericColorScale;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.ArrayList;

@XmlAccessorType(XmlAccessType.FIELD)
public class PValueLogSumScale extends NumericColorScale {

    private double center;

    private double limit;

    private double significanceLimit;

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

    private PValueLogSumScale(double center, double limit, double significanceLimit, Color leftMinColor, Color leftMaxColor, Color rightMinColor, Color rightMaxColor, Color nonSignificantColor) {
        this.center = center;
        this.limit = limit;
        this.significanceLimit = significanceLimit;
        this.leftMinColor = leftMinColor;
        this.leftMaxColor = leftMaxColor;
        this.rightMinColor = rightMinColor;
        this.rightMaxColor = rightMaxColor;
        this.nonSignificantColor = nonSignificantColor;
    }

    public PValueLogSumScale() {
        this(0, 10, 1.278754, Color.BLUE, Color.CYAN, Color.YELLOW, Color.RED, ColorConstants.nonSignificantColor);
    }

    @Override
    protected Color colorOf(double value) {

        double absValue = Math.abs(value);

        if (absValue < significanceLimit) {
            return nonSignificantColor;
        }

        double f = (absValue - significanceLimit) / (limit - significanceLimit);


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
        return new double[]{center - limit, center - significanceLimit, center + significanceLimit, center + limit};
    }

    @Override
    protected void updateRangesList() {

        ArrayList<ColorScaleRange> rangesList = getInternalScaleRanges();
        rangesList.clear();

        ColorScaleRange left = new ColorScaleRange(getMinValue(), -getSignificanceLimit(), -getSignificanceLimit() - getMinValue());
        ColorScaleRange middle = new ColorScaleRange(-getSignificanceLimit(), getSignificanceLimit() - 0.01, getSignificanceLimit() * 2);
        ColorScaleRange right = new ColorScaleRange(getSignificanceLimit(), getMaxValue(), getMaxValue() - getSignificanceLimit());

        left.setLeftLabel(getMinValue());
        left.setRightLabel(-getSignificanceLimit());
        middle.setType(ColorScaleRange.CONSTANT_TYPE);
        right.setLeftLabel(getSignificanceLimit());
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

    public void setLimit(double limit) {
        this.limit = limit;
        updateRangesList();
    }

    public double getSignificanceLimit() {
        return significanceLimit;
    }

    public void setSignificanceLimit(double significanceLimit) {
        this.significanceLimit = significanceLimit;
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

    public double getLimit() {
        return limit;
    }
}
