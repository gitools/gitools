package es.imim.bg.colorscale;

import java.awt.Color;

import es.imim.bg.colorscale.util.ColorUtils;

@Deprecated
public class OldLogColorScale extends AbstractColorScale {

	public static final double defaultLogFactor = 0.25;

	protected Color nonSignificantColor = new Color(187, 187, 187);
	
	private double midPoint;
	
	private double sigLevel;
	private double logFactor;

	public OldLogColorScale(
			double minPoint,
			double midPoint, 
			double maxPoint,
			double sigLevel,
			double logFactor) {
	
		super(minPoint, maxPoint);
		this.midPoint = midPoint;
		this.sigLevel = sigLevel;
		this.logFactor = logFactor;
	}
	
	public OldLogColorScale(
			double minPoint,
			double midPoint, 
			double maxPoint) {
		
		this(minPoint, midPoint, maxPoint, midPoint, defaultLogFactor);
	}
	
	public OldLogColorScale() {
		this(0.0, 0.05, 1.0, 0.05, defaultLogFactor);
	}
	
	public final double getMidPoint() {
		return midPoint;
	}
	
	public final void setMidPoint(double midPoint) {
		this.midPoint = midPoint;
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
