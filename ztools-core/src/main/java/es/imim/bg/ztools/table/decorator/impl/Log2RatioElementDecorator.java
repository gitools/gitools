package es.imim.bg.ztools.table.decorator.impl;

import java.awt.Color;

import es.imim.bg.GenericFormatter;
import es.imim.bg.colorscale.Log2RatioColorScale;
import es.imim.bg.colorscale.util.ColorConstants;
import es.imim.bg.ztools.table.TableUtils;
import es.imim.bg.ztools.table.decorator.ElementDecoration;
import es.imim.bg.ztools.table.decorator.ElementDecorator;
import es.imim.bg.ztools.table.element.IElementAdapter;

public class Log2RatioElementDecorator extends ElementDecorator {

	private int valueIndex;
	
	/*private double minValue;
	private double midValue;
	private double maxValue;
	
	private Color minColor;
	private Color midColor;
	private Color maxColor;*/
	
	private Log2RatioColorScale scale;
	
	private GenericFormatter fmt = new GenericFormatter("<");
	
	public Log2RatioElementDecorator(IElementAdapter adapter) {
		super(adapter);
		
		valueIndex = getPropertyIndex(new String[] {
				"value", "log2ratio" });
		
		/*minValue = -2;
		midValue = 0;
		maxValue = 2;
		
		minColor = Color.RED;
		midColor = Color.WHITE;
		maxColor = Color.BLUE;*/
		
		scale = new Log2RatioColorScale(/*
				minValue, midValue, maxValue, 
				minColor, midColor, maxColor*/);
	}

	public final int getValueIndex() {
		return valueIndex;
	}

	public final void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final double getMinValue() {
		return scale.getMinPoint();
	}

	public final void setMinValue(double minValue) {
		//this.minValue = minValue;
		scale.setMinPoint(minValue);
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final double getMidValue() {
		return scale.getMidPoint();
	}

	public final void setMidValue(double midValue) {
		//this.midValue = midValue;
		scale.setMidPoint(midValue);
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final double getMaxValue() {
		return scale.getMaxPoint();
	}

	public final void setMaxValue(double maxValue) {
		//this.maxValue = maxValue;
		scale.setMaxPoint(maxValue);
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final Color getMinColor() {
		return scale.getMinColor();
	}

	public final void setMinColor(Color minColor) {
		//this.minColor = minColor;
		scale.setMinColor(minColor);
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final Color getMidColor() {
		return scale.getMidColor();
	}

	public final void setMidColor(Color midColor) {
		//this.midColor = midColor;
		scale.setMidColor(midColor);
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final Color getMaxColor() {
		return scale.getMaxColor();
	}

	public final void setMaxColor(Color maxColor) {
		//this.maxColor = maxColor;
		scale.setMaxColor(maxColor);
		firePropertyChange(PROPERTY_CHANGED);
	}

	@Override
	public void decorate(ElementDecoration decoration, Object element) {
		decoration.reset();
		
		if (element == null) {
			decoration.setBgColor(Color.WHITE);
			decoration.setToolTip("Empty cell");
			return;
		}
		
		Object value = adapter.getValue(element, valueIndex);
		
		double v = TableUtils.doubleValue(value);

		final Color color = scale.getColor(v);
		
		decoration.setBgColor(color);
		decoration.setToolTip(fmt.pvalue(v));
	}
}
