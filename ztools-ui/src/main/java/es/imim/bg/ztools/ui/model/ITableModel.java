package es.imim.bg.ztools.ui.model;

import java.util.List;

public interface ITableModel extends IModel {
	
	String HTML_INFO_PROPERTY = "htmlInfo";
	String SELECTION_MODE_PROPERTY = "selectionMode";
	String SELECTION_LEAD_PROPERTY = "selectionLead";
	String SELECTION_COLUMNS_PROPERTY = "selectionColumns";
	String SELECTION_ROWS_PROPERTY = "selectionRows";

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
	void setSelectedColumns(int[] selectedColumns);

	int[] getSelectedRows();
	void setSelectedRows(int[] selectedRows);

	void setLeadSelection(int column, int row);
	int getSelectedLeadColumn();
	int getSelectedLeadRow();
	
	void resetSelection();
	
	String getHtmlInfo();

	void setHtmlInfo(String htmlInfo);

	void sort(final List<SortCriteria> criteriaList);

	void sortByFunc(final int[] selCols, final int paramIndex);

	void selectAll();

}