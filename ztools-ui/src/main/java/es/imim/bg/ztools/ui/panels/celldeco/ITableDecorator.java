package es.imim.bg.ztools.ui.panels.celldeco;

import java.awt.Component;

import es.imim.bg.ztools.ui.model.celldeco.ITableDecoratorContext;
import es.imim.bg.ztools.ui.panels.table.ColorMatrixCellDecorator;

public interface ITableDecorator extends ColorMatrixCellDecorator {

	// context
	
	ITableDecoratorContext getContext();
	void setContext(ITableDecoratorContext context);
	
	// size
	
	int getMinimumWidth();
	int getPreferredWidth();
	int getMaximumWidth();
	
	int getMinimumHeight();
	int getPreferredHeight();
	int getMaximumHeight();
	
	// config panel
	
	Component createConfigurationComponent();
}
