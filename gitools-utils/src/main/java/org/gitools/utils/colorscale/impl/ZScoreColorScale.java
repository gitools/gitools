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

package org.gitools.utils.colorscale.impl;

import org.gitools.utils.aggregation.IAggregator;
import org.gitools.utils.aggregation.SumAggregator;
import org.gitools.utils.color.utils.ColorUtils;
import org.gitools.utils.colorscale.ColorScaleRange;
import org.gitools.utils.colorscale.NumericColorScale;
import org.gitools.utils.colorscale.util.ColorConstants;

import java.awt.*;
import java.util.ArrayList;


public class ZScoreColorScale extends NumericColorScale {

    private double center;
    private double halfAmplitude;
    private double sigHalfAmplitude;

    private Color leftMinColor;
    private Color leftMaxColor;
    private Color rightMinColor;
    private Color rightMaxColor;
    private Color nonSignificantColor;

    public ZScoreColorScale(double center, double halfAmplitude, double sigHalfAmplitude, Color leftMinColor, Color leftMaxColor, Color rightMinColor, Color rightMaxColor, Color nonSignificantColor) {
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
        this(
                0, 10, 1.96,
                Color.BLUE, Color.CYAN,
                Color.YELLOW, Color.RED,
                ColorConstants.nonSignificantColor);
    }

    @Override
    protected Color colorOf(double value) {
        
        double absValue = Math.abs(value);
        
        if (absValue < sigHalfAmplitude) {
            return nonSignificantColor;
        }

        double f = (absValue - sigHalfAmplitude) / (halfAmplitude - sigHalfAmplitude);


        if (value > getMaxValue())
            return rightMaxColor;
        else if (value < getMinValue())
            return leftMinColor;
        else if (value > center) {
            return ColorUtils.mix(rightMaxColor, rightMinColor, f);
        } else {
            return ColorUtils.mix(leftMinColor, leftMaxColor, f);
        }

    }

    @Override
    public double[] getPoints() {
        return new double[] {
                center - halfAmplitude,
                center - sigHalfAmplitude,
                center + sigHalfAmplitude,
                center + halfAmplitude
        };
    }

    @Override
    protected void updateRangesList() {

        ArrayList<ColorScaleRange> rangesList = getInternalScaleRanges();
        rangesList.clear();

        ColorScaleRange left = new ColorScaleRange(getMinValue(),-getSigHalfAmplitude(),-getSigHalfAmplitude()-getMinValue());
        ColorScaleRange middle = new ColorScaleRange(-getSigHalfAmplitude(),getSigHalfAmplitude()-0.01,getSigHalfAmplitude()*2);
        ColorScaleRange right = new ColorScaleRange(getSigHalfAmplitude(),getMaxValue(),getMaxValue()-getSigHalfAmplitude());
        
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
