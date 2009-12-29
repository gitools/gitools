/*
 *  Copyright 2009 chris.
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

package edu.upf.bg.colorscale;

import java.awt.Color;
import java.io.Serializable;

public class ColorScalePoint 
	implements Serializable {

	private static final long serialVersionUID = -7085812262326883603L;
	
	protected String name;
	protected double value;
	protected Color leftColor;
	protected Color rightColor;

	public ColorScalePoint(String name, double value, Color leftColor, Color rightColor) {
		this.name = name;
		this.value = value;
		this.leftColor = leftColor;
		this.rightColor = rightColor;
	}

	public ColorScalePoint(String name, double value, Color color) {
		this(name, value, color, color);
	}

	public ColorScalePoint(double value, Color leftColor, Color rightColor) {
		this("", value, leftColor, rightColor);
	}

	public ColorScalePoint(double value, Color color) {
		this("", value, color);
	}

	public ColorScalePoint(double value) {
		this("", value, null);
	}

	public ColorScalePoint(Color color) {
		this("", Double.NaN, color);
	}

	public ColorScalePoint(Color leftColor, Color rightColor) {
		this("", Double.NaN, leftColor, rightColor);
	}

	public String getName() {
		return name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Color getLeftColor() {
		return leftColor;
	}

	public void setLeftColor(Color leftColor) {
		this.leftColor = leftColor;
	}

	public Color getRightColor() {
		return rightColor;
	}

	public void setRightColor(Color rightColor) {
		this.rightColor = rightColor;
	}

	public Color getColor() {
		return leftColor != null ? leftColor : rightColor;
	}

	public void setColor(Color color) {
		this.leftColor = color;
		this.rightColor = color;
	}
}
