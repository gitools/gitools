package es.imim.bg.colorscale;

import java.awt.Color;

import es.imim.bg.colorscale.util.ColorUtils;

public class PValueLogColorScale implements ColorScale {
	
	private static final Color notANumberColor = Color.WHITE;
	private static final Color posInfinityColor = Color.GREEN;
	private static final Color negInfinityColor = Color.CYAN;
	
	public static final double defaultLogFactor = 0.25;
	
	private Color nonSignificantColor = new Color(187, 187, 187);
	private Color minColor = new Color(255, 255, 0);
	private Color maxColor = new Color(255, 0, 0);
	
	private double midPoint;
	private double logFactor;

	public PValueLogColorScale(double midPoint, double logFactor) {
		this.midPoint = midPoint;
		this.logFactor = logFactor;
	}
	
	public double getMidPoint() {
		return midPoint;
	}
	
	public void setMidPoint(double midPoint) {
		this.midPoint = midPoint;
	}
	
	public PValueLogColorScale() {
		this(0.05, defaultLogFactor);
	}
	
	public double getLogFactor() {
		return logFactor;
	}
	
	public void setLogFactor(double logFactor) {
		this.logFactor = logFactor;
	}
	
	public Color getColor(double value) {
		if (Double.isNaN(value))
			return notANumberColor;
		else if (value == Double.POSITIVE_INFINITY)
			return posInfinityColor;
		else if (value == Double.NEGATIVE_INFINITY)
			return negInfinityColor;
		
		if (value > midPoint)
			return nonSignificantColor;

		double f = value / midPoint;
		
		f = f > 0.0 ? 1.0 + logFactor * Math.log10(f) : 0.0;

		return ColorUtils.mix(minColor, maxColor, f);
	}
}
