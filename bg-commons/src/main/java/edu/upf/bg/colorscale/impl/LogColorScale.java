package edu.upf.bg.colorscale.impl;

import edu.upf.bg.colorscale.ColorScalePoint;
import edu.upf.bg.colorscale.SimpleColorScale;
import edu.upf.bg.colorscale.util.ColorConstants;
import java.awt.Color;

import edu.upf.bg.colorscale.util.ColorUtils;

public class LogColorScale extends SimpleColorScale {

	public static final double defaultLogFactor = 0.25;

	protected Color nonSignificantColor;
	
	private double logFactor;

	public LogColorScale(
			ColorScalePoint min,
			ColorScalePoint max,
			double logFactor) {
	
		super(min, max);

		this.logFactor = logFactor;
	}
	
	public LogColorScale(
			ColorScalePoint min,
			ColorScalePoint max) {
		
		this(min, max, defaultLogFactor);
	}
	
	public LogColorScale() {
		this(
				new ColorScalePoint(0.0, ColorConstants.minColor),
				new ColorScalePoint(1.0, ColorConstants.maxColor),
				defaultLogFactor);
	}
	
	public double getLogFactor() {
		return logFactor;
	}
	
	public void setLogFactor(double logFactor) {
		this.logFactor = logFactor;
	}
	
	@Override
	public Color valueColor(double value) {
		Color color = limitsColor(value);
		if (color != null)
			return color;
		
		double range = max.getValue() - min.getValue();
		
		double f = value / range;
		
		f = f > 0.0 ? 1.0 + logFactor * Math.log10(f) : 
			f < 0.0 ? 1.0 + logFactor * Math.log10(-f) : 0.0;

		return ColorUtils.mix(max.getLeftColor(), min.getRightColor(), f);
	}
}
