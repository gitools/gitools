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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @noinspection ALL
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ColorScaleRange
{

    @NotNull
    public static final String CONSTANT_TYPE = "constant";
    @NotNull
    public static final String EMPTY_TYPE = "empty";
    @NotNull
    public static final String LINEAR_TYPE = "linear";
    @NotNull
    public static final String LOGARITHMIC_TYPE = "logarithmic";

    private String type = "";
    @Nullable
    private Object leftLabel = null;
    @Nullable
    private Object centerLabel = null;
    @Nullable
    private Object rightLabel = null;
    private double width;
    private double minValue;
    private double maxValue;

    private boolean borderEnabled = true;

    public ColorScaleRange()
    {
        super();
        // JAXB requirement
    }

    public ColorScaleRange(double minValue, double maxValue, double width)
    {
        this(minValue, maxValue, width, null, null, null, LINEAR_TYPE);
    }

    public ColorScaleRange(double minValue, double maxValue, double width, Object leftLabel, Object centerLabel, Object rightLabel, String type)
    {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.width = width;
        this.leftLabel = leftLabel;
        this.rightLabel = rightLabel;
        this.centerLabel = centerLabel;
        this.type = type;

    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Nullable
    public Object getLeftLabel()
    {
        return leftLabel;
    }

    public void setLeftLabel(Object leftLabel)
    {
        this.leftLabel = leftLabel;
    }

    @Nullable
    public Object getCenterLabel()
    {
        return centerLabel;
    }

    public void setCenterLabel(Object centerLabel)
    {
        this.centerLabel = centerLabel;
    }

    @Nullable
    public Object getRightLabel()
    {
        return rightLabel;
    }

    public void setRightLabel(Object rightLabel)
    {
        this.rightLabel = rightLabel;
    }

    public double getWidth()
    {
        return width;
    }

    public void setWidth(double width)
    {
        this.width = width;
    }

    public double getMinValue()
    {
        return minValue;
    }

    public void setMinValue(double minValue)
    {
        this.minValue = minValue;
    }

    public double getMaxValue()
    {
        return maxValue;
    }

    public void setMaxValue(double maxValue)
    {
        this.maxValue = maxValue;
    }

    public boolean isBorderEnabled()
    {
        return borderEnabled;
    }

    public void setBorderEnabled(boolean borderEnabled)
    {
        this.borderEnabled = borderEnabled;
    }
}
