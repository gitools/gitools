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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LinearTwoSidedColorScale extends CompositeColorScale {

	protected ColorScalePoint mid;

	@XmlTransient
	private ColorScaleFragment leftScaleFrag;
	
	@XmlTransient
	private LinearColorScale leftScale;

	@XmlTransient
	private ColorScaleFragment rightScaleFrag;

	@XmlTransient
	private LinearColorScale rightScale;
	
	public LinearTwoSidedColorScale(
			ColorScalePoint min,
			ColorScalePoint mid,
			ColorScalePoint max) {
		
		super(min, max);
	
		this.mid = mid;

		addPoint(mid);
		
		leftScale = new LinearColorScale(min, mid);
		leftScaleFrag = new ColorScaleFragment(min, mid, leftScale);
		
		rightScale = new LinearColorScale(mid, max);
		rightScaleFrag = new ColorScaleFragment(mid, max, rightScale);
				
		setScaleRanges(new ColorScaleFragment[] {
				leftScaleFrag,
				rightScaleFrag });
	}
	
	public LinearTwoSidedColorScale() {
		this(
				new ColorScalePoint(-2, Color.GREEN),
				new ColorScalePoint(0, Color.BLACK),
				new ColorScalePoint(2, Color.RED));
	}

	public ColorScalePoint getMid() {
		return mid;
	}
}
