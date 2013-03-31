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

package org.gitools.model.decorator.impl;

import org.gitools.utils.colorscale.ColorScalePoint;
import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.colorscale.impl.CategoricalColorScale;
import org.gitools.utils.formatter.GenericFormatter;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;

import java.awt.*;

public class CategoricalElementDecorator extends ElementDecorator {

	private static final long serialVersionUID = -181427286948958314L;

	private int valueIndex;

	private CategoricalColorScale scale;

	private final static GenericFormatter fmt = new GenericFormatter("<");

	public CategoricalElementDecorator(double[] points) {
		this(null, new CategoricalColorScale(points));
	}

	public CategoricalElementDecorator(IElementAdapter adapter) {
		this(adapter, new CategoricalColorScale());
	}

	public CategoricalElementDecorator(IElementAdapter adapter, CategoricalColorScale scale) {
		super(adapter);
		
		valueIndex = getPropertyIndex(new String[] {
				"value", "log2ratio", "score" });
		
		this.scale = scale;
	}

	public final int getValueIndex() {
		return valueIndex;
	}

	public final void setValueIndex(int valueIndex) {
		int old = this.valueIndex;
		this.valueIndex = valueIndex;
		firePropertyChange(PROPERTY_CHANGED, old, valueIndex);
	}

	public final double getMinValue() {
		return scale.getMin().getValue();
	}

	public final double getMaxValue() {
		return scale.getMax().getValue();
	}

	public final Color getMinColor() {
		return scale.getMin().getColor();
	}

	public final Color getMaxColor() {
		return scale.getMax().getColor();
	}

	public final void setColor(double value,Color valueColor) {
        if (scale.getColorScalePoint(value)!=null) {
            Color old = scale.getColorScalePoint(value).getColor();
            scale.getColorScalePoint(value).setColor(valueColor);
            firePropertyChange(PROPERTY_CHANGED, old, valueColor);
        }
	}

	public Color getEmptyColor() {
		return scale.getEmptyColor();
	}
	
	public void setEmptyColor(Color color) {
		Color old = scale.getEmptyColor();
		scale.setEmptyColor(color);
		firePropertyChange(PROPERTY_CHANGED, old, color);
	}

	@Override
	public void decorate(ElementDecoration decoration, Object element) {
		decoration.reset();

		Object value = element != null ? adapter.getValue(element, valueIndex) : Double.NaN;

		double v = MatrixUtils.doubleValue(value);

		if (element == null || Double.isNaN(v)) {
			decoration.setBgColor(scale.getEmptyColor());
			decoration.setToolTip("Empty cell");
			return;
		}

		final Color color = scale.valueColor(v);
		
		decoration.setBgColor(color);
		decoration.setToolTip(fmt.pvalue(v));
	}

    public void setCategories(ColorScalePoint[] newScalePoints) {
        ColorScalePoint[] old = scale.getPointObjects();
        scale.setPointObjects(newScalePoints);
        firePropertyChange(PROPERTY_CHANGED, old, newScalePoints);
    }
    
    public int getCategoriesCount() {
        return scale.getPoints().length;
    }

	@Override
	public IColorScale getScale() {
		return scale;
	}

    public boolean isCagetoricalSpans() {
        return scale.isCagetoricalSpans();
    }

    public void setCagetoricalSpans(boolean cagetoricalSpans) {
        scale.setCagetoricalSpans(cagetoricalSpans);
    }

}
