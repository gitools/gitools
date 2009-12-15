package org.gitools.model.decorator.impl;

import edu.upf.bg.colorscale.IColorScale;
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
import edu.upf.bg.colorscale.impl.BinaryColorScale;
import edu.upf.bg.colorscale.util.ColorConstants;

@XmlRootElement
public class BinaryElementDecorator extends ElementDecorator {

	private static final long serialVersionUID = 8832886601133057329L;
	
	private int valueIndex;

	private BinaryColorScale scale;

	private transient GenericFormatter fmt = new GenericFormatter("<");

	public BinaryElementDecorator() {
		this(null);
	}
	
	
	public BinaryElementDecorator(IElementAdapter adapter) {
		super(adapter);
	
		valueIndex = getPropertyIndex(new String[] {"value"});

		this.scale = new BinaryColorScale();
	}

	public int getValueIndex() {
		return valueIndex;
	}
	
	public void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public CutoffCmp getCutoffCmp() {
		return scale.getCutoffCmp();
	}
	
	public void setCutoffCmp(CutoffCmp cutoffCmp) {
		this.scale.setCutoffCmp(cutoffCmp);
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public double getCutoff() {
		return scale.getCutoff().getValue();
	}
	
	public void setCutoff(double cutoff) {
		this.scale.getCutoff().setValue(cutoff);
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getColor() {
		return scale.getMax().getColor();
	}
	
	public void setColor(Color color) {
		this.scale.getMax().setColor(color);
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getNonSignificantColor() {
		return scale.getMin().getColor();
	}
	
	public void setNonSignificantColor(Color color) {
		this.scale.getMin().setColor(color);
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
		
		final Color c = scale.valueColor(v);
		
		decoration.setBgColor(c);
		decoration.setToolTip(fmt.format(v));
	}

	@Override
	public IColorScale getScale() {
		return scale;
	}
	
	//FIXME scale comparison state
	@Override
	public Map<String, String> getConfiguration() {
		
		Map<String, String> configuration = new HashMap <String, String>();
		
		configuration.put("valueIndex", Integer.toString(valueIndex));
		configuration.put("color",  "#"+Integer.toHexString(getColor().getRGB()));
		configuration.put("nonSignificantColor", "#"+Integer.toHexString(getNonSignificantColor().getRGB()));
		configuration.put("cutoff", Double.toString(getCutoff()));
		
		return configuration;
	}

	@Override
	public void setConfiguration(Map<String, String> configuration) {
		
		this.valueIndex = Integer.parseInt((String) configuration.get("valueIndex"));	
		setColor(Color.decode(configuration.get("color")));
		setNonSignificantColor(Color.decode(configuration.get("nonSignificantColor")));
		setCutoff(Double.parseDouble((String) configuration.get("cutoff")));
	}
}
