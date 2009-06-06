package es.imim.bg.colorscale;

import java.awt.Color;

import es.imim.bg.colorscale.CompositeColorScale.ScaleRange;
import es.imim.bg.colorscale.util.ColorConstants;

public class ZScoreColorScale extends CompositeColorScale {

	protected double center;
	protected double halfAmplitude;
	protected double sigHalfAmplitude;
	private ScaleRange nonSigScaleRange;
	private LinearColorScale leftScale;
	private ScaleRange leftScaleRange;
	private LinearColorScale rightScale;
	private ScaleRange rightScaleRange;
	
	public ZScoreColorScale(
			double center,
			double halfAmplitude, 
			double sigHalfAmplitude,
			Color leftMinColor,
			Color leftMaxColor,
			Color rightMinColor,
			Color rightMaxColor,
			Color nonSigColor) {
		
		super(0.0, 0.0, 
				leftMinColor, rightMaxColor);
	
		this.center = center;
		this.halfAmplitude = halfAmplitude;
		this.sigHalfAmplitude = sigHalfAmplitude;
		
		double min = -halfAmplitude + center;
		double max = halfAmplitude + center;
		
		double sigMin = -sigHalfAmplitude + center;
		double sigMax = sigHalfAmplitude + center;
		
		setMinPoint(min);
		setMaxPoint(max);
		
		nonSigScaleRange = new ScaleRange(sigMin, sigMax, 
				new UniformColorScale(nonSigColor));
		
		leftScale = new LinearColorScale(
				min, center, 
				leftMinColor, 
				leftMaxColor);
		
		leftScaleRange = new ScaleRange(min, center, leftScale);
		
		rightScale = new LinearColorScale(
				center, max, 
				rightMinColor, 
				rightMaxColor);
		
		rightScaleRange = new ScaleRange(center, max, rightScale);
		
		ScaleRange[] scaleRanges = new ScaleRange[] {
				nonSigScaleRange,
				leftScaleRange,
				rightScaleRange
		};
		
		setScaleRanges(scaleRanges);
	}

	public ZScoreColorScale() {
		this(0, 10, 1.96, 
				Color.BLUE, Color.CYAN,
				Color.YELLOW, Color.RED,
				ColorConstants.nonSignificantColor);
	}

	public double getCenter() {
		return center;
	}

	public void setCenter(double center) {
		this.center = center;
		recalculate();
	}

	public double getHalfAmplitude() {
		return halfAmplitude;
	}

	public void setHalfAmplitude(double halfAmplitude) {
		this.halfAmplitude = halfAmplitude;
		recalculate();
	}

	public double getSigHalfAmplitude() {
		return sigHalfAmplitude;
	}

	public void setSigHalfAmplitude(double sigHalfAmplitude) {
		this.sigHalfAmplitude = sigHalfAmplitude;
		recalculate();
	}

	public Color getLeftMinColor() {
		return leftScale.getMinColor();
	}
	
	public void setLeftMinColor(Color color) {
		leftScale.setMinColor(color);
	}
	
	public Color getLeftMaxColor() {
		return leftScale.getMaxColor();
	}
	
	public void setLeftMaxColor(Color color) {
		leftScale.setMaxColor(color);
	}
	
	public Color getRightMinColor() {
		return rightScale.getMinColor();
	}
	
	public void setRightMinColor(Color color) {
		rightScale.setMinColor(color);
	}
	
	public Color getRightMaxColor() {
		return rightScale.getMaxColor();
	}
	
	public void setRightMaxColor(Color color) {
		rightScale.setMaxColor(color);
	}
	
	private void recalculate() {
		double min = -halfAmplitude + center;
		double max = halfAmplitude + center;

		leftScale.setMinPoint(min);
		leftScaleRange.setMinPoint(min);
		leftScale.setMaxPoint(center);
		leftScaleRange.setMaxPoint(center);
		
		rightScale.setMaxPoint(max);
		rightScaleRange.setMaxPoint(max);
		rightScale.setMinPoint(center);
		rightScaleRange.setMinPoint(center);
		
		setMinPoint(min);
		setMaxPoint(max);
		
		nonSigScaleRange.setMinPoint(-sigHalfAmplitude + center);
		nonSigScaleRange.setMaxPoint(sigHalfAmplitude + center);
	}
}
