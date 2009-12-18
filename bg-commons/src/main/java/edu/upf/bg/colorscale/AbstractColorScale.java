package edu.upf.bg.colorscale;

import java.awt.Color;
import java.io.Serializable;

import edu.upf.bg.colorscale.util.ColorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
