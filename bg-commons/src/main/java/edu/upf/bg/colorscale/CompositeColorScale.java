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

package edu.upf.bg.colorscale;

import java.awt.Color;

import edu.upf.bg.colorscale.util.ColorConstants;
import edu.upf.bg.xml.adapter.ColorXmlAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CompositeColorScale extends SimpleColorScale {

	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color undefinedColor;

	@XmlTransient
	protected ColorScaleFragment[] fragments;

	public CompositeColorScale() {
	}
	
	public CompositeColorScale(
			ColorScalePoint min,
			ColorScalePoint max,
			Color undefinedColor,
			ColorScaleFragment[] fragments) {
		
		super(min, max);
		
		this.undefinedColor = undefinedColor;
		this.fragments = fragments;
	}

	public CompositeColorScale(
			ColorScalePoint min,
			ColorScalePoint max,
			Color undefinedColor) {

		this(min, max, undefinedColor, new ColorScaleFragment[0]);
	}
	
	public CompositeColorScale(
			ColorScalePoint min,
			ColorScalePoint max) {
		this(min, max, ColorConstants.undefinedColor, new ColorScaleFragment[0]);
	}
	
	public Color getUndefinedColor() {
		return undefinedColor;
	}
	
	public void setUndefinedColor(Color undefinedColor) {
		this.undefinedColor = undefinedColor;
	}
	
	public ColorScaleFragment[] getScaleRanges() {
		return fragments;
	}
	
	public void setScaleRanges(ColorScaleFragment[] scales) {
		this.fragments = scales;
	}
	
	@Override
	public Color valueColor(double value) {
		Color color = limitsColor(value);
		if (color != null)
			return color;

		for (ColorScaleFragment range : fragments) {
			double rmin = range.min.getValue();
			double rmax = range.max.getValue();
			if (rmin <= value && value <= rmax)
				return range.scale.valueColor(value);
		}
		
		return undefinedColor;
	}

}
