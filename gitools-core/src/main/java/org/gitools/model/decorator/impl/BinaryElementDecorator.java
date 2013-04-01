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
import org.gitools.utils.colorscale.impl.BinaryColorScale;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.formatter.GenericFormatter;

import java.awt.*;

public class BinaryElementDecorator extends ElementDecorator
{

    private static final long serialVersionUID = 8832886601133057329L;

    private int valueIndex;

    private BinaryColorScale scale;

    private final static GenericFormatter fmt = new GenericFormatter("<");

    public BinaryElementDecorator()
    {
        this(null);
    }

    public BinaryElementDecorator(IElementAdapter adapter)
    {
        super(adapter);

        valueIndex = getPropertyIndex(new String[]{"value"});

        this.scale = new BinaryColorScale();
    }

    public int getValueIndex()
    {
        return valueIndex;
    }

    public void setValueIndex(int valueIndex)
    {
        int old = this.valueIndex;
        this.valueIndex = valueIndex;
        firePropertyChange(PROPERTY_CHANGED, old, valueIndex);
    }

    public CutoffCmp getCutoffCmp()
    {
        return CutoffCmp.getFromName(scale.getComparator());
    }

    public void setCutoffCmp(CutoffCmp cutoffCmp)
    {
        CutoffCmp old = getCutoffCmp();
        this.scale.setComparator(cutoffCmp.getShortName());
        firePropertyChange(PROPERTY_CHANGED, old, cutoffCmp);
    }

    public double getCutoff()
    {
        return scale.getCutoff();
    }

    public void setCutoff(double cutoff)
    {
        double old = scale.getCutoff();
        this.scale.setCutoff(cutoff);
        firePropertyChange(PROPERTY_CHANGED, old, cutoff);
    }

    public Color getColor()
    {
        return scale.getMaxColor();
    }

    public void setColor(Color color)
    {
        Color old = scale.getMaxColor();
        this.scale.setMaxColor(color);
        firePropertyChange(PROPERTY_CHANGED, old, color);
    }

    public Color getNonSignificantColor()
    {
        return scale.getMinColor();
    }

    public void setNonSignificantColor(Color color)
    {
        Color old = scale.getMinColor();
        this.scale.setMinColor(color);
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
            ElementDecoration decoration,
            Object element)
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

        /*if (element == null) {
              decoration.setBgColor(ColorConstants.emptyColor);
              decoration.setToolTip("Empty cell");
              return;
          }

          Object value = adapter.getValue(element, valueIndex);

          double v = MatrixUtils.doubleValue(value);*/

        final Color c = scale.valueColor(v);

        decoration.setBgColor(c);
        decoration.setToolTip(fmt.format(v));
    }

    @Override
    public IColorScale getScale()
    {
        return scale;
    }
}
