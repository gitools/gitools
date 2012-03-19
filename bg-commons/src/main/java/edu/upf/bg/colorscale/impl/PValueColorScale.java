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

import edu.upf.bg.color.utils.ColorUtils;
import edu.upf.bg.colorscale.NumericColorScale;

import java.awt.Color;

import edu.upf.bg.colorscale.util.ColorConstants;

public class PValueColorScale extends NumericColorScale {
	
	public static final double defaultLogFactor = 0.25;

	private double significanceLevel;

    private Color minColor;
    private Color maxColor;
    private Color nonSignificantColor;
	
	public PValueColorScale(
			double significanceLevel, 
			Color minColor, 
			Color maxColor,
			Color nonSignificantColor) {
		
		super();
		
		this.significanceLevel = significanceLevel;
        this.maxColor = maxColor;
        this.minColor = minColor;
        this.nonSignificantColor = nonSignificantColor;
	}
	
	public PValueColorScale() {
		this(0.05, 
				ColorConstants.minColor,
				ColorConstants.maxColor,
				ColorConstants.nonSignificantColor);
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

    @Override
    public double[] getPoints() {
        return new double[] { 0, significanceLevel, 1 } ;
    }

    public double getSignificanceLevel() {
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
}
