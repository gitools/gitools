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
package org.gitools.utils.colorscale;

import org.gitools.utils.color.Colors;
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
public abstract class NumericColorScale implements IColorScale, IColorScaleHtml {

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color notANumberColor;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color posInfinityColor;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color negInfinityColor;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color emptyColor;

    private ArrayList<ColorScaleRange> rangesList;

    protected NumericColorScale() {
        super();
        rangesList = new ArrayList<ColorScaleRange>();
    }


    @Override
    public Color valueColor(double value) {
        Color color = limitsColor(value);
        if (color != null) {
            return color;
        }

        return colorOf(value);
    }

    protected abstract Color colorOf(double value);


    protected abstract double[] getPoints();

    public double getMinValue() {
        return getPoints()[0];
    }

    public double getMaxValue() {
        return getPoints()[getPoints().length - 1];
    }


    @Override
    public String valueRGBHtmlColor(double value) {
        Color color = valueColor(value);
        return Colors.colorToRGBHtml(color);
    }


    @Override
    public String valueHexHtmlColor(double value) {
        Color color = valueColor(value);
        return Colors.colorToHexHtml(color);
    }


    Color limitsColor(double value) {
        if (Double.isNaN(value)) {
            return getNotANumberColor();
        }
        //        else if (value == Double.POSITIVE_INFINITY)
        //            return getPosInfinityColor();
        //        else if (value == Double.NEGATIVE_INFINITY)
        //            return getNegInfinityColor();

        return null;
    }

    Color getNotANumberColor() {
        if (notANumberColor == null) {
            return ColorConstants.notANumberColor;
        }
        return notANumberColor;
    }

    public void setNotANumberColor(Color notANumberColor) {
        this.notANumberColor = notANumberColor;
    }

    public Color getPosInfinityColor() {
        if (posInfinityColor == null) {
            return ColorConstants.posInfinityColor;
        }
        return posInfinityColor;
    }

    public void setPosInfinityColor(Color posInfinityColor) {
        this.posInfinityColor = posInfinityColor;
    }

    public Color getNegInfinityColor() {
        if (negInfinityColor == null) {
            return ColorConstants.negInfinityColor;
        }
        return negInfinityColor;
    }

    public void setNegInfinityColor(Color negInfinityColor) {
        this.negInfinityColor = negInfinityColor;
    }

    public Color getEmptyColor() {
        if (emptyColor == null) {
            return ColorConstants.emptyColor;
        }
        return emptyColor;
    }

    public void setEmptyColor(Color emptyColor) {
        this.emptyColor = emptyColor;
    }

    public final ArrayList<ColorScaleRange> getScaleRanges() {

        updateRangesList();

        return rangesList;
    }

    protected ArrayList<ColorScaleRange> getInternalScaleRanges() {
        if (rangesList == null) {
            rangesList = new ArrayList<ColorScaleRange>();
        }
        return rangesList;
    }

    protected void updateRangesList() {
        rangesList.clear();

        double[] points = getPoints();
        double min = Double.NaN;
        double max = Double.NaN;
        for (int i = 0; i < points.length; i++) {
            min = max;
            max = points[i];

            if (Double.isNaN(min)) {
                continue;
            }

            Object leftLabel = (i == 1) ? min : "";
            Object rightLabel = max;
            ColorScaleRange range = new ColorScaleRange(min, max, (max - min));
            range.setLeftLabel(leftLabel);
            range.setRightLabel(rightLabel);

            rangesList.add(range);
        }
    }
}
