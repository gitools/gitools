/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package edu.upf.bg.colorscale.impl;

import edu.upf.bg.colorscale.ColorScaleRange;
import edu.upf.bg.colorscale.NumericColorScale;
import edu.upf.bg.colorscale.util.ColorConstants;
import edu.upf.bg.cutoffcmp.CutoffCmp;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


public class BinaryColorScale extends NumericColorScale {

	private String comparator;
    private double cutoff;
    
    private Color minColor;
    private Color maxColor;

    private static List<CutoffCmp> sharpCutoffs = new ArrayList<CutoffCmp>();
    static {
        sharpCutoffs.add(CutoffCmp.EQ);
        sharpCutoffs.add(CutoffCmp.NE);
        sharpCutoffs.add(CutoffCmp.ABS_EQ);
        sharpCutoffs.add(CutoffCmp.ABS_NE);
    }

	public BinaryColorScale(double cutoff, CutoffCmp cmp) {
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
        return new double[] { -edge, cutoff, edge };
    }

    @Override
    protected void updateRangesList() {

        ArrayList<ColorScaleRange> rangesList = getInternalScaleRanges();
        rangesList.clear();

        CutoffCmp positive = CutoffCmp.getFromName(comparator);
        CutoffCmp negative = CutoffCmp.getOpposite(positive);

        double outsideRange = Math.abs(cutoff) * 1.5;
        double insideRange = (sharpCutoffs.contains(positive)) ?
                            cutoff :
                            cutoff * 0.8;

        double positiveValue;
        double negativeValue;

        if (positive.compare(outsideRange,cutoff)) {
            negativeValue = insideRange;
            positiveValue = outsideRange;
        } else {
            negativeValue = outsideRange;
            positiveValue = insideRange;
        }

        ColorScaleRange positiveRange = new ColorScaleRange(positiveValue,positiveValue,3);
        positiveRange.setCenterLabel(positive.getShortName() + " " + Double.toString(cutoff));

        ColorScaleRange empty = new ColorScaleRange(0,0,1);
        empty.setType(ColorScaleRange.EMPTY_TYPE);
        empty.setBorderEnabled(false);

        ColorScaleRange negativeRange = new ColorScaleRange(negativeValue,negativeValue,3);
        negativeRange.setCenterLabel(negative.getShortName() + " " + Double.toString(cutoff));

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


}
