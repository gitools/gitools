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
import edu.upf.bg.colorscale.util.ColorConstants;

import java.awt.*;


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
        
        if (value > center) {
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

    public double getCenter() {
        return center;
    }

    public void setCenter(double center) {
        this.center = center;
    }

    public double getHalfAmplitude() {
        return halfAmplitude;
    }

    public void setHalfAmplitude(double halfAmplitude) {
        this.halfAmplitude = halfAmplitude;
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
    }

    public Color getLeftMaxColor() {
        return leftMaxColor;
    }

    public void setLeftMaxColor(Color leftMaxColor) {
        this.leftMaxColor = leftMaxColor;
    }

    public Color getRightMinColor() {
        return rightMinColor;
    }

    public void setRightMinColor(Color rightMinColor) {
        this.rightMinColor = rightMinColor;
    }

    public Color getRightMaxColor() {
        return rightMaxColor;
    }

    public void setRightMaxColor(Color rightMaxColor) {
        this.rightMaxColor = rightMaxColor;
    }

    public Color getNonSignificantColor() {
        return nonSignificantColor;
    }

    public void setNonSignificantColor(Color nonSignificantColor) {
        this.nonSignificantColor = nonSignificantColor;
    }
}
