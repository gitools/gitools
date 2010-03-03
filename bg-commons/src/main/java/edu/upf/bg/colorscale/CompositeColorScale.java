package edu.upf.bg.colorscale;

import java.awt.Color;

import edu.upf.bg.colorscale.util.ColorConstants;
import edu.upf.bg.xml.adapter.ColorXmlAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CompositeColorScale extends SimpleColorScale {

	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color undefinedColor;

	@XmlTransient
	protected ColorScaleFragment[] fragments;

	public CompositeColorScale() {
	}
	
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
