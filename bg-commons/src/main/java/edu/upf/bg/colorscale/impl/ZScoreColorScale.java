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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ZScoreColorScale extends CompositeColorScale {

	private double halfAmplitude;
	private double sigHalfAmplitude;

	private final ColorScalePoint center;
	private final ColorScalePoint nonSigLeft;
	private final ColorScalePoint nonSigRight;

	@XmlTransient
	private UniformColorScale nonSigScale;

	@XmlTransient
	private ColorScaleFragment nonSigScaleFrag;

	@XmlTransient
	private LinearColorScale leftScale;

	@XmlTransient
	private ColorScaleFragment leftScaleFrag;

	@XmlTransient
	private LinearColorScale rightScale;

	@XmlTransient
	private ColorScaleFragment rightScaleFrag;
	
	public ZScoreColorScale(
			double centerValue,
			double halfAmplitude, 
			double sigHalfAmplitude,
			Color leftMinColor,
			Color leftMaxColor,
			Color rightMinColor,
			Color rightMaxColor,
			Color nonSignificantColor) {
		
		super(
				new ColorScalePoint(leftMinColor),
				new ColorScalePoint(rightMaxColor));
	
		this.halfAmplitude = halfAmplitude;
		this.sigHalfAmplitude = sigHalfAmplitude;

		center = new ColorScalePoint(centerValue, leftMaxColor, rightMinColor);

		min.setValue(centerValue - halfAmplitude);
		max.setValue(centerValue + halfAmplitude);

		nonSigLeft = new ColorScalePoint(centerValue - sigHalfAmplitude);
		nonSigRight = new ColorScalePoint(centerValue + sigHalfAmplitude);

		addPoint(nonSigLeft);
		addPoint(nonSigRight);
		
		nonSigScale = new UniformColorScale(nonSignificantColor);
		nonSigScaleFrag = new ColorScaleFragment(nonSigLeft, nonSigRight, nonSigScale);
		
		leftScale = new LinearColorScale(min, center);
		leftScaleFrag = new ColorScaleFragment(min, center, leftScale);
		
		rightScale = new LinearColorScale(center, max);
		rightScaleFrag = new ColorScaleFragment(center, max, rightScale);
		
		setScaleRanges(new ColorScaleFragment[] {
				nonSigScaleFrag,
				leftScaleFrag,
				rightScaleFrag
		});
	}

	public ZScoreColorScale() {
		this(0, 10, 1.96, 
				Color.BLUE, Color.CYAN,
				Color.YELLOW, Color.RED,
				ColorConstants.nonSignificantColor);
	}

	public double getHalfAmplitude() {
		return halfAmplitude;
	}

	public void setHalfAmplitude(double halfAmplitude) {
		this.halfAmplitude = halfAmplitude;
		recalculate();
	}

	public double getSigHalfAmplitude() {
		return sigHalfAmplitude;
	}

	public void setSigHalfAmplitude(double sigHalfAmplitude) {
		this.sigHalfAmplitude = sigHalfAmplitude;
		recalculate();
	}

	public ColorScalePoint getCenter() {
		return center;
	}

	public ColorScalePoint getNonSigLeft() {
		return nonSigLeft;
	}

	public ColorScalePoint getNonSigRight() {
		return nonSigRight;
	}

	public Color getNonSignificantColor() {
		return nonSigScale.getColor();
	}
	
	public void setNonSignificantColor(Color color) {
		nonSigScale.setColor(color);
	}
	
	private void recalculate() {
		min.setValue(center.getValue() - halfAmplitude);
		max.setValue(center.getValue() + halfAmplitude);

		nonSigLeft.setValue(center.getValue() - sigHalfAmplitude);
		nonSigRight.setValue(center.getValue() + sigHalfAmplitude);
	}
}
