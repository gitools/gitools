package es.imim.bg.ztools.table.decorator.pvalue;

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
	private boolean useCorrectedScale;
	private double cutoff;
	private PValueColorScale scale;

	public PValueElementDecorator(IElementAdapter adapter) {
		super(adapter);
		
		valueIndex = getPropertyIndex(new String[] {
				"right-p-value", "p-value" });
		
		correctedValueIndex = getPropertyIndex(new String[] {
				"corrected-right-p-value", "corrected-p-value" });
		
		useCorrectedScale = false;
		cutoff = 0.05;
		scale = new PValueColorScale();
	}

	public final int getValueIndex() {
		return valueIndex;
	}

	public final void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
	}

	public final int getCorrectedValueIndex() {
		return correctedValueIndex;
	}

	public final void setCorrectedValueIndex(int correctedValueIndex) {
		this.correctedValueIndex = correctedValueIndex;
	}

	public final boolean isUseCorrectedScale() {
		return useCorrectedScale;
	}

	public final void setUseCorrectedScale(boolean useCorrectedScale) {
		this.useCorrectedScale = useCorrectedScale;
	}

	public final double getCutoff() {
		return cutoff;
	}

	public final void setCutoff(double cutoff) {
		this.cutoff = cutoff;
	}

	public final PValueColorScale getScale() {
		return scale;
	}

	public final void setScale(PValueColorScale scale) {
		this.scale = scale;
	}

	@Override
	public void decorate(
			ElementDecoration decoration,
			Object element) {
		
		if (element == null) {
			decoration.setBgColor(Color.WHITE);
			decoration.setToolTip("Void cell");
			return;
		}
		
		Object value = adapter.getValue(
				element, valueIndex);
		
		double v = TableUtils.doubleValue(value);
		
		boolean isSig = v <= cutoff;
		
		if (useCorrectedScale) {
			Object corrValue = correctedValueIndex >= 0 ?
					adapter.getValue(element, correctedValueIndex) : 0.0;
					
			double cv = TableUtils.doubleValue(corrValue);
			
			isSig = cv <= cutoff;
		}
		
		final Color color = isSig ? scale.getColor(v) 
				: ColorConstants.nonSignificantColor;
		
		final GenericFormatter fmt = new GenericFormatter("<");
		
		decoration.setBgColor(color);
		decoration.setToolTip(fmt.pvalue(v));
	}
}
