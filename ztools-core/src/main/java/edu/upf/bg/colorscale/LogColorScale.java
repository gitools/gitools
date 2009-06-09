package edu.upf.bg.colorscale;

import java.awt.Color;

import edu.upf.bg.colorscale.util.ColorUtils;

public class LogColorScale extends AbstractColorScale {

	public static final double defaultLogFactor = 0.25;

	protected Color nonSignificantColor;
	
	private double logFactor;

	public LogColorScale(
			double minPoint, 
			double maxPoint,
			double logFactor) {
	
		super(minPoint, maxPoint);
		this.logFactor = logFactor;
	}
	
	public LogColorScale(
			double minPoint, 
			double maxPoint) {
		
		this(minPoint, maxPoint, defaultLogFactor);
	}
	
	public LogColorScale() {
		this(0.0, 1.0, defaultLogFactor);
	}
	
	public LogColorScale(
			double minPoint, 
			double maxPoint, 
			Color minColor, 
			Color maxColor) {
		
		this(minPoint, maxPoint);
		setMinColor(minColor);
		setMaxColor(maxColor);
	}

	public double getLogFactor() {
		return logFactor;
	}
	
	public void setLogFactor(double logFactor) {
		this.logFactor = logFactor;
	}
	
	public Color valueColor(double value) {
		if (Double.isNaN(value))
			return notANumberColor;
		else if (value > maxPoint || value == Double.POSITIVE_INFINITY)
			return posInfinityColor;
		else if (value < minPoint || value == Double.NEGATIVE_INFINITY)
			return negInfinityColor;
		
		double range = maxPoint - minPoint;
		
		double f = value / range;
		
		f = f > 0.0 ? 1.0 + logFactor * Math.log10(f) : 
			f < 0.0 ? 1.0 + logFactor * Math.log10(-f) : 0.0;

		return ColorUtils.mix(maxColor, minColor, f);
	}
}
