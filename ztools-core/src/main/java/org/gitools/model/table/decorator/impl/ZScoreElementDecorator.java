package org.gitools.model.table.decorator.impl;

import java.awt.Color;

import org.gitools.model.table.TableUtils;
import org.gitools.model.table.decorator.ElementDecoration;
import org.gitools.model.table.decorator.ElementDecorator;
import org.gitools.model.table.element.IElementAdapter;

import cern.jet.stat.Probability;

import edu.upf.bg.GenericFormatter;
import edu.upf.bg.colorscale.ZScoreColorScale;
import edu.upf.bg.colorscale.util.ColorConstants;

public class ZScoreElementDecorator extends ElementDecorator {

	private int valueIndex;
	private int correctedValueIndex;
	private boolean useCorrection;
	private double significanceLevel;
	
	private ZScoreColorScale scale;
	
	private GenericFormatter fmt = new GenericFormatter("<");
	
	public ZScoreElementDecorator(IElementAdapter adapter) {
		super(adapter);
		
		valueIndex = getPropertyIndex(new String[] { "z-score" });
		correctedValueIndex = getPropertyIndex(new String[] { 
				"corrected-two-tail-p-value", "corrected-p-value" });
		
		useCorrection = false;
		significanceLevel = 0.05;
		
		scale = new ZScoreColorScale();
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

	public double getSignificanceLevel() {
		return significanceLevel;
	}
	
	public void setSignificanceLevel(double sigLevel) {
		this.significanceLevel = sigLevel;
		setSigHalfAmplitude(calculateSigHalfAmplitudeFromSigLevel(sigLevel));
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	private double calculateSigHalfAmplitudeFromSigLevel(double sigLevel) {
		double v = Probability.normalInverse(sigLevel);
		return Math.abs(v);
	}

	public final double getSigHalfAmplitude() {
		return scale.getSigHalfAmplitude();
	}

	public final void setSigHalfAmplitude(double sigHalfAmplitude) {
		scale.setSigHalfAmplitude(sigHalfAmplitude);
		firePropertyChange(PROPERTY_CHANGED);
	}

	public final ZScoreColorScale getScale() {
		return scale;
	}

	public final void setScale(ZScoreColorScale scale) {
		this.scale = scale;
		firePropertyChange(PROPERTY_CHANGED);
	}

	public Color getLeftMinColor() {
		return scale.getLeftMinColor();
	}
	
	public void setLeftMinColor(Color color) {
		scale.setLeftMinColor(color);
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getLeftMaxColor() {
		return scale.getLeftMaxColor();
	}
	
	public void setLeftMaxColor(Color color) {
		scale.setLeftMaxColor(color);
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getRightMinColor() {
		return scale.getRightMinColor();
	}
	
	public void setRightMinColor(Color color) {
		scale.setRightMinColor(color);
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getRightMaxColor() {
		return scale.getRightMaxColor();
	}
	
	public void setRightMaxColor(Color color) {
		scale.setRightMaxColor(color);
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
			
			useScale = cv <= significanceLevel;
		}
		
		final Color color = useScale ? scale.getColor(v) 
				: ColorConstants.nonSignificantColor;
		
		decoration.setBgColor(color);
		decoration.setToolTip(fmt.pvalue(v));
	}
}
