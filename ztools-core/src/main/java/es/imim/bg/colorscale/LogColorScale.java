package es.imim.bg.colorscale;

import java.awt.Color;

import es.imim.bg.colorscale.util.ColorUtils;

public class LogColorScale implements ColorScale {

	private static final Color notANumberColor = Color.WHITE;
	private static final Color posInfinityColor = Color.GREEN;
	private static final Color negInfinityColor = Color.CYAN;
	
	public static final double defaultLogFactor = 0.25;
	
	private Color nonSignificantColor = new Color(187, 187, 187);
	private Color minColor = new Color(255, 255, 0);
	private Color maxColor = new Color(255, 0, 0);
	
	private double minPoint;
	private double midPoint;
	private double maxPoint;
	
	private double sigLevel;
	private double logFactor;

	public LogColorScale(
			double minPoint,
			double midPoint, 
			double maxPoint,
			double sigLevel,
			double logFactor) {
		
		this.minPoint = minPoint;
		this.midPoint = midPoint;
		this.maxPoint = maxPoint;
		this.sigLevel = sigLevel;
		this.logFactor = logFactor;
	}
	
	public LogColorScale(
			double minPoint,
			double midPoint, 
			double maxPoint) {
		
		this(minPoint, midPoint, maxPoint, midPoint, defaultLogFactor);
	}
	
	public LogColorScale() {
		this(0.0, 0.05, 1.0, 0.05, defaultLogFactor);
	}
	
	public Color getMinColor() {
		return minColor;
	}
	
	public void setMinColor(Color minColor) {
		this.minColor = minColor;
	}
	
	public Color getMaxColor() {
		return maxColor;
	}
	
	public void setMaxColor(Color maxColor) {
		this.maxColor = maxColor;
	}
	
	public double getMinPoint() {
		return minPoint;
	}
	
	public void setMinPoint(double minPoint) {
		this.minPoint = minPoint;
	}
	
	public double getMidPoint() {
		return midPoint;
	}
	
	public void setMidPoint(double midPoint) {
		this.midPoint = midPoint;
	}
	
	public double getMaxPoint() {
		return maxPoint;
	}
	
	public void setMaxPoint(double maxPoint) {
		this.maxPoint = maxPoint;
	}
	
	public double getSigLevel() {
		return sigLevel;
	}
	
	public void setSigLevel(double sigLevel) {
		this.sigLevel = sigLevel;
	}
	
	public double getLogFactor() {
		return logFactor;
	}
	
	public void setLogFactor(double logFactor) {
		this.logFactor = logFactor;
	}
	
	public Color getNonSignificantColor() {
		return nonSignificantColor;
	}
	
	public void setNonSignificantColor(Color nonSignificantColor) {
		this.nonSignificantColor = nonSignificantColor;
	}
	
	public Color getColor(double value) {
		if (Double.isNaN(value))
			return notANumberColor;
		else if (value > maxPoint || value == Double.POSITIVE_INFINITY)
			return posInfinityColor;
		else if (value < minPoint || value == Double.NEGATIVE_INFINITY)
			return negInfinityColor;
		
		if (value > sigLevel)
			return nonSignificantColor;

		double range = midPoint - minPoint;
		
		double f = value / range;
		
		f = f > 0.0 ? 1.0 + logFactor * Math.log10(f) : 0.0;

		return ColorUtils.mix(minColor, maxColor, f);
	}
}
