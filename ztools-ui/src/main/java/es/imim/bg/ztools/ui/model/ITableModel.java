package es.imim.bg.ztools.ui.model;

import java.util.List;

import es.imim.bg.ztools.ui.colormatrix.CellDecorationConfig;

public interface ITableModel extends IModel {
	
	String HTML_INFO_PROPERTY = "htmlInfo";
	
	String COLS_CHANGED_PROPERTY = "colsChanged";
	String ROWS_CHANGED_PROPERTY = "rowsChanged";
	
	String SELECTION_MODE_PROPERTY = "selectionMode";
	String SELECTION_LEAD_PROPERTY = "selectionLead";
	String SELECTION_COLUMNS_PROPERTY = "selectionColumns";
	String SELECTION_ROWS_PROPERTY = "selectionRows";
	String SELECTION_PROPERTY = "selection";
	
	String SELECTION_ALL_PROPERTY = "selectionAll";
		
	String CELL_DECORATION_PROPERTY = "cellDecoration";

	int getColumnCount();

	String getColumnName(int column);

	int getRowCount();

	String getRowName(int row);

	double getValue(int column, int row);

	void removeColumns(int[] indices);

	void removeRows(int[] indices);

	void setSelectionMode(SelectionMode mode);
	SelectionMode getSelectionMode();

	int[] getSelectedColumns();
	//void setSelectedColumns(int[] selectedColumns);

	int[] getSelectedRows();
	//void setSelectedRows(int[] selectedRows);

	void setSelection(
			int[] selectedColumns,
			int[] selectedRows);
	
	void setLeadSelection(int column, int row);
	int getSelectedLeadColumn();
	int getSelectedLeadRow();
	
	void resetSelection();
	
	String getHtmlInfo();

	void setHtmlInfo(String htmlInfo);

	void sort(final List<SortCriteria> criteriaList);

	void sortByFunc(final int[] selCols);

	void selectAll();

	CellDecorationConfig getCellDecoration();
	void setCellDecoration(CellDecorationConfig cellDecoration);

	void moveRowsUp(int[] selectedRows);
	void moveRowsDown(int[] selectedRows);
	void moveColsLeft(int[] selectedColumns);
	void moveColsRight(int[] selectedColumns);
}