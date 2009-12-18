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

import java.io.Serializable;

public class ColorScaleFragment implements Serializable {

	public ColorScalePoint min;
	public ColorScalePoint max;
	public IColorScale scale;

	public ColorScaleFragment(
			ColorScalePoint min,
			ColorScalePoint max,
			IColorScale scale) {

		this.min = min;
		this.max = max;
		this.scale = scale;
	}

	public ColorScalePoint getMin() {
		return min;
	}

	public void setMin(ColorScalePoint min) {
		this.min = min;
	}

	public double getMinPoint() {
		return min.getValue();
	}

	public void setMinPoint(double value) {
		this.min.setValue(value);
	}

	public ColorScalePoint getMax() {
		return max;
	}

	public void setMax(ColorScalePoint max) {
		this.max = max;
	}

	public double getMaxPoint() {
		return max.getValue();
	}

	public void setMaxPoint(double value) {
		this.max.setValue(value);
	}

	public IColorScale getScale() {
		return scale;
	}

	public void setScale(IColorScale scale) {
		this.scale = scale;
	}
}
