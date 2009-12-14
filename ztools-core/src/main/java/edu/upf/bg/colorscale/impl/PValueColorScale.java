package edu.upf.bg.colorscale.impl;

import edu.upf.bg.colorscale.ColorScaleFragment;
import edu.upf.bg.colorscale.ColorScalePoint;
import edu.upf.bg.colorscale.CompositeColorScale;
import java.awt.Color;

import edu.upf.bg.colorscale.util.ColorConstants;

public class PValueColorScale extends CompositeColorScale {
	
	public static final double defaultLogFactor = 0.25;
	
	private static final double epsilon = 1e-16;
	
	private double significanceLevel;
	
	private UniformColorScale nonSigScale;
	private ColorScaleFragment nonSigScaleFrag;

	private LogColorScale scale;
	private ColorScaleFragment scaleFrag;
	
	private final ColorScalePoint sigLevel;

	public PValueColorScale(
			double significanceLevel, 
			Color minColor, 
			Color maxColor,
			Color nonSignificantColor) {
		
		super(
				new ColorScalePoint(0.0, minColor),
				new ColorScalePoint(1.0, nonSignificantColor));
		
		this.significanceLevel = significanceLevel;

		sigLevel = new ColorScalePoint(significanceLevel + epsilon, maxColor);

		nonSigScale = new UniformColorScale(nonSignificantColor);
		nonSigScaleFrag = new ColorScaleFragment(sigLevel, max, nonSigScale);
		
		scale = new LogColorScale(min, sigLevel);
		scaleFrag = new ColorScaleFragment(min, sigLevel, scale);
		
		setScaleRanges(new ColorScaleFragment[] {
			nonSigScaleFrag,
			scaleFrag
		});
	}
	
	public PValueColorScale() {
		this(0.05, 
				ColorConstants.minColor,
				ColorConstants.maxColor,
				ColorConstants.nonSignificantColor);
	}
	
	public double getSignificanceLevel() {
		return significanceLevel;
	}
	
	public void setSignificanceLevel(double significanceLevel) {
		this.significanceLevel = significanceLevel;
		sigLevel.setValue(significanceLevel + epsilon);
	}

	public ColorScalePoint getSigLevel() {
		return sigLevel;
	}

	public Color getNonSignificantColor() {
		return max.getColor();
	}
	
	public void setNonSignificantColor(Color color) {
		nonSigScale.setColor(color);
		max.setColor(color);
	}
}
