package es.imim.bg.ztools.ui.views.table;

import javax.swing.JPanel;

import es.imim.bg.ztools.ui.colormatrix.ColorMatrixCellDecorator;
import es.imim.bg.ztools.ui.model.table.ITableDecoratorContext;

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
	
	JPanel createConfigurationPanel();
}
