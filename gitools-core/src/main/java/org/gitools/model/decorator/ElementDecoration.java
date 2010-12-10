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

package org.gitools.model.decorator;

import java.awt.Color;
import java.io.Serializable;

public class ElementDecoration implements Serializable {

	private static final long serialVersionUID = 5204451046972665249L;

	public enum TextAlignment {
		left, right, center
	}
	
	protected String text;
	public TextAlignment textAlign;
	protected String toolTip;
	protected Color fgColor;
	protected Color bgColor;
	
	public ElementDecoration() {
		reset();
	}

	public void reset() {
		this.text = "";
		this.textAlign = TextAlignment.left;
		this.toolTip = "";
		this.fgColor = Color.BLACK;
		this.bgColor = Color.WHITE;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public TextAlignment getTextAlign() {
		return textAlign;
	}
	
	public void setTextAlign(TextAlignment textAlign) {
		this.textAlign = textAlign;
	}

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	public Color getFgColor() {
		return fgColor;
	}

	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}
}
