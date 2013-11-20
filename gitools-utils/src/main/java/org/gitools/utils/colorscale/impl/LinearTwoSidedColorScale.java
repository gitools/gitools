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
import org.gitools.utils.aggregation.MeanAggregator;
import org.gitools.utils.color.utils.ColorUtils;
import org.gitools.utils.colorscale.ColorScalePoint;
import org.gitools.utils.colorscale.ColorScaleRange;
import org.gitools.utils.colorscale.NumericColorScale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.awt.*;
import java.util.ArrayList;

/**
 * @noinspection ALL
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LinearTwoSidedColorScale extends NumericColorScale {


    private ColorScalePoint min;
    private ColorScalePoint mid;
    private ColorScalePoint max;

    LinearTwoSidedColorScale(ColorScalePoint min, ColorScalePoint mid, ColorScalePoint max) {
        super();
        this.min = min;
        this.mid = mid;
        this.max = max;
        updateRangesList();
    }

    public LinearTwoSidedColorScale() {
        this(new ColorScalePoint(-2, Color.GREEN), new ColorScalePoint(0, Color.WHITE), new ColorScalePoint(2, Color.RED));
    }

    @Override
    protected Color colorOf(double value) {

        if (value < min.getValue()) {
            return min.getColor();
        }

        if (value > max.getValue()) {
            return max.getColor();
        }

        if (value == mid.getValue()) {
            return mid.getColor();
        }

        if (value < mid.getValue()) {
            double f = (value - min.getValue()) / (mid.getValue() - min.getValue());
            return ColorUtils.mix(mid.getColor(), min.getColor(), f);
        } else {
            double f = (value - mid.getValue()) / (max.getValue() - mid.getValue());
            return ColorUtils.mix(max.getColor(), mid.getColor(), f);
        }

    }


    @Override
    public double[] getPoints() {
        return new double[]{min.getValue(), mid.getValue(), max.getValue()};
    }

    public ColorScalePoint getMin() {
        return min;
    }

    public void setMin(ColorScalePoint min) {
        this.min = min;
        updateRangesList();
    }

    public ColorScalePoint getMid() {
        return mid;
    }

    public void setMid(ColorScalePoint mid) {
        this.mid = mid;
        updateRangesList();
    }

    public ColorScalePoint getMax() {
        return max;
    }

    public void setMax(ColorScalePoint max) {
        this.max = max;
        updateRangesList();
    }

    @Override
    protected void updateRangesList() {

        ArrayList<ColorScaleRange> rangesList = getInternalScaleRanges();
        rangesList.clear();

        double[] points = getPoints();
        double min = getMinValue();
        double max = getMaxValue();
        double mid = getMid().getValue();

        if (min == mid) {
            rangesList.add(new ColorScaleRange(mid, max, 1, mid, "", max, ColorScaleRange.LINEAR_TYPE));
        } else if (mid == max) {
            rangesList.add(new ColorScaleRange(min, mid, 1, min, "", mid, ColorScaleRange.LINEAR_TYPE));
        } else {
            rangesList.add(new ColorScaleRange(min, mid, 1, min, "", mid, ColorScaleRange.LINEAR_TYPE));
            rangesList.add(new ColorScaleRange(mid, max, 1, "", "", max, ColorScaleRange.LINEAR_TYPE));
        }

    }


    @Override
    public IAggregator defaultAggregator() {
        return MeanAggregator.INSTANCE;
    }
}
