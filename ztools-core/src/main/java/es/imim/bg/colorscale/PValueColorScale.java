package es.imim.bg.colorscale;

import java.awt.Color;

import es.imim.bg.colorscale.util.ColorConstants;

public class PValueColorScale extends CompositeColorScale {
	
	public static final double defaultLogFactor = 0.25;
	
	private static final double epsilon = 1e-16;
	
	private double significanceLevel;
	
	private ScaleRange nonSigScaleRange;

	private ScaleRange scaleRange;

	private LogColorScale scale;

	public PValueColorScale(
			double significanceLevel, 
			Color minColor, 
			Color maxColor,
			Color nonSigColor) {
		
		super(0.0, 1.0, minColor, maxColor);
		
		this.significanceLevel = significanceLevel;
		
		nonSigScaleRange = new ScaleRange(
				significanceLevel + epsilon, 1.0, 
				new UniformColorScale(nonSigColor));
		
		scale = new LogColorScale(
				0.0, significanceLevel + epsilon,
				minColor, maxColor);
		
		scaleRange = new ScaleRange(
				0.0, significanceLevel + epsilon, scale);
		
		ScaleRange[] scaleRanges = new ScaleRange[] {
			nonSigScaleRange,
			scaleRange
		};
		
		setScaleRanges(scaleRanges);
	}
	
	public PValueColorScale() {
		this(0.05, 
				ColorConstants.pvalueMinColor, 
				ColorConstants.pvalueMaxColor, 
				ColorConstants.nonSignificantColor);
	}
	
	@Override
	public void setMinColor(Color color) {
		super.setMinColor(color);
		scale.setMinColor(color);
	}
	
	@Override
	public void setMaxColor(Color color) {
		super.setMaxColor(color);
		scale.setMaxColor(color);
	}
	
	public double getSignificanceLevel() {
		return significanceLevel;
	}
	
	public void setSignificanceLevel(double significanceLevel) {
		this.significanceLevel = significanceLevel;
		nonSigScaleRange.setMinPoint(significanceLevel + epsilon);
		scaleRange.setMaxPoint(significanceLevel + epsilon);
		scale.setMaxPoint(significanceLevel + epsilon);
	}
}
