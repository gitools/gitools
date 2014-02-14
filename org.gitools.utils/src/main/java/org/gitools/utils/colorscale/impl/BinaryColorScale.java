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
import org.gitools.utils.colorscale.ColorConstants;
import org.gitools.utils.colorscale.ColorScaleRange;
import org.gitools.utils.colorscale.NumericColorScale;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class BinaryColorScale extends NumericColorScale {

    private String comparator;
    private double cutoff;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color minColor;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color maxColor;


    private static final List<CutoffCmp> equalCmp = new ArrayList<>();

    static {
        equalCmp.add(CutoffCmp.EQ);
        equalCmp.add(CutoffCmp.ABS_EQ);
    }


    private static final List<CutoffCmp> notequalCmp = new ArrayList<>();

    {
        notequalCmp.add(CutoffCmp.NE);
        notequalCmp.add(CutoffCmp.ABS_NE);
    }


    private static final List<CutoffCmp> absoluteCmp = new ArrayList<>();

    {
        absoluteCmp.add(CutoffCmp.ABS_GE);
        absoluteCmp.add(CutoffCmp.ABS_LE);
        absoluteCmp.add(CutoffCmp.ABS_EQ);
        absoluteCmp.add(CutoffCmp.ABS_GT);
        absoluteCmp.add(CutoffCmp.ABS_LT);
        absoluteCmp.add(CutoffCmp.ABS_NE);
    }

    private BinaryColorScale(double cutoff, CutoffCmp cmp) {
        super();

        this.comparator = cmp.getShortName();
        this.cutoff = cutoff;

    }

    public BinaryColorScale() {
        this(1.0, CutoffCmp.EQ);
    }

    @Override
    protected Color colorOf(double value) {
        boolean satisfies = CutoffCmp.getFromName(comparator).compare(value, cutoff);
        return satisfies ? getMaxColor() : getMinColor();
    }


    @Override
    public double[] getPoints() {
        double edge = Math.abs(cutoff) * 1.5;
        if (edge < 0.5) {
            edge = 0.5;
        }
        return new double[]{-edge, cutoff, edge};
    }

    @Override
    protected void updateRangesList() {

        ArrayList<ColorScaleRange> rangesList = getInternalScaleRanges();
        rangesList.clear();

        CutoffCmp positiveCmp = CutoffCmp.getFromName(comparator);
        CutoffCmp negativeCmp = CutoffCmp.getOpposite(positiveCmp);


        double outsideRange;
        double insideRange;
        double cutoffForRanges;

        if (absoluteCmp.contains(positiveCmp)) {
            cutoffForRanges = Math.abs(cutoff);
        } else {
            cutoffForRanges = cutoff;
        }

        if (cutoffForRanges != Double.POSITIVE_INFINITY && cutoffForRanges != Double.NEGATIVE_INFINITY) {
            outsideRange = (cutoffForRanges == 0.0) ? Math.abs(cutoffForRanges) + 0.5 : cutoffForRanges * 2.5;


            if (cutoffForRanges == 0.0) {
                insideRange = -0.8;
            } else {
                insideRange = cutoffForRanges - cutoffForRanges * 0.5;
            }


        } else if (cutoffForRanges == Double.POSITIVE_INFINITY) {
            outsideRange = 0.0;
            insideRange = Double.POSITIVE_INFINITY;
        } else {
            outsideRange = 0.0;
            insideRange = Double.NEGATIVE_INFINITY;
        }


        double positiveValue;
        double negativeValue;

        if (positiveCmp.compare(outsideRange, cutoffForRanges)) {
            negativeValue = insideRange;
            positiveValue = outsideRange;
        } else {
            negativeValue = outsideRange;
            positiveValue = insideRange;
        }

        if (!positiveCmp.compare(insideRange, cutoffForRanges) && equalCmp.contains(positiveCmp)) {
            positiveValue = cutoffForRanges;
        } else if (positiveCmp.compare(negativeValue, cutoffForRanges) && notequalCmp.contains(positiveCmp)) {
            negativeValue = cutoffForRanges;
        }

        ColorScaleRange positiveRange = new ColorScaleRange(positiveValue, positiveValue, 3);
        positiveRange.setType(ColorScaleRange.CONSTANT_TYPE);
        positiveRange.setCenterLabel(positiveCmp.getShortName() + " " + Double.toString(cutoffForRanges));

        ColorScaleRange empty = new ColorScaleRange(0, 0, 1);
        empty.setType(ColorScaleRange.EMPTY_TYPE);
        empty.setBorderEnabled(false);

        ColorScaleRange negativeRange = new ColorScaleRange(negativeValue, negativeValue, 3);
        negativeRange.setType(ColorScaleRange.CONSTANT_TYPE);
        negativeRange.setCenterLabel(negativeCmp.getShortName() + " " + Double.toString(cutoffForRanges));

        rangesList.add(empty);
        rangesList.add(positiveRange);
        rangesList.add(empty);
        rangesList.add(negativeRange);
        rangesList.add(empty);

    }

    public Color getMinColor() {
        if (minColor == null) {
            return ColorConstants.binaryMinColor;
        }
        return minColor;
    }

    public void setMinColor(Color minColor) {
        this.minColor = minColor;
    }

    public Color getMaxColor() {
        if (maxColor == null) {
            return ColorConstants.binaryMaxColor;
        }
        return maxColor;
    }

    public void setMaxColor(Color maxColor) {
        this.maxColor = maxColor;
    }

    public String getComparator() {
        return comparator;
    }

    public void setComparator(String comparator) {
        this.comparator = comparator;
        updateRangesList();
    }

    public double getCutoff() {
        return cutoff;
    }

    public void setCutoff(double cutoff) {
        this.cutoff = cutoff;
        updateRangesList();
    }


    @Override
    public IAggregator defaultAggregator() {
        return SumAggregator.INSTANCE;
    }
}
