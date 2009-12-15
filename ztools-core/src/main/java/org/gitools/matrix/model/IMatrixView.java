package org.gitools.matrix.model;

import org.gitools.model.IModel;

public interface IMatrixView extends IModel, IMatrix {
	
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
	
	IMatrix getContents();
	
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
	boolean isRowSelected(int index);

	int[] getSelectedColumns();
	void setSelectedColumns(int[] indices);
	boolean isColumnSelected(int index);
	
	void selectAll();
	void invertSelection();
	void clearSelection();
	
	int getSelectionLeadRow();
	int getSelectionLeadColumn();
	void setLeadSelection(int row, int column);
	
	// properties
	
	int getSelectedPropertyIndex();
	void setSelectedPropertyIndex(int index);
}
