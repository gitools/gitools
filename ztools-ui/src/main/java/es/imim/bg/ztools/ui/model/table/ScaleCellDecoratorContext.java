package es.imim.bg.ztools.ui.model.table;

import es.imim.bg.colorscale.ColorScale;
import es.imim.bg.colorscale.LogColorScale;


public class ScaleCellDecoratorContext 
		extends AbstractCellDecoratorContext {
	
	private ColorScale scale;
	
	public ScaleCellDecoratorContext() {
		scale = new LogColorScale();
	}
	
	public ColorScale getScale() {
		return scale;
	}
	
	public void setScale(ColorScale scale) {
		this.scale = scale;
	}
}
