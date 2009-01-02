package es.imim.bg.ztools.ui.model.celldeco;

import java.awt.Component;

import es.imim.bg.ztools.ui.panels.table.TablePanelCellDecorator;

public interface ITableDecorator extends TablePanelCellDecorator {

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
