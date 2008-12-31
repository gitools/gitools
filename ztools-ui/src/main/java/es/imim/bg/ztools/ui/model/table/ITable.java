package es.imim.bg.ztools.ui.model.table;

import es.imim.bg.ztools.ui.model.IModel;

public interface ITable extends IModel, ITableContents {
	
	// events
	
	String VISIBLE_COLUMNS_CHANGED = "visibleColsChanged";
	String VISIBLE_ROWS_CHANGED = "visibleRowsChanged";
	
	String SELECTED_LEAD_CHANGED = "selectionLead";
	String SELECTION_CHANGED = "selectionChanged";
	//String SELECTED_COLUMNS_CHANGED = "selectionColumns";
	//String SELECTED_ROWS_CHANGED = "selectionRows";
	
	String CELL_VALUE_CHANGED = "cellValueChanged";
	
	String CELL_DECORATION_CONTEXT_CHANGED = "cellDecorationContextChanged";

	// contents
	
	ITableContents getContents();
	
	// visibility
	
	int[] getVisibleRows();
	void setVisibleRows(int[] indices);
	
	int[] getVisibleColumns();
	void setVisibleColumns(int[] indices);
	
	void moveRowsUp(int[] indices);
	void moveRowsDown(int[] indices);
	void moveColumnsLeft(int[] indices);
	void moveColumnsRight(int[] indices);
	
	// selection
	
	int[] getSelectedRows();
	void setSelectedRows(int[] indices);
	
	int[] getSelectedColumns();
	void setSelectedColumns(int[] indices);
	
	void selectAll();
	void invertSelection();
	void clearSelection();
	
	int getSelectionLeadRow();
	int getSelectionLeadColumn();
	void setLeadSelection(int row, int column);
	
	// properties
	
	/*int getCurrentProperty();
	void setCurrentProperty(int index);*/
	
	// cell decoration

	ITableDecoratorContext getCellDecoratorContext();
	void setCellDecoratorContext(ITableDecoratorContext decoratorContext);
}
