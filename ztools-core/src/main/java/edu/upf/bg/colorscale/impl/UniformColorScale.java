package edu.upf.bg.colorscale.impl;

import edu.upf.bg.colorscale.AbstractColorScale;
import edu.upf.bg.colorscale.ColorScalePoint;
import edu.upf.bg.colorscale.ColorScaleRange;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class UniformColorScale extends AbstractColorScale {

	protected Color color;
	
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
