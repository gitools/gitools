package org.gitools.model.decorator.impl;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.gitools.matrix.MatrixUtils;
import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.matrix.model.element.IElementAdapter;

import edu.upf.bg.formatter.GenericFormatter;
import edu.upf.bg.colorscale.IColorScale;
import edu.upf.bg.colorscale.impl.LinearTwoSidedColorScale;
import edu.upf.bg.colorscale.util.ColorConstants;

@XmlAccessorType(XmlAccessType.FIELD)
public class LinearTwoSidedElementDecorator extends ElementDecorator {

	private static final long serialVersionUID = -181427286948958314L;

	private int valueIndex;
	@XmlTransient
	private LinearTwoSidedColorScale scale;
	@XmlTransient
	private GenericFormatter fmt = new GenericFormatter("<");
	
	
	public LinearTwoSidedElementDecorator(){
		
		valueIndex = getPropertyIndex(new String[] {
				"value", "log2ratio" });
		
		scale = new LinearTwoSidedColorScale();

	}
	
	
	public LinearTwoSidedElementDecorator(IElementAdapter adapter) {
		super(adapter);
		
		valueIndex = getPropertyIndex(new String[] {
				"value", "log2ratio" });
		
		scale = new LinearTwoSidedColorScale();
	}

	public final int getValueIndex() {
		return valueIndex;
	}

	public final void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final double getMinValue() {
		return scale.getMin().getValue();
	}

	public final void setMinValue(double minValue) {
		scale.getMin().setValue(minValue);
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final double getMidValue() {
		return scale.getMid().getValue();
	}

	public final void setMidValue(double midValue) {
		scale.getMid().setValue(midValue);
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final double getMaxValue() {
		return scale.getMax().getValue();
	}

	public final void setMaxValue(double maxValue) {
		scale.getMax().setValue(maxValue);
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final Color getMinColor() {
		return scale.getMin().getColor();
	}

	public final void setMinColor(Color minColor) {
		scale.getMin().setColor(minColor);
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final Color getMidColor() {
		return scale.getMid().getColor();
	}

	public final void setMidColor(Color midColor) {
		scale.getMid().setColor(midColor);
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final Color getMaxColor() {
		return scale.getMax().getColor();
	}

	public final void setMaxColor(Color maxColor) {
		scale.getMax().setColor(maxColor);
		firePropertyChange(PROPERTY_CHANGED);
	}

	@Override
	public void decorate(ElementDecoration decoration, Object element) {
		decoration.reset();
		
		if (element == null) {
			decoration.setBgColor(ColorConstants.emptyColor);
			decoration.setToolTip("Empty cell");
			return;
		}
		
		Object value = adapter.getValue(element, valueIndex);
		
		double v = MatrixUtils.doubleValue(value);

		final Color color = scale.valueColor(v);
		
		decoration.setBgColor(color);
		decoration.setToolTip(fmt.pvalue(v));
	}

	@Override
	public IColorScale getScale() {
		return scale;
	}

	//FIXME scale configuration missing
	@Override
	public Map<String, String> getConfiguration() {
		
		Map<String, String> configuration = new HashMap <String, String>();
		configuration.put("valueIndex;", Integer.toString(valueIndex));
		return configuration;
	}

	@Override
	public void setConfiguration(Map<String, String> configuration) {
		this.valueIndex = Integer.parseInt((String) configuration.get("valueIndex"));	
	}
}
