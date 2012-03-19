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

import edu.upf.bg.colorscale.IColorScale;
import java.util.IllegalFormatException;

import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.matrix.model.element.IElementAdapter;

public class FormattedTextElementDecorator extends ElementDecorator {
	
	private static final long serialVersionUID = -8595819997133940913L;
	
	private int valueIndex = -1;
	private String formatString = null;
	
	public FormattedTextElementDecorator() {
		this.formatString = "%1$s";
	}

	public FormattedTextElementDecorator(IElementAdapter elementAdapter) {
		super(elementAdapter);
		this.formatString = "%1$s";
	}

	@Override
	public Object clone() {
		FormattedTextElementDecorator obj = null;
		try {
			obj = (FormattedTextElementDecorator) super.clone();
		}
		catch (CloneNotSupportedException ex) { }
		return obj;
	}

	public void setFormatString(String formatString) {
		this.formatString = formatString;
	}

	public String getFormatString() {
		return formatString;
	}

	
	public final int getValueIndex() {
		return valueIndex;
	}

	public final void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
		firePropertyChange(PROPERTY_CHANGED);
	}

	@Override
	public void decorate(ElementDecoration decoration, Object element) {
		String cellText;
		try {
			if (this.valueIndex >= 0) {
				cellText = String.format(formatString, 
						this.adapter.getValue(element, valueIndex));
			} else
				cellText = String.format(formatString, element);

		} catch (IllegalFormatException e) {
			cellText = element.toString();
		}
		decoration.setText(cellText);
		decoration.setToolTip(cellText);
	}

	@Override
	public IColorScale getScale() {
		return null;
	}

}
