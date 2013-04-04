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

import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.colorscale.impl.PValueColorScale;
import org.gitools.utils.formatter.GenericFormatter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class PValueElementDecorator extends ElementDecorator
{

    private static final long serialVersionUID = -1215192981017862718L;

    private int valueIndex;
    private int correctedValueIndex;
    private boolean useCorrection;
    private double significanceLevel;

    private PValueColorScale scale;

    private final static GenericFormatter fmt = new GenericFormatter("<");

    public PValueElementDecorator()
    {

        valueIndex = getPropertyIndex(new String[]{
                "right-p-value", "p-value"});

        correctedValueIndex = getPropertyIndex(new String[]{
                "corrected-right-p-value", "corrected-p-value"});

        useCorrection = false;

        significanceLevel = 0.05;
        scale = new PValueColorScale();
    }

    public PValueElementDecorator(IElementAdapter adapter)
    {
        super(adapter);

        valueIndex = getPropertyIndex(new String[]{
                "right-p-value", "p-value"});

        correctedValueIndex = getPropertyIndex(new String[]{
                "corrected-right-p-value", "corrected-p-value"});

        useCorrection = false;

        significanceLevel = 0.05;
        scale = new PValueColorScale();
    }

    @Override
    public final int getValueIndex()
    {
        return valueIndex;
    }

    @Override
    public final void setValueIndex(int valueIndex)
    {
        int old = this.valueIndex;
        this.valueIndex = valueIndex;
        firePropertyChange(PROPERTY_CHANGED, old, valueIndex);
    }

    public final int getCorrectedValueIndex()
    {
        return correctedValueIndex;
    }

    public final void setCorrectedValueIndex(int correctedValueIndex)
    {
        int old = this.correctedValueIndex;
        this.correctedValueIndex = correctedValueIndex;
        firePropertyChange(PROPERTY_CHANGED, old, correctedValueIndex);
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

    public final double getSignificanceLevel()
    {
        return significanceLevel;
    }

    public final void setSignificanceLevel(double significanceLevel)
    {
        double old = this.significanceLevel;
        this.significanceLevel = significanceLevel;
        scale.setSignificanceLevel(significanceLevel);
        firePropertyChange(PROPERTY_CHANGED, old, significanceLevel);
    }

    public final PValueColorScale getPValueScale()
    {
        return scale;
    }

    public Color getMinColor()
    {
        return scale.getMinColor();
    }

    public void setMinColor(Color color)
    {
        Color old = scale.getMinColor();
        scale.setMinColor(color);
        firePropertyChange(PROPERTY_CHANGED, old, color);
    }

    public Color getMaxColor()
    {
        return scale.getMaxColor();
    }

    public void setMaxColor(Color color)
    {
        Color old = scale.getMaxColor();
        scale.setMaxColor(color);
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
    public void decorate(
            @NotNull ElementDecoration decoration,
            @Nullable Object element)
    {

        decoration.reset();

		/*if (element == null) {
            decoration.setBgColor(ColorConstants.emptyColor);
			decoration.setToolTip("Empty cell");
			return;
		}*/

        //Object value = adapter.getValue(element, valueIndex);

        Object value = element != null ? adapter.getValue(element, valueIndex) : Double.NaN;

        double v = MatrixUtils.doubleValue(value);

        if (element == null || Double.isNaN(v))
        {
            decoration.setBgColor(scale.getEmptyColor());
            decoration.setToolTip("Empty cell");
            return;
        }

        boolean isSig = v <= significanceLevel;

        if (useCorrection)
        {
            Object corrValue = correctedValueIndex >= 0 ?
                    adapter.getValue(element, correctedValueIndex) : 0.0;

            double cv = MatrixUtils.doubleValue(corrValue);

            isSig = cv <= significanceLevel;
        }

        final Color color = isSig ? scale.valueColor(v)
                : scale.getNonSignificantColor();

        decoration.setBgColor(color);
        decoration.setToolTip(fmt.pvalue(v));
    }

    @Override
    public IColorScale getScale()
    {
        return scale;
    }
}
