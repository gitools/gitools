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

import org.gitools.utils.aggregation.IAggregator;
import org.gitools.utils.aggregation.SumAggregator;
import org.gitools.utils.colorscale.ColorScalePoint;
import org.gitools.utils.colorscale.ColorScaleRange;
import org.gitools.utils.colorscale.NumericColorScale;
import org.gitools.utils.colorscale.util.ColorConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @noinspection ALL
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoricalColorScale extends NumericColorScale
{

    private ColorScalePoint[] points;
    private boolean cagetoricalSpans = true;

    public CategoricalColorScale()
    {
        this(new double[]{1.0, 2.0});
    }

    public CategoricalColorScale(ColorScalePoint[] points)
    {
        super();
        this.points = points;
    }

    public CategoricalColorScale(@NotNull double pointValues[])
    {

        setValues(pointValues);
    }

    public void setValues(@NotNull double[] pointValues)
    {
        Arrays.sort(pointValues);
        this.points = new ColorScalePoint[pointValues.length];


        Color[] palette = generateColors(pointValues.length);
        for (int i = 0; i < pointValues.length; i++)
        {
            this.points[i] = new ColorScalePoint(pointValues[i], palette[i]);
        }

        updateRangesList();
    }

    @NotNull
    Color[] generateColors(int n)
    {
        Color[] cols = new Color[n];

        if (n == 2)
        {
            cols[0] = ColorConstants.binaryMinColor;
            cols[1] = ColorConstants.binaryMaxColor;
            return cols;
        }
        else if (n == 3)
        {
            cols[0] = Color.BLUE;
            cols[1] = Color.green.darker();
            cols[2] = Color.RED;
            return cols;
        }

        for (int i = 0; i < n; i++)
        {
            cols[i] = Color.getHSBColor((float) i / (float) n, 0.85f, 1.0f);
        }
        return cols;
    }

    @Nullable
    public ColorScalePoint getColorScalePoint(double value)
    {
        for (ColorScalePoint p : points)
        {
            if (cagetoricalSpans)
            {
                if (p.getValue() >= value)
                {
                    return p;
                }
            }
            else
            {
                if (p.getValue() == value)
                {
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    protected Color colorOf(double value)
    {

        Color c = super.getEmptyColor();
        ColorScalePoint firstPoint = getMin();
        for (ColorScalePoint p : points)
        {
            if (cagetoricalSpans)
            {
                if (p.getValue() >= value)
                {
                    return p.getColor();
                }
            }
            else
            {
                if (p.getValue() == value)
                {
                    return p.getColor();
                }
            }
        }
        return c;
    }

    @NotNull
    @Override
    public double[] getPoints()
    {
        double[] pointValues = new double[points.length];
        for (int i = 0; i < points.length; i++)
            pointValues[i] = points[i].getValue();

        return pointValues;
    }

    public ColorScalePoint getMin()
    {
        return points[0];
    }

    @Override
    public double getMinValue()
    {
        Double spectrum = points[points.length - 1].getValue() - points[0].getValue();
        Double step = spectrum / (points.length - 1);
        return points[0].getValue() - step;
    }

    public ColorScalePoint getMax()
    {
        ColorScalePoint last = points[points.length - 1];
        return last;
    }

    @Override
    public double getMaxValue()
    {
        return points[points.length - 1].getValue();
    }

    public boolean isCagetoricalSpans()
    {
        return cagetoricalSpans;
    }

    public void setCagetoricalSpans(boolean cagetoricalSpans)
    {
        this.cagetoricalSpans = cagetoricalSpans;
    }

    public ColorScalePoint[] getPointObjects()
    {
        return this.points;
    }

    public void setPointObjects(@NotNull ColorScalePoint[] points)
    {
        Arrays.sort(points);
        this.points = points;
        updateRangesList();
    }

    @Override
    protected void updateRangesList()
    {

        ArrayList<ColorScaleRange> rangesList = getInternalScaleRanges();
        rangesList.clear();

        for (ColorScalePoint p : points)
        {
            ColorScaleRange r = new ColorScaleRange(p.getValue(), p.getValue(), 10);
            if (p.getName().equals(""))
            {
                r.setCenterLabel(p.getValue());
            }
            else
            {
                r.setCenterLabel(p.getName());
            }
            r.setType(ColorScaleRange.CONSTANT_TYPE);
            rangesList.add(r);

            if (p != points[points.length - 1])
            {
                ColorScaleRange e = new ColorScaleRange(-1, -1, 1);
                e.setType(ColorScaleRange.EMPTY_TYPE);
                e.setBorderEnabled(false);
                rangesList.add(e);
            }
        }
    }

    @NotNull
    @Override
    public IAggregator defaultAggregator()
    {
        return SumAggregator.INSTANCE;
    }
}
