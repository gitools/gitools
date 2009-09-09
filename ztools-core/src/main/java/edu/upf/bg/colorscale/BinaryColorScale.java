package edu.upf.bg.colorscale;

import java.awt.Color;

import edu.upf.bg.colorscale.util.ColorConstants;

public class BinaryColorScale extends AbstractColorScale {

	private double cutoff = 1.0;
	
	private Color nonSignificantColor  = 
		ColorConstants.nonSignificantColor;
	
	
	public BinaryColorScale( 
			double minPoint, 
			double maxPoint, 
			double cutoff,
			Color minColor,
			Color maxColor,
			Color nonSignificantColor) {
		super(minPoint, maxPoint);
		this.cutoff = cutoff;
		this.minColor = minColor;
		this.maxColor = maxColor;
		this.nonSignificantColor = nonSignificantColor;
	}
	
	public BinaryColorScale() {
		super(0.0, 1.0);
	}
	
	@Override
	public Color valueColor(double value) {
		if (Double.isNaN(value))
			return notANumberColor;
		else if (value > maxPoint || value == Double.POSITIVE_INFINITY)
			return posInfinityColor;
		else if (value < minPoint || value == Double.NEGATIVE_INFINITY)
			return negInfinityColor;
		
		return value >= cutoff ? 
				maxColor: minColor;
	}

	public void setNonSignificantColor(Color nonSignificantColor) {
		this.nonSignificantColor = nonSignificantColor;
	}

	public Color getNonSignificantColor() {
		return nonSignificantColor;
	}

	public double getCutoff() {
		return cutoff;
	}

	public void setCutoff(double cutoff) {
		this.cutoff = cutoff;
	}

	
}
