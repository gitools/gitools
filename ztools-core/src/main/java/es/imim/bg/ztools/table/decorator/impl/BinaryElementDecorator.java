package es.imim.bg.ztools.table.decorator.impl;

import java.awt.Color;

import es.imim.bg.GenericFormatter;
import es.imim.bg.colorscale.util.ColorConstants;
import es.imim.bg.ztools.datafilters.BinaryCutoffFilter;
import es.imim.bg.ztools.datafilters.BinaryCutoffFilter.BinaryCutoffCmp;
import es.imim.bg.ztools.table.TableUtils;
import es.imim.bg.ztools.table.decorator.ElementDecoration;
import es.imim.bg.ztools.table.decorator.ElementDecorator;
import es.imim.bg.ztools.table.element.IElementAdapter;

public class BinaryElementDecorator extends ElementDecorator {

	private static final Color defaultColor = new Color(20, 120, 230);
	
	private int valueIndex;
	private double cutoff;
	private BinaryCutoffCmp cutoffCmp;
	private Color color;
	
	private GenericFormatter fmt = new GenericFormatter("<");
	
	public BinaryElementDecorator(IElementAdapter adapter) {
		super(adapter);
	
		valueIndex = getPropertyIndex(new String[] {
				"value" });
		
		cutoff = 1.0;
		
		cutoffCmp = BinaryCutoffFilter.EQ;
		
		color = defaultColor;
	}

	public int getValueIndex() {
		return valueIndex;
	}
	
	public void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public BinaryCutoffCmp getCutoffCmp() {
		return cutoffCmp;
	}
	
	public void setCutoffCmp(BinaryCutoffCmp cutoffCmp) {
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
	
	@Override
	public void decorate(
			ElementDecoration decoration, 
			Object element) {
		
		decoration.reset();
		
		if (element == null) {
			decoration.setBgColor(Color.WHITE);
			decoration.setToolTip("Empty cell");
			return;
		}
		
		Object value = adapter.getValue(
				element, valueIndex);
		
		double v = TableUtils.doubleValue(value);
		
		boolean isSig = cutoffCmp.compare(v, cutoff);
		
		final Color c = isSig ? color 
				: ColorConstants.nonSignificantColor;
		
		decoration.setBgColor(c);
		decoration.setToolTip(fmt.format(v));
	}
}
