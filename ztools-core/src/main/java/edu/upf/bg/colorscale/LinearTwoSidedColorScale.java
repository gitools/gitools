package edu.upf.bg.colorscale;

import java.awt.Color;

public class LinearTwoSidedColorScale extends CompositeColorScale {

	protected double midPoint;
	protected Color midColor;
	
	private ScaleRange leftScaleRange;
	private ScaleRange rightScaleRange;
	
	//private LogColorScale leftScale;
	//private LogColorScale rightScale;
	
	private LinearColorScale leftScale;
	private LinearColorScale rightScale;
	
	public LinearTwoSidedColorScale(
			double minPoint,
			double midPoint,
			double maxPoint,
			Color minColor,
			Color midColor,
			Color maxColor) {
		
		super(minPoint, maxPoint, minColor, maxColor);
	
		this.midPoint = midPoint;
		this.midColor = midColor;
		
		leftScale = new LinearColorScale(//new LogColorScale(
				minPoint, midPoint, 
				minColor, midColor);
		
		leftScaleRange = new ScaleRange(minPoint, midPoint, leftScale);
		
		rightScale = new LinearColorScale( //new LogColorScale(
				midPoint, maxPoint, 
				midColor, maxColor);
		
		rightScaleRange = new ScaleRange(midPoint, maxPoint, rightScale);
				
		setScaleRanges(new ScaleRange[] {
				leftScaleRange,
				rightScaleRange });
	}
	
	public LinearTwoSidedColorScale() {
		this(-2, 0, 2, 
				Color.BLUE, 
				Color.BLACK,
				Color.RED);
	}
	
	@Override
	public void setMinPoint(double minPoint) {
		super.setMinPoint(minPoint);
		leftScale.setMinPoint(minPoint);
		leftScaleRange.setMinPoint(minPoint);
	}
	
	public double getMidPoint() {
		return midPoint;
	}
	
	public void setMidPoint(double midPoint) {
		this.midPoint = midPoint;
		leftScale.setMaxPoint(midPoint);
		leftScaleRange.setMaxPoint(midPoint);
		rightScale.setMinPoint(midPoint);
		rightScaleRange.setMinPoint(midPoint);
	}
	
	@Override
	public void setMaxPoint(double maxPoint) {
		super.setMaxPoint(maxPoint);
		rightScale.setMaxPoint(maxPoint);
		rightScaleRange.setMaxPoint(maxPoint);
	}
	
	@Override
	public void setMinColor(Color minColor) {
		super.setMinColor(minColor);
		leftScale.setMinColor(minColor);
	}
	
	public Color getMidColor() {
		return midColor;
	}
	
	public void setMidColor(Color midColor) {
		this.midColor = midColor;
		leftScale.setMaxColor(midColor);
		rightScale.setMinColor(midColor);
	}
	
	@Override
	public void setMaxColor(Color maxColor) {
		super.setMaxColor(maxColor);
		rightScale.setMaxColor(maxColor);
	}
}
