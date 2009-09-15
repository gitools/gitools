package edu.upf.bg.colorscale;

import java.awt.Color;
import java.io.Serializable;

import edu.upf.bg.colorscale.util.ColorConstants;
import edu.upf.bg.colorscale.util.ColorUtils;

public abstract class AbstractColorScale implements IColorScale, IColorScaleHtml, Serializable {

	protected Color notANumberColor = ColorConstants.notANumberColor;
	protected Color posInfinityColor = ColorConstants.posInfinityColor;
	protected Color negInfinityColor = ColorConstants.negInfinityColor;
	protected Color emptyColor = ColorConstants.emptyColor;
	
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

	public Color getNotANumberColor() {
		return notANumberColor;
	}
	
	public void setNotANumberColor(Color notANumberColor) {
		this.notANumberColor = notANumberColor;
	}
	
	
	public Color getPosInfinityColor() {
		return posInfinityColor;
	}

	public void setPosInfinityColor(Color posInfinityColor) {
		this.posInfinityColor = posInfinityColor;
	}

	public Color getNegInfinityColor() {
		return negInfinityColor;
	}

	public void setNegInfinityColor(Color negInfinityColor) {
		this.negInfinityColor = negInfinityColor;
	}
	
	public Color getEmptyColor() {
		return emptyColor;
	}
	
	public void setEmptyColor(Color emptyColor) {
		this.emptyColor = emptyColor;
	}
	
	public Color getMinColor() {
		return minColor;
	}

	public void setMinColor(Color minColor) {
		this.minColor = minColor;
	}

	public Color getMaxColor() {
		return maxColor;
	}

	public void setMaxColor(Color maxColor) {
		this.maxColor = maxColor;
	}

	public double getMinPoint() {
		return minPoint;
	}

	public void setMinPoint(double minPoint) {
		this.minPoint = minPoint;
	}

	public double getMaxPoint() {
		return maxPoint;
	}

	public void setMaxPoint(double maxPoint) {
		this.maxPoint = maxPoint;
	}
	
	@Override
	public String valueRGBHtmlColor(double value) {
		Color color = valueColor(value);
		return ColorUtils.colorToRGBHtml(color);
	}

	@Override
	public String valueHexHtmlColor(double value) {
	    Color color = valueColor(value);
	    return ColorUtils.colorToHexHtml(color);
	}
	
	
}
