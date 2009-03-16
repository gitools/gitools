package es.imim.bg.ztools.table;

import es.imim.bg.ztools.table.element.IElementAdapter;

public interface ITableContents {

	// rows
	
	int getRowCount();
	Object getRow(int index);
	IElementAdapter getRowAdapter();
	
	// columns
	
	int getColumnCount();
	Object getColumn(int index);
	IElementAdapter getColumnAdapter();
	
	// cells
	
	Object getCell(int row, int column);
	Object getCellValue(int row, int column, int index);
	Object getCellValue(int row, int column, String id);
	void setCellValue(int row, int column, int index, Object value);
	void setCellValue(int row, int column, String id, Object value);
	
	IElementAdapter getCellAdapter();
}
