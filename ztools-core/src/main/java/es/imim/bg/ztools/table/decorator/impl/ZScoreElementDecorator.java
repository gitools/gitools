package es.imim.bg.ztools.table.decorator.impl;

import java.awt.Color;

import es.imim.bg.GenericFormatter;
import es.imim.bg.colorscale.ZScoreColorScale;
import es.imim.bg.colorscale.util.ColorConstants;
import es.imim.bg.ztools.table.TableUtils;
import es.imim.bg.ztools.table.decorator.ElementDecoration;
import es.imim.bg.ztools.table.decorator.ElementDecorator;
import es.imim.bg.ztools.table.element.IElementAdapter;

public class ZScoreElementDecorator extends ElementDecorator {

	private int valueIndex;
	private int correctedValueIndex;
	private boolean useCorrection;
	private double correctedValueCutoff;
	
	private double sigHalfAmplitude;
	private ZScoreColorScale scale;
	
	private GenericFormatter fmt = new GenericFormatter("<");
	
	public ZScoreElementDecorator(IElementAdapter adapter) {
		super(adapter);
		
		valueIndex = getPropertyIndex(new String[] { "z-score" });
		correctedValueIndex = getPropertyIndex(new String[] { 
				"corrected-two-tail-p-value", "corrected-p-value" });
		
		useCorrection = false;
		correctedValueCutoff = 0.05;
		
		scale = new ZScoreColorScale();
		sigHalfAmplitude = scale.getSigHalfAmplitude();
	}

	public final int getValueIndex() {
		return valueIndex;
	}

	public final void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
		firePropertyChange(PROPERTY_CHANGED);
	}

	public int getCorrectedValueIndex() {
		return correctedValueIndex;
	}
	
	public void setCorrectedValueIndex(int correctionValueIndex) {
		this.correctedValueIndex = correctionValueIndex;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public final boolean getUseCorrection() {
		return useCorrection;
	}

	public final void setUseCorrection(boolean useCorrectedScale) {
		this.useCorrection = useCorrectedScale;
		firePropertyChange(PROPERTY_CHANGED);
	}

	public double getCorrectedValueCutoff() {
		return correctedValueCutoff;
	}
	
	public void setCorrectedValueCutoff(double correctedValueCutoff) {
		this.correctedValueCutoff = correctedValueCutoff;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public final double getSigHalfAmplitude() {
		return sigHalfAmplitude;
	}

	public final void setSigHalfAmplitude(double sigHalfAmplitude) {
		this.sigHalfAmplitude = sigHalfAmplitude;
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final ZScoreColorScale getScale() {
		return scale;
	}

	public final void setScale(ZScoreColorScale scale) {
		this.scale = scale;
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
		
		double v = TableUtils.doubleValue(value);
		
		boolean useScale = true;
		
		if (useCorrection) {
			Object corrValue = correctedValueIndex >= 0 ?
					adapter.getValue(element, correctedValueIndex) : 0.0;
					
			double cv = TableUtils.doubleValue(corrValue);
			
			useScale = cv <= correctedValueCutoff;
		}
		
		final Color color = useScale ? scale.getColor(v) 
				: ColorConstants.nonSignificantColor;
		
		decoration.setBgColor(color);
		decoration.setToolTip(fmt.pvalue(v));
	}
}
