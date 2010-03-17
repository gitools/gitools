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
		int old = this.valueIndex;
		this.valueIndex = valueIndex;
		firePropertyChange(PROPERTY_CHANGED, old, valueIndex);
	}
	
	public CutoffCmp getCutoffCmp() {
		return scale.getCutoffCmp();
	}
	
	public void setCutoffCmp(CutoffCmp cutoffCmp) {
		CutoffCmp old = scale.getCutoffCmp();
		this.scale.setCutoffCmp(cutoffCmp);
		firePropertyChange(PROPERTY_CHANGED, old, cutoffCmp);
	}
	
	public double getCutoff() {
		return scale.getCutoff().getValue();
	}
	
	public void setCutoff(double cutoff) {
		double old = scale.getCutoff().getValue();
		this.scale.getCutoff().setValue(cutoff);
		firePropertyChange(PROPERTY_CHANGED, old, cutoff);
	}
	
	public Color getColor() {
		return scale.getMax().getColor();
	}
	
	public void setColor(Color color) {
		Color old = scale.getMax().getColor();
		this.scale.getMax().setColor(color);
		firePropertyChange(PROPERTY_CHANGED, old, color);
	}
	
	public Color getNonSignificantColor() {
		return scale.getMin().getColor();
	}
	
	public void setNonSignificantColor(Color color) {
		Color old = scale.getMin().getColor();
		this.scale.getMin().setColor(color);
		firePropertyChange(PROPERTY_CHANGED, old, color);
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
}
