package es.imim.bg.ztools.ui.model.celldeco;

import es.imim.bg.colorscale.ColorScale;
import es.imim.bg.colorscale.PValueColorScale;

public class ScaleCellDecoratorContext 
		extends AbstractCellDecoratorContext {
	
	private int correctedValueIndex;
	private boolean useCorrectedScale;
	private double cutoff;
	private ColorScale scale;
	
	public ScaleCellDecoratorContext() {
		super();
		
		correctedValueIndex = -1;
		useCorrectedScale = false;
		cutoff = 0.05;
		scale = new PValueColorScale();
	}
	
	public int getCorrectedValueIndex() {
		return correctedValueIndex;
	}
	
	public void setCorrectedValueIndex(int correctedValueIndex) {
		this.correctedValueIndex = correctedValueIndex;
		firePropertyChange(ITableDecoratorContext.VALUE_CHANGED);
	}
	
	public boolean isUseCorrectedScale() {
		return useCorrectedScale;
	}
	
	public void setUseCorrectedScale(boolean useCorrectedScale) {
		this.useCorrectedScale = useCorrectedScale;
		firePropertyChange(ITableDecoratorContext.VALUE_CHANGED);
	}
	
	public double getCutoff() {
		return cutoff;
	}
	
	public void setCutoff(double cutoff) {
		this.cutoff = cutoff;
		firePropertyChange(ITableDecoratorContext.VALUE_CHANGED);
	}
	
	public ColorScale getScale() {
		return scale;
	}
	
	public void setScale(ColorScale scale) {
		this.scale = scale;
		firePropertyChange(ITableDecoratorContext.VALUE_CHANGED);
	}
}
