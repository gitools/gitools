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
import java.io.Serializable;

import edu.upf.bg.colorscale.util.ColorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractColorScale implements IColorScale, IColorScaleHtml, Serializable {

	@XmlTransient
	private ColorScaleRange range;

	/*@XmlElementWrapper(name = "points")
	@XmlElement(name = "point")*/
	@XmlTransient
	private List<ColorScalePoint> points;

	public AbstractColorScale() {
		this(new ColorScaleRange(0, 0), new ArrayList<ColorScalePoint>(0));
	}

	public AbstractColorScale(
			ColorScaleRange range,
			List<ColorScalePoint> points) {

		this.range = range;
		this.points = points;
	}

	@Override
	public ColorScaleRange getRange() {
		return range;
	}

	@Override
	public List<ColorScalePoint> getPoints() {
		return Collections.unmodifiableList(points);
	}

	protected void addPoint(ColorScalePoint point) {
		points.add(point);
		Collections.sort(points, new Comparator<ColorScalePoint>() {
			@Override
			public int compare(ColorScalePoint o1, ColorScalePoint o2) {
				return (int) Math.signum(o2.getValue() - o1.getValue());
			}
		});
	}

	@Override
	public String valueRGBHtmlColor(double value) {
		Color color = valueColor(value);
		return ColorUtils.colorToRGBHtml(color);
	}

	@Override
	public String valueHexHtmlColor(double value) {
	    Color color = valueColor(value);
	    return ColorUtils.colorToHexHtml(color);
	}
	
	
}
