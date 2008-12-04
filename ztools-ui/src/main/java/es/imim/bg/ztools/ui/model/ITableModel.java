package es.imim.bg.ztools.ui.model;

import java.util.List;

import cern.colt.matrix.DoubleMatrix2D;

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
	
	String MATRIX_CHANGED_PROPERTY = "matrixChanged";

	int getColumnCount();
	String getColumnName(int column);

	int getRowCount();
	String getRowName(int row);

	double getValue(int column, int row);
	DoubleMatrix2D getVisibleMatrix();
	DoubleMatrix2D getMatrix();

	int[] getVisibleColumns();
	void setVisibleColumns(int[] indices);
	void hideColumns(int[] indices);
	
	int[] getVisibleRows();
	void setVisibleRows(int[] indices);
	void hideRows(int[] indices);

	void setSelectionMode(SelectionMode mode);
	SelectionMode getSelectionMode();

	int[] getSelectedColumns();
	int[] getSelectedRows();

	void setSelection(
			int[] selectedColumns,
			int[] selectedRows);
	
	void setLeadSelection(int column, int row);
	int getSelectedLeadColumn();
	int getSelectedLeadRow();
	
	void selectAll();
	void resetSelection();
	
	void invertSelection();
	
	String getHtmlInfo();
	void setHtmlInfo(String htmlInfo);

	void sort(final List<SortCriteria> criteriaList);

	void sortByFunc(final int[] selCols);

	CellDecorationConfig getCellDecoration();
	void setCellDecoration(CellDecorationConfig cellDecoration);

	void moveRowsUp(int[] selectedRows);
	void moveRowsDown(int[] selectedRows);
	void moveColsLeft(int[] selectedColumns);
	void moveColsRight(int[] selectedColumns);
	
	void fireMatrixChanged();
}