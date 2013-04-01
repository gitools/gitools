/*
 * #%L
 * gitools-core
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
package org.gitools.model.decorator.impl;

import cern.jet.stat.Probability;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.colorscale.impl.ZScoreColorScale;
import org.gitools.utils.colorscale.util.ColorConstants;
import org.gitools.utils.formatter.GenericFormatter;

import java.awt.*;

public class ZScoreElementDecorator extends ElementDecorator
{

    private static final long serialVersionUID = -7623938918947195891L;

    private int valueIndex;
    private int correctedValueIndex;
    private boolean useCorrection;
    private double significanceLevel;

    private ZScoreColorScale scale;

    private final static GenericFormatter fmt = new GenericFormatter("<");


    public ZScoreElementDecorator()
    {

        valueIndex = getPropertyIndex(new String[]{"z-score"});
        correctedValueIndex = getPropertyIndex(new String[]{
                "corrected-two-tail-p-value", "corrected-p-value"});

        useCorrection = false;
        significanceLevel = 0.05;

        scale = new ZScoreColorScale();
    }


    public ZScoreElementDecorator(IElementAdapter adapter)
    {
        super(adapter);

        valueIndex = getPropertyIndex(new String[]{"z-score"});
        correctedValueIndex = getPropertyIndex(new String[]{
                "corrected-two-tail-p-value", "corrected-p-value"});

        useCorrection = false;
        significanceLevel = 0.05;

        scale = new ZScoreColorScale();
    }

    @Override
    public final int getValueIndex()
    {
        return valueIndex;
    }

    @Override
    public void setValueIndex(int valueIndex)
    {
        int old = this.valueIndex;
        this.valueIndex = valueIndex;
        firePropertyChange(PROPERTY_CHANGED, old, valueIndex);
    }

    public int getCorrectedValueIndex()
    {
        return correctedValueIndex;
    }

    public void setCorrectedValueIndex(int correctionValueIndex)
    {
        int old = this.correctedValueIndex;
        this.correctedValueIndex = correctionValueIndex;
        firePropertyChange(PROPERTY_CHANGED, old, correctionValueIndex);
    }

    public final boolean getUseCorrection()
    {
        return useCorrection;
    }

    public final void setUseCorrection(boolean useCorrection)
    {
        boolean old = this.useCorrection;
        this.useCorrection = useCorrection;
        firePropertyChange(PROPERTY_CHANGED, old, useCorrection);
    }

    public double getSignificanceLevel()
    {
        return significanceLevel;
    }

    public void setSignificanceLevel(double sigLevel)
    {
        double old = this.significanceLevel;
        this.significanceLevel = sigLevel;
        setSigHalfAmplitude(calculateSigHalfAmplitudeFromSigLevel(sigLevel));
        firePropertyChange(PROPERTY_CHANGED, old, sigLevel);
    }

    private double calculateSigHalfAmplitudeFromSigLevel(double sigLevel)
    {
        double v = Probability.normalInverse(sigLevel / 2);
        return Math.abs(v);
    }

    public final double getSigHalfAmplitude()
    {
        return scale.getSigHalfAmplitude();
    }

    public final void setSigHalfAmplitude(double sigHalfAmplitude)
    {
        double old = scale.getSigHalfAmplitude();
        scale.setSigHalfAmplitude(sigHalfAmplitude);
        firePropertyChange(PROPERTY_CHANGED, old, sigHalfAmplitude);
    }

    public final ZScoreColorScale getZScoreScale()
    {
        return scale;
    }

    public final void setScale(ZScoreColorScale scale)
    {
        ZScoreColorScale old = this.scale;
        this.scale = scale;
        firePropertyChange(PROPERTY_CHANGED, old, scale);
    }

    public Color getLeftMinColor()
    {
        return scale.getLeftMinColor();
    }

    public void setLeftMinColor(Color color)
    {
        Color old = scale.getLeftMinColor();
        scale.setLeftMinColor(color);
        firePropertyChange(PROPERTY_CHANGED, old, color);
    }

    public Color getLeftMaxColor()
    {
        return scale.getLeftMaxColor();
    }

    public void setLeftMaxColor(Color color)
    {
        Color old = scale.getLeftMaxColor();
        scale.setLeftMaxColor(color);
        firePropertyChange(PROPERTY_CHANGED, old, color);
    }

    public Color getRightMinColor()
    {
        return scale.getRightMinColor();
    }

    public void setRightMinColor(Color color)
    {
        Color old = scale.getRightMinColor();
        scale.setRightMinColor(color);
        firePropertyChange(PROPERTY_CHANGED, old, color);
    }

    public Color getRightMaxColor()
    {
        return scale.getRightMaxColor();
    }

    public void setRightMaxColor(Color color)
    {
        Color old = scale.getRightMaxColor();
        scale.setRightMaxColor(color);
        firePropertyChange(PROPERTY_CHANGED, old, color);
    }

    public Color getNonSignificantColor()
    {
        return scale.getNonSignificantColor();
    }

    public void setNonSignificantColor(Color color)
    {
        Color old = scale.getNonSignificantColor();
        scale.setNonSignificantColor(color);
        firePropertyChange(PROPERTY_CHANGED, old, color);
    }

    public Color getEmptyColor()
    {
        return scale.getEmptyColor();
    }

    public void setEmptyColor(Color color)
    {
        Color old = scale.getEmptyColor();
        scale.setEmptyColor(color);
        firePropertyChange(PROPERTY_CHANGED, old, color);
    }

    @Override
    public void decorate(ElementDecoration decoration, Object element)
    {
        decoration.reset();

        Object value = element != null ? adapter.getValue(element, valueIndex) : Double.NaN;

        double v = MatrixUtils.doubleValue(value);

        if (element == null || Double.isNaN(v))
        {
            decoration.setBgColor(scale.getEmptyColor());
            decoration.setToolTip("Empty cell");
            return;
        }

        boolean useScale = true;

        if (useCorrection)
        {
            Object corrValue = correctedValueIndex >= 0 ?
                    adapter.getValue(element, correctedValueIndex) : 0.0;

            double cv = MatrixUtils.doubleValue(corrValue);

            useScale = cv <= significanceLevel;
        }

        final Color color = useScale ? scale.valueColor(v)
                : ColorConstants.nonSignificantColor;

        decoration.setBgColor(color);
        decoration.setToolTip(fmt.pvalue(v));
    }

    @Override
    public IColorScale getScale()
    {
        return scale;
    }
}
