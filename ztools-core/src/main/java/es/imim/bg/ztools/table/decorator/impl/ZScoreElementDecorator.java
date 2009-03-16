package es.imim.bg.ztools.table.decorator.impl;

import java.awt.Color;

import es.imim.bg.GenericFormatter;
import es.imim.bg.colorscale.ZScoreColorScale;
import es.imim.bg.ztools.table.TableUtils;
import es.imim.bg.ztools.table.decorator.ElementDecoration;
import es.imim.bg.ztools.table.decorator.ElementDecorator;
import es.imim.bg.ztools.table.element.IElementAdapter;

public class ZScoreElementDecorator extends ElementDecorator {

	private int valueIndex;
	private int correctedValueIndex;
	private double sigHalfAmplitude;
	private ZScoreColorScale scale;
	
	private GenericFormatter fmt = new GenericFormatter("<");
	
	public ZScoreElementDecorator(IElementAdapter adapter) {
		super(adapter);
		
		valueIndex = getPropertyIndex(new String[] { "z-score" });
		correctedValueIndex = getPropertyIndex(new String[] { 
				"corrected-right-p-value", "corrected-p-value" });
		
		scale = new ZScoreColorScale();
		sigHalfAmplitude = scale.getSigHalfAmplitude();
	}

	public final int getValueIndex() {
		return valueIndex;
	}

	public final void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
	}

	public int getCorrectedValueIndex() {
		return correctedValueIndex;
	}
	
	public void setCorrectedValueIndex(int correctionValueIndex) {
		this.correctedValueIndex = correctionValueIndex;
	}
	
	public final double getSigHalfAmplitude() {
		return sigHalfAmplitude;
	}

	public final void setSigHalfAmplitude(double sigHalfAmplitude) {
		this.sigHalfAmplitude = sigHalfAmplitude;
	}

	public final ZScoreColorScale getScale() {
		return scale;
	}

	public final void setScale(ZScoreColorScale scale) {
		this.scale = scale;
	}

	@Override
	public void decorate(ElementDecoration decoration, Object element) {
		decoration.reset();
		
		if (element == null) {
			decoration.setBgColor(Color.WHITE);
			decoration.setToolTip("Void cell");
			return;
		}
		
		Object value = adapter.getValue(element, valueIndex);
		
		double v = TableUtils.doubleValue(value);
		
		final Color color = scale.getColor(v);
		
		decoration.setBgColor(color);
		decoration.setToolTip(fmt.pvalue(v));
	}
}
