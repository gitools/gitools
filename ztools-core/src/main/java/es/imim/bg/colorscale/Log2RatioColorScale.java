package es.imim.bg.colorscale;

import java.awt.Color;

public class Log2RatioColorScale extends CompositeColorScale {

	protected double midPoint;
	protected Color midColor;
	
	public Log2RatioColorScale(
			double minPoint,
			double midPoint,
			double maxPoint,
			Color minColor,
			Color midColor,
			Color maxColor) {
		
		super(minPoint, maxPoint, minColor, maxColor);
	
		this.midPoint = midPoint;
		this.midColor = midColor;
		
		ScaleRange[] scaleRanges = new ScaleRange[] {
				new ScaleRange(minPoint, midPoint, 
						new LogColorScale(
								minPoint, midPoint, 
								minColor, midColor)),
				new ScaleRange(midPoint, maxPoint, 
						new LogColorScale(
								midPoint, maxPoint, 
								maxColor, midColor))
		};
		
		setScaleRanges(scaleRanges);
	}
	
	public Log2RatioColorScale() {
		this(-4, 0, 4, 
				Color.BLUE, 
				Color.LIGHT_GRAY,
				Color.RED);
	}
	
	public double getMidPoint() {
		return midPoint;
	}
	
	public void setMidPoint(double midPoint) {
		this.midPoint = midPoint;
	}
	
	public Color getMidColor() {
		return midColor;
	}
	
	public void setMidColor(Color midColor) {
		this.midColor = midColor;
	}
}
