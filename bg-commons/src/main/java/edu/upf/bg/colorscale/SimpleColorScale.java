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

import edu.upf.bg.colorscale.util.ColorConstants;
import edu.upf.bg.xml.adapter.ColorXmlAdapter;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class SimpleColorScale extends AbstractColorScale {

	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color notANumberColor = ColorConstants.notANumberColor;

	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color posInfinityColor = ColorConstants.posInfinityColor;

	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color negInfinityColor = ColorConstants.negInfinityColor;

	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color emptyColor = ColorConstants.emptyColor;

	protected ColorScalePoint min;
	protected ColorScalePoint max;

	public SimpleColorScale() {
	}

	public SimpleColorScale(
			ColorScalePoint min,
			ColorScalePoint max) {

		this(new ColorScaleRange(0, 0), new ArrayList<ColorScalePoint>(0), min, max);
	}

	public SimpleColorScale(
			ColorScaleRange range,
			List<ColorScalePoint> points,
			ColorScalePoint min,
			ColorScalePoint max) {

		super(range, points);
		
		this.min = min;
		this.max = max;

		addPoint(min);
		addPoint(max);
	}

	public Color limitsColor(double value) {
		if (Double.isNaN(value))
			return notANumberColor;
		else if (value == Double.POSITIVE_INFINITY)
			return posInfinityColor;
		else if (value == Double.NEGATIVE_INFINITY)
			return negInfinityColor;
		else if (value > max.getValue())
			return max.getColor();
		else if (value < min.getValue())
			return min.getColor();
		
		return null;
	}

	public Color simpleLimitsColor(double value) {
		if (Double.isNaN(value))
			return notANumberColor;
		else if (value == Double.POSITIVE_INFINITY)
			return posInfinityColor;
		else if (value == Double.NEGATIVE_INFINITY)
			return negInfinityColor;

		return null;
	}

	public Color getNotANumberColor() {
		return notANumberColor;
	}

	public void setNotANumberColor(Color notANumberColor) {
		this.notANumberColor = notANumberColor;
	}

	public Color getPosInfinityColor() {
		return posInfinityColor;
	}

	public void setPosInfinityColor(Color posInfinityColor) {
		this.posInfinityColor = posInfinityColor;
	}

	public Color getNegInfinityColor() {
		return negInfinityColor;
	}

	public void setNegInfinityColor(Color negInfinityColor) {
		this.negInfinityColor = negInfinityColor;
	}

	public Color getEmptyColor() {
		return emptyColor;
	}

	public void setEmptyColor(Color emptyColor) {
		this.emptyColor = emptyColor;
	}

	public ColorScalePoint getMin() {
		return min;
	}

	public void setMin(ColorScalePoint min) {
		this.min = min;
	}

	public ColorScalePoint getMax() {
		return max;
	}

	public void setMax(ColorScalePoint max) {
		this.max = max;
	}

	@Override
	public ColorScaleRange getRange() {
		return new ColorScaleRange(min.getValue(), max.getValue());
	}
}
