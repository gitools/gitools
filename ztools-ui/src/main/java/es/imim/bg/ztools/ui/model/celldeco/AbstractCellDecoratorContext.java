package es.imim.bg.ztools.ui.model.celldeco;

import es.imim.bg.ztools.ui.model.AbstractModel;

public class AbstractCellDecoratorContext
		extends AbstractModel
		implements ITableDecoratorContext {

	private double zoom;
	private int valueIndex;
	
	public AbstractCellDecoratorContext() {
		zoom = 1.0;
		valueIndex = 0;
	}
	
	@Override
	public double getZoom() {
		return zoom;
	}

	@Override
	public void setZoom(double zoom) {
		this.zoom = zoom;
		firePropertyChange(ITableDecoratorContext.VALUE_CHANGED);
	}

	public int getValueIndex() {
		return valueIndex;
	}
	
	public void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
		firePropertyChange(ITableDecoratorContext.VALUE_CHANGED);
	}
}
