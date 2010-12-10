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

package edu.upf.bg.colorscale.impl;

import edu.upf.bg.colorscale.ColorScaleFragment;
import edu.upf.bg.colorscale.ColorScalePoint;
import edu.upf.bg.colorscale.CompositeColorScale;
import java.awt.Color;

import edu.upf.bg.colorscale.util.ColorConstants;
import edu.upf.bg.xml.adapter.ColorXmlAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PValueColorScale extends CompositeColorScale {
	
	public static final double defaultLogFactor = 0.25;
	
	private static final double epsilon = 1e-16;
	
	private double significanceLevel;

	private UniformColorScale nonSigScale;

	private ColorScaleFragment nonSigScaleFrag;

	private LogColorScale scale;

	private ColorScaleFragment scaleFrag;
	
	private ColorScalePoint sigLevel;

	public PValueColorScale(
			double significanceLevel, 
			Color minColor, 
			Color maxColor,
			Color nonSignificantColor) {
		
		super(
				new ColorScalePoint(0.0, minColor),
				new ColorScalePoint(1.0, nonSignificantColor));
		
		this.significanceLevel = significanceLevel;

		sigLevel = new ColorScalePoint(significanceLevel + epsilon, maxColor);

		addPoint(sigLevel);

		nonSigScale = new UniformColorScale(nonSignificantColor);
		nonSigScaleFrag = new ColorScaleFragment(sigLevel, max, nonSigScale);
		
		scale = new LogColorScale(min, sigLevel);
		scaleFrag = new ColorScaleFragment(min, sigLevel, scale);
		
		setScaleRanges(new ColorScaleFragment[] {
			nonSigScaleFrag,
			scaleFrag
		});
	}
	
	public PValueColorScale() {
		this(0.05, 
				ColorConstants.minColor,
				ColorConstants.maxColor,
				ColorConstants.nonSignificantColor);
	}
	
	public double getSignificanceLevel() {
		return significanceLevel;
	}
	
	public void setSignificanceLevel(double significanceLevel) {
		this.significanceLevel = significanceLevel;
		sigLevel.setValue(significanceLevel + epsilon);
	}

	public ColorScalePoint getSigLevelPoint() {
		return sigLevel;
	}

	public void setSigLevelPoint(ColorScalePoint sigLevel) {
		this.sigLevel = sigLevel;
	}

	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	public Color getNonSignificantColor() {
		return max.getColor();
	}
	
	public void setNonSignificantColor(Color color) {
		nonSigScale.setColor(color);
		max.setColor(color);
	}
}
