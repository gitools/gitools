package es.imim.bg.colorscale;

import java.awt.Color;

import es.imim.bg.colorscale.util.ColorUtils;

public class LogColorScale implements ColorScale {
	
	private static final Color notANumberColor = Color.WHITE;
	private static final Color posInfinityColor = Color.GREEN;
	private static final Color negInfinityColor = Color.CYAN;
	
	private static final Color nonSignificantColor = new Color(187, 187, 187);
	
	private static final Color minColor = new Color(255, 255, 0);
	private static final Color maxColor = new Color(255, 0, 0);
	
	public static final double defaultLogFactor = 0.25;
	
	private double logFactor;

	public LogColorScale(double logFactor) {
		this.logFactor = logFactor;
	}
	
	public LogColorScale() {
		this(defaultLogFactor);
	}
	
	public Color getColor(double value) {
		if (Double.isNaN(value))
			return notANumberColor;
		else if (value == Double.POSITIVE_INFINITY)
			return posInfinityColor;
		else if (value == Double.NEGATIVE_INFINITY)
			return negInfinityColor;
		
		if (value > 0.05)
			return nonSignificantColor;

		double f = value / 0.05;
		
		f = f > 0.0 ? 1.0 + logFactor * Math.log10(f) : 0.0;

		return ColorUtils.mix(minColor, maxColor, f);
	}

}
