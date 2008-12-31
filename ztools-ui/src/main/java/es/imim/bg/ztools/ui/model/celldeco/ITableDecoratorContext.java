package es.imim.bg.ztools.ui.model.celldeco;

import es.imim.bg.ztools.ui.model.IModel;

public interface ITableDecoratorContext extends IModel {

	String VALUE_CHANGED = "valueChanged";
	
	// zoom
	
	double getZoom();
	void setZoom(double zoom);
}
