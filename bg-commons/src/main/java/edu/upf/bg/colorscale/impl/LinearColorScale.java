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

import edu.upf.bg.colorscale.ColorScalePoint;
import edu.upf.bg.colorscale.SimpleColorScale;
import java.awt.Color;

import edu.upf.bg.colorscale.util.ColorUtils;

public class LinearColorScale extends SimpleColorScale {

	public LinearColorScale(
			ColorScalePoint min,
			ColorScalePoint max) {

		super(min, max);
	}

	public LinearColorScale() {
		this(
				new ColorScalePoint(0, Color.WHITE),
				new ColorScalePoint(1, Color.BLACK));
	}
	
	@Override
	public Color valueColor(double value) {
		Color color = limitsColor(value);
		if (color != null)
			return color;
		
		double range = max.getValue() - min.getValue();
		
		double f = (value - min.getValue()) / range;

		return ColorUtils.mix(max.getLeftColor(), min.getRightColor(), f);
	}

}
