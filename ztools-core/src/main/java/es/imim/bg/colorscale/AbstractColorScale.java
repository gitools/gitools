package es.imim.bg.colorscale;

import java.awt.Color;

import es.imim.bg.colorscale.util.ColorConstants;

public abstract class AbstractColorScale implements IColorScale {

	protected Color notANumberColor = ColorConstants.notANumberColor;
	protected Color posInfinityColor = ColorConstants.posInfinityColor;
	protected Color negInfinityColor = ColorConstants.negInfinityColor;
	
	protected Color minColor;
	protected Color maxColor;
	
	protected double minPoint;
	protected double maxPoint;
	
	public AbstractColorScale(
			double minPoint, double maxPoint) {
		
		this(minPoint, maxPoint, 
				ColorConstants.negInfinityColor, 
				ColorConstants.posInfinityColor);
	}
	
	public AbstractColorScale(
			double minPoint, double maxPoint,
			Color minColor, Color maxColor) {
		this.minPoint = minPoint;
		this.maxPoint = maxPoint;
		this.minColor = minColor;
		this.maxColor = maxColor;
	}

	public final Color getNotANumberColor() {
		return notANumberColor;
	}
	
	public final void setNotANumberColor(Color notANumberColor) {
		this.notANumberColor = notANumberColor;
	}
	
	
	public final Color getPosInfinityColor() {
		return posInfinityColor;
	}

	public final void setPosInfinityColor(Color posInfinityColor) {
		this.posInfinityColor = posInfinityColor;
	}

	public final Color getNegInfinityColor() {
		return negInfinityColor;
	}

	public final void setNegInfinityColor(Color negInfinityColor) {
		this.negInfinityColor = negInfinityColor;
	}
	
	public final Color getMinColor() {
		return minColor;
	}

	public final void setMinColor(Color minColor) {
		this.minColor = minColor;
	}

	public final Color getMaxColor() {
		return maxColor;
	}

	public final void setMaxColor(Color maxColor) {
		this.maxColor = maxColor;
	}

	public final double getMinPoint() {
		return minPoint;
	}

	public final void setMinPoint(double minPoint) {
		this.minPoint = minPoint;
	}

	public final double getMaxPoint() {
		return maxPoint;
	}

	public final void setMaxPoint(double maxPoint) {
		this.maxPoint = maxPoint;
	}
}
