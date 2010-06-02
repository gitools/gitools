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
