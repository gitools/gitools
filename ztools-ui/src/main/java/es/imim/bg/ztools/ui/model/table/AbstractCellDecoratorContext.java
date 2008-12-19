package es.imim.bg.ztools.ui.model.table;

public class AbstractCellDecoratorContext implements ITableDecoratorContext {

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
	}

	public int getValueIndex() {
		return valueIndex;
	}
	
	public void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
	}
}
