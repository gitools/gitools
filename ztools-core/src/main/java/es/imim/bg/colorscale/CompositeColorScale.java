package es.imim.bg.colorscale;

import java.awt.Color;

import es.imim.bg.colorscale.util.ColorConstants;

public class CompositeColorScale extends AbstractColorScale {

	public static class ScaleRange {
		public double minPoint;
		public double maxPoint;
		public IColorScale scale;
		public ScaleRange(
				double minPoint, 
				double maxPoint,
				IColorScale scale) {
			this.minPoint = minPoint;
			this.maxPoint = maxPoint;
			this.scale = scale;
		}
	}
	
	protected Color undefinedColor;
	
	protected ScaleRange[] scaleRanges;
	
	public CompositeColorScale(
			double minPoint, 
			double maxPoint,
			Color minColor,
			Color maxColor,
			Color undefinedColor,
			ScaleRange[] scales) {
		
		super(minPoint, maxPoint, minColor, maxColor);
		
		this.undefinedColor = undefinedColor;
		this.scaleRanges = scales;
	}

	public CompositeColorScale(
			double minPoint, 
			double maxPoint,
			Color minColor,
			Color maxColor,
			Color undefinedColor) {
		this(minPoint, maxPoint,
				minColor, maxColor,
				undefinedColor, 
				new ScaleRange[0]);
	}
	
	public CompositeColorScale(
			double minPoint, 
			double maxPoint,
			Color minColor,
			Color maxColor) {
		this(minPoint, maxPoint,
				minColor, maxColor,
				ColorConstants.undefinedColor, 
				new ScaleRange[0]);
	}
	
	public CompositeColorScale(
			double minPoint, 
			double maxPoint) {
		this(minPoint, maxPoint, 
				ColorConstants.negInfinityColor,
				ColorConstants.posInfinityColor,
				ColorConstants.undefinedColor, 
				new ScaleRange[0]);
	}

	public Color getUndefinedColor() {
		return undefinedColor;
	}
	
	public void setUndefinedColor(Color undefinedColor) {
		this.undefinedColor = undefinedColor;
	}
	
	public ScaleRange[] getScaleRanges() {
		return scaleRanges;
	}
	
	public void setScaleRanges(ScaleRange[] scales) {
		this.scaleRanges = scales;
	}
	
	@Override
	public Color getColor(double value) {
		if (Double.isNaN(value))
			return notANumberColor;
		else if (value == Double.POSITIVE_INFINITY)
			return posInfinityColor;
		else if (value == Double.NEGATIVE_INFINITY)
			return negInfinityColor;
		else if (value > maxPoint)
			return maxColor;
		else if (value < minPoint)
			return minColor;
		
		for (ScaleRange range : scaleRanges)
			if (range.minPoint <= value && value <= range.maxPoint)
				return range.scale.getColor(value);
		
		return undefinedColor;
	}

}
