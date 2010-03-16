package org.gitools.model.decorator.impl;

import edu.upf.bg.colorscale.IColorScale;
import java.awt.Color;

import javax.xml.bind.annotation.XmlRootElement;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import org.gitools.matrix.MatrixUtils;
import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.matrix.model.element.IElementAdapter;

import edu.upf.bg.formatter.GenericFormatter;
import edu.upf.bg.colorscale.impl.BinaryColorScale;
import edu.upf.bg.colorscale.util.ColorConstants;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BinaryElementDecorator extends ElementDecorator {

	private static final long serialVersionUID = 8832886601133057329L;
	
	private int valueIndex;

	private BinaryColorScale scale;

	@XmlTransient
	private /*transient*/ GenericFormatter fmt;

	public BinaryElementDecorator() {
		this(null);
	}
	
	public BinaryElementDecorator(IElementAdapter adapter) {
		super(adapter);
	
		valueIndex = getPropertyIndex(new String[] {"value"});

		this.scale = new BinaryColorScale();

		fmt = new GenericFormatter("<");
	}

	/*@Override
	public Object clone() {
		BinaryElementDecorator obj = null;
		try {
			obj = (BinaryElementDecorator) super.clone();
			obj.fmt = new GenericFormatter("<");
		}
		catch (CloneNotSupportedException ex) { }
		return obj;
	}*/

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
	
	/*@Deprecated
	@Override
	public Map<String, String> getConfiguration() {
		
		Map<String, String> configuration = new HashMap <String, String>();
		
		configuration.put("valueIndex", Integer.toString(valueIndex));
		configuration.put("color",  "#"+Integer.toHexString(getColor().getRGB()));
		configuration.put("nonSignificantColor", "#"+Integer.toHexString(getNonSignificantColor().getRGB()));
		configuration.put("cutoff", Double.toString(getCutoff()));
		
		return configuration;
	}

	@Deprecated
	@Override
	public void setConfiguration(Map<String, String> configuration) {
		
		this.valueIndex = Integer.parseInt((String) configuration.get("valueIndex"));	
		setColor(Color.decode(configuration.get("color")));
		setNonSignificantColor(Color.decode(configuration.get("nonSignificantColor")));
		setCutoff(Double.parseDouble((String) configuration.get("cutoff")));
	}*/
}
