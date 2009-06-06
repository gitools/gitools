package es.imim.bg.ztools.table.decorator.impl;

import java.awt.Color;

import es.imim.bg.GenericFormatter;
import es.imim.bg.colorscale.PValueColorScale;
import es.imim.bg.colorscale.util.ColorConstants;
import es.imim.bg.ztools.table.TableUtils;
import es.imim.bg.ztools.table.decorator.ElementDecoration;
import es.imim.bg.ztools.table.decorator.ElementDecorator;
import es.imim.bg.ztools.table.element.IElementAdapter;

public class PValueElementDecorator extends ElementDecorator {

	private int valueIndex;
	private int correctedValueIndex;
	private boolean useCorrection;
	private double significanceLevel;
	private PValueColorScale scale;

	private GenericFormatter fmt = new GenericFormatter("<");
	
	public PValueElementDecorator(IElementAdapter adapter) {
		super(adapter);
		
		valueIndex = getPropertyIndex(new String[] {
				"right-p-value", "p-value" });
		
		correctedValueIndex = getPropertyIndex(new String[] {
				"corrected-right-p-value", "corrected-p-value" });
		
		useCorrection = false;
		
		significanceLevel = 0.05;
		scale = new PValueColorScale();
	}

	public final int getValueIndex() {
		return valueIndex;
	}

	public final void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final int getCorrectedValueIndex() {
		return correctedValueIndex;
	}

	public final void setCorrectedValueIndex(int correctedValueIndex) {
		this.correctedValueIndex = correctedValueIndex;
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final boolean getUseCorrection() {
		return useCorrection;
	}

	public final void setUseCorrection(boolean useCorrectedScale) {
		this.useCorrection = useCorrectedScale;
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final double getSignificanceLevel() {
		return significanceLevel;
	}

	public final void setSignificanceLevel(double significanceLevel) {
		this.significanceLevel = significanceLevel;
		scale.setSignificanceLevel(significanceLevel);
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final PValueColorScale getScale() {
		return scale;
	}

	public final void setScale(PValueColorScale scale) {
		this.scale = scale;
		firePropertyChange(PROPERTY_CHANGED);
	}

	public Color getMinColor() {
		return scale.getMinColor();
	}

	public void setMinColor(Color color) {
		scale.setMinColor(color);
		firePropertyChange(PROPERTY_CHANGED);
	}

	public Color getMaxColor() {
		return scale.getMaxColor();
	}

	public void setMaxColor(Color color) {
		scale.setMaxColor(color);
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
		
		Object value = adapter.getValue(
				element, valueIndex);
		
		double v = TableUtils.doubleValue(value);
		
		boolean isSig = v <= significanceLevel;
		
		if (useCorrection) {
			Object corrValue = correctedValueIndex >= 0 ?
					adapter.getValue(element, correctedValueIndex) : 0.0;
					
			double cv = TableUtils.doubleValue(corrValue);
			
			isSig = cv <= significanceLevel;
		}
		
		final Color color = isSig ? scale.getColor(v) 
				: ColorConstants.nonSignificantColor;
		
		decoration.setBgColor(color);
		decoration.setToolTip(fmt.pvalue(v));
	}
}
