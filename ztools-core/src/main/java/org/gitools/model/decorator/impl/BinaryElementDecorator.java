package org.gitools.model.decorator.impl;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.gitools.datafilters.CutoffCmp;
import org.gitools.matrix.MatrixUtils;
import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.matrix.element.IElementAdapter;

import edu.upf.bg.GenericFormatter;
import edu.upf.bg.colorscale.util.ColorConstants;

@XmlRootElement
public class BinaryElementDecorator extends ElementDecorator {

	private static final long serialVersionUID = 8832886601133057329L;

	private static final Color defaultColor = new Color(20, 120, 250);
	
	private int valueIndex;
	
	private double cutoff;
	
	private CutoffCmp cutoffCmp;
	
	private Color color;
	private Color nonSignificantColor;
	
	private GenericFormatter fmt = new GenericFormatter("<");
	
	
	public BinaryElementDecorator() {
		
		valueIndex = getPropertyIndex(new String[] {
				"value" });
		
		cutoff = 1.0;
		
		cutoffCmp = CutoffCmp.EQ;
		
		color = defaultColor;
		nonSignificantColor = ColorConstants.nonSignificantColor;
	}
	
	
	public BinaryElementDecorator(IElementAdapter adapter) {
		super(adapter);
	
		valueIndex = getPropertyIndex(new String[] {
				"value" });
		
		cutoff = 1.0;
		
		cutoffCmp = CutoffCmp.EQ;
		
		color = defaultColor;
		nonSignificantColor = ColorConstants.nonSignificantColor;
	}

	public int getValueIndex() {
		return valueIndex;
	}
	
	public void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public CutoffCmp getCutoffCmp() {
		return cutoffCmp;
	}
	
	public void setCutoffCmp(CutoffCmp cutoffCmp) {
		this.cutoffCmp = cutoffCmp;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public double getCutoff() {
		return cutoff;
	}
	
	public void setCutoff(double cutoff) {
		this.cutoff = cutoff;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getNonSignificantColor() {
		return nonSignificantColor;
	}
	
	public void setNonSignificantColor(Color color) {
		this.nonSignificantColor = color;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	@Override
	public void decorate(
			ElementDecoration decoration, 
			Object element) {
		
		decoration.reset();
		
		if (element == null) {
			decoration.setBgColor(ColorConstants.emptyColor);
			decoration.setToolTip("Empty cell");
			return;
		}
		
		Object value = adapter.getValue(element, valueIndex);
		
		double v = MatrixUtils.doubleValue(value);
		
		boolean isSig = cutoffCmp.compare(v, cutoff);
		
		final Color c = Double.isNaN(v) ? ColorConstants.notANumberColor
				: isSig ? color : nonSignificantColor;
		
		decoration.setBgColor(c);
		decoration.setToolTip(fmt.format(v));
	}

	@Override
	public Map<String, String> getConfiguration() {
		
		Map<String, String> configuration = new HashMap <String, String>();
		
		configuration.put("valueIndex", Integer.toString(valueIndex));
		configuration.put("color",  "#"+Integer.toHexString(color.getRGB()));
		configuration.put("nonSignificantColor", "#"+Integer.toHexString(nonSignificantColor.getRGB()));;
		configuration.put("cutoff", Double.toString(cutoff));
		
		return configuration;
	}

	@Override
	public void setConfiguration(Map<String, String> configuration) {
		
		this.valueIndex = Integer.parseInt((String) configuration.get("valueIndex"));	
		this.color = Color.decode(configuration.get("color"));
		this.nonSignificantColor = Color.decode(configuration.get("nonSignificantColor"));
		this.cutoff = Double.parseDouble((String) configuration.get("cutoff"));
	}
}
