package edu.upf.bg.colorscale;

import java.awt.Color;

import edu.upf.bg.colorscale.util.ColorUtils;

public class LinearColorScale extends AbstractColorScale {

	public LinearColorScale(
			double minPoint, 
			double maxPoint, 
			Color minColor,
			Color maxColor) {
		super(minPoint, maxPoint);
		this.minColor = minColor;
		this.maxColor = maxColor;
	}

	public LinearColorScale(double minPoint, double maxPoint) {
		super(minPoint, maxPoint);
	}
	
	public LinearColorScale() {
		super(0.0, 1.0);
	}
	
	@Override
	public Color getColor(double value) {
		if (Double.isNaN(value))
			return notANumberColor;
		else if (value > maxPoint || value == Double.POSITIVE_INFINITY)
			return posInfinityColor;
		else if (value < minPoint || value == Double.NEGATIVE_INFINITY)
			return negInfinityColor;
		
		double range = maxPoint - minPoint;
		
		double f = value / range;

		return f <= 0 ? 
				ColorUtils.mix(minColor, maxColor, -f)
				: ColorUtils.mix(maxColor, minColor, f);
	}

}
