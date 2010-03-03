package edu.upf.bg.colorscale.impl;

import edu.upf.bg.colorscale.AbstractColorScale;
import edu.upf.bg.colorscale.ColorScalePoint;
import edu.upf.bg.colorscale.ColorScaleRange;
import edu.upf.bg.xml.adapter.ColorXmlAdapter;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UniformColorScale extends AbstractColorScale {

	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color color;

	public UniformColorScale() {
	}
	
	public UniformColorScale(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public Color valueColor(double value) {
		return color;
	}

	@Override
	public ColorScaleRange getRange() {
		return new ColorScaleRange(-1, 1);
	}

	@Override
	public List<ColorScalePoint> getPoints() {
		return new ArrayList<ColorScalePoint>(0);
	}

}
