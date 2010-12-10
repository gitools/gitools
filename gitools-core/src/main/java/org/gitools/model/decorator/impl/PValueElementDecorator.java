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

import java.awt.Color;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.gitools.matrix.MatrixUtils;
import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.matrix.model.element.IElementAdapter;

import edu.upf.bg.formatter.GenericFormatter;
import edu.upf.bg.colorscale.IColorScale;
import edu.upf.bg.colorscale.impl.PValueColorScale;
import edu.upf.bg.colorscale.util.ColorConstants;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PValueElementDecorator extends ElementDecorator {

	private static final long serialVersionUID = -1215192981017862718L;

	private int valueIndex;
	private int correctedValueIndex;
	private boolean useCorrection;
	private double significanceLevel;
	
	private PValueColorScale scale;

	@XmlTransient
	private GenericFormatter fmt;
	
	public PValueElementDecorator() {
		
		valueIndex = getPropertyIndex(new String[] {
				"right-p-value", "p-value" });
		
		correctedValueIndex = getPropertyIndex(new String[] {
				"corrected-right-p-value", "corrected-p-value" });
		
		useCorrection = false;
		
		significanceLevel = 0.05;
		scale = new PValueColorScale();

		fmt = new GenericFormatter("<");
	}
	
	public PValueElementDecorator(IElementAdapter adapter) {
		super(adapter);
		
		valueIndex = getPropertyIndex(new String[] {
				"right-p-value", "p-value" });
		
		correctedValueIndex = getPropertyIndex(new String[] {
				"corrected-right-p-value", "corrected-p-value" });
		
		useCorrection = false;
		
		significanceLevel = 0.05;
		scale = new PValueColorScale();

		fmt = new GenericFormatter("<");
	}

	public final int getValueIndex() {
		return valueIndex;
	}

	public final void setValueIndex(int valueIndex) {
		int old = this.valueIndex;
		this.valueIndex = valueIndex;
		firePropertyChange(PROPERTY_CHANGED, old, valueIndex);
	}

	public final int getCorrectedValueIndex() {
		return correctedValueIndex;
	}

	public final void setCorrectedValueIndex(int correctedValueIndex) {
		int old = this.correctedValueIndex;
		this.correctedValueIndex = correctedValueIndex;
		firePropertyChange(PROPERTY_CHANGED, old, correctedValueIndex);
	}

	public final boolean getUseCorrection() {
		return useCorrection;
	}

	public final void setUseCorrection(boolean useCorrection) {
		boolean old = this.useCorrection;
		this.useCorrection = useCorrection;
		firePropertyChange(PROPERTY_CHANGED, old, useCorrection);
	}

	public final double getSignificanceLevel() {
		return significanceLevel;
	}

	public final void setSignificanceLevel(double significanceLevel) {
		double old = this.significanceLevel;
		this.significanceLevel = significanceLevel;
		scale.setSignificanceLevel(significanceLevel);
		firePropertyChange(PROPERTY_CHANGED, old, significanceLevel);
	}

	public final PValueColorScale getPValueScale() {
		return scale;
	}

	public Color getMinColor() {
		return scale.getMin().getColor();
	}

	public void setMinColor(Color color) {
		Color old = scale.getMin().getColor();
		scale.getMin().setColor(color);
		firePropertyChange(PROPERTY_CHANGED, old, color);
	}

	public Color getMaxColor() {
		return scale.getSigLevelPoint().getLeftColor();
	}

	public void setMaxColor(Color color) {
		Color old = scale.getSigLevelPoint().getLeftColor();
		scale.getSigLevelPoint().setLeftColor(color);
		firePropertyChange(PROPERTY_CHANGED, old, color);
	}
	
	public Color getNonSignificantColor() {
		return scale.getNonSignificantColor();
	}

	public void setNonSignificantColor(Color color) {
		Color old = scale.getNonSignificantColor();
		scale.setNonSignificantColor(color);
		firePropertyChange(PROPERTY_CHANGED, old, color);
	}
	
	@Override
	public void decorate(
			ElementDecoration decoration,
			Object element) {
		
		decoration.reset();
		
		if (element == null) {
			decoration.setBgColor(ColorConstants.emptyColor);
			decoration.setToolTip("Empty cell");
			return;
		}
		
		Object value = adapter.getValue(
				element, valueIndex);
		
		double v = MatrixUtils.doubleValue(value);
		
		boolean isSig = v <= significanceLevel;
		
		if (useCorrection) {
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
	public IColorScale getScale() {
		return scale;
	}
}
