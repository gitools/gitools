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
