package org.gitools.model.decorator.impl;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gitools.matrix.MatrixUtils;
import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.matrix.model.element.IElementAdapter;

import cern.jet.stat.Probability;
import edu.upf.bg.formatter.GenericFormatter;
import edu.upf.bg.colorscale.IColorScale;
import edu.upf.bg.colorscale.impl.ZScoreColorScale;
import edu.upf.bg.colorscale.util.ColorConstants;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ZScoreElementDecorator extends ElementDecorator {

	private static final long serialVersionUID = -7623938918947195891L;

	private int valueIndex;
	private int correctedValueIndex;
	private boolean useCorrection;
	private double significanceLevel;
	
	private ZScoreColorScale scale;

	@XmlTransient
	private GenericFormatter fmt = new GenericFormatter("<");
	
	
	public ZScoreElementDecorator(){
	
		valueIndex = getPropertyIndex(new String[] { "z-score" });
		correctedValueIndex = getPropertyIndex(new String[] { 
				"corrected-two-tail-p-value", "corrected-p-value" });
		
		useCorrection = false;
		significanceLevel = 0.05;
		
		scale = new ZScoreColorScale();
	}
	
	
	public ZScoreElementDecorator(IElementAdapter adapter) {
		super(adapter);
		
		valueIndex = getPropertyIndex(new String[] { "z-score" });
		correctedValueIndex = getPropertyIndex(new String[] { 
				"corrected-two-tail-p-value", "corrected-p-value" });
		
		useCorrection = false;
		significanceLevel = 0.05;
		
		scale = new ZScoreColorScale();
	}

	/*@Override
	public Object clone() {
		ZScoreElementDecorator obj = null;
		try {
			obj = (ZScoreElementDecorator) super.clone();
			obj.scale = scale.clone();
			obj.fmt = new GenericFormatter("<");
		}
		catch (CloneNotSupportedException ex) { }
		return obj;
	}*/

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

	public final ZScoreColorScale getZScoreScale() {
		return scale;
	}

	public final void setScale(ZScoreColorScale scale) {
		this.scale = scale;
		firePropertyChange(PROPERTY_CHANGED);
	}

	public Color getLeftMinColor() {
		return scale.getMin().getColor();
	}
	
	public void setLeftMinColor(Color color) {
		scale.getMin().setColor(color);
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getLeftMaxColor() {
		return scale.getCenter().getLeftColor();
	}
	
	public void setLeftMaxColor(Color color) {
		scale.getCenter().setLeftColor(color);
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getRightMinColor() {
		return scale.getCenter().getRightColor();
	}
	
	public void setRightMinColor(Color color) {
		scale.getCenter().setRightColor(color);
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getRightMaxColor() {
		return scale.getMax().getColor();
	}
	
	public void setRightMaxColor(Color color) {
		scale.getMax().setColor(color);
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getNonSignificantColor() {
		return scale.getNonSignificantColor();
	}
	
	public void setNonSignificantColor(Color color) {
		scale.setNonSignificantColor(color);
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
		
		boolean useScale = true;
		
		if (useCorrection) {
			Object corrValue = correctedValueIndex >= 0 ?
					adapter.getValue(element, correctedValueIndex) : 0.0;
					
			double cv = MatrixUtils.doubleValue(corrValue);
			
			useScale = cv <= significanceLevel;
		}
		
		final Color color = useScale ? scale.valueColor(v) 
				: ColorConstants.nonSignificantColor;
		
		decoration.setBgColor(color);
		decoration.setToolTip(fmt.pvalue(v));
	}

	@Override
	public IColorScale getScale() {
		return scale;
	}
	
	@Deprecated
	@Override
	public Map<String, String> getConfiguration() {
		
		Map<String, String> configuration = new HashMap <String, String>();
		
		configuration.put("valueIndex", Integer.toString(valueIndex));
		configuration.put("correctedValueIndex", Integer.toString(correctedValueIndex));
		configuration.put("useCorrection", Boolean.toString(useCorrection));
		configuration.put("significanceLevel", Double.toString(significanceLevel));
		
		return configuration;
	}

	@Deprecated
	@Override
	public void setConfiguration(Map<String, String> configuration) {
	
		this.valueIndex = Integer.parseInt((String) configuration.get("valueIndex"));	
		this.correctedValueIndex = Integer.parseInt((String) configuration.get("correctedValueIndex"));
		this.useCorrection = Boolean.parseBoolean ((String) configuration.get("useCorrection"));
		this.significanceLevel = Double.parseDouble((String) configuration.get("significanceLevel"));
	}
}
