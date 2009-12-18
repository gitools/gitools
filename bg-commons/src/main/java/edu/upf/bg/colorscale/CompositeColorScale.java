package edu.upf.bg.colorscale;

import java.awt.Color;

import edu.upf.bg.colorscale.util.ColorConstants;

public class CompositeColorScale extends SimpleColorScale {
	
	protected Color undefinedColor;
	
	protected ColorScaleFragment[] fragments;
	
	public CompositeColorScale(
			ColorScalePoint min,
			ColorScalePoint max,
			Color undefinedColor,
			ColorScaleFragment[] fragments) {
		
		super(min, max);
		
		this.undefinedColor = undefinedColor;
		this.fragments = fragments;
	}

	public CompositeColorScale(
			ColorScalePoint min,
			ColorScalePoint max,
			Color undefinedColor) {

		this(min, max, undefinedColor, new ColorScaleFragment[0]);
	}
	
	public CompositeColorScale(
			ColorScalePoint min,
			ColorScalePoint max) {
		this(min, max, ColorConstants.undefinedColor, new ColorScaleFragment[0]);
	}
	
	public Color getUndefinedColor() {
		return undefinedColor;
	}
	
	public void setUndefinedColor(Color undefinedColor) {
		this.undefinedColor = undefinedColor;
	}
	
	public ColorScaleFragment[] getScaleRanges() {
		return fragments;
	}
	
	public void setScaleRanges(ColorScaleFragment[] scales) {
		this.fragments = scales;
	}
	
	@Override
	public Color valueColor(double value) {
		Color color = limitsColor(value);
		if (color != null)
			return color;

		for (ColorScaleFragment range : fragments)
			if (range.min.getValue() <= value
					&& value <= range.max.getValue())
				return range.scale.valueColor(value);
		
		return undefinedColor;
	}

}
