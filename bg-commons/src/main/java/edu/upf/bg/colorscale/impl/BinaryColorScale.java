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

import edu.upf.bg.colorscale.NumericColorScale;
import edu.upf.bg.colorscale.util.ColorConstants;
import edu.upf.bg.cutoffcmp.CutoffCmp;
import java.awt.Color;


public class BinaryColorScale extends NumericColorScale {

	private String comparator;
    private double cutoff;
    
    private Color minColor;
    private Color maxColor;

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
    }

    public double getCutoff() {
        return cutoff;
    }

    public void setCutoff(double cutoff) {
        this.cutoff = cutoff;
    }


}
