package edu.upf.bg.colorscale;

import java.awt.Color;
import java.io.Serializable;

import edu.upf.bg.colorscale.util.ColorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractColorScale implements IColorScale, IColorScaleHtml, Serializable {

	private ColorScaleRange range;
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
