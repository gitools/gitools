package es.imim.bg.ztools.ui.model.table;

import es.imim.bg.ztools.model.elements.ElementFacade;

public interface ITableContents {

	// rows
	
	int getRowCount();
	Object getRow(int index);
	ElementFacade getRowsFacade();
	
	// columns
	
	int getColumnCount();
	Object getColumn(int index);
	ElementFacade getColumnsFacade();
	
	// cells
	
	Object getCell(int row, int column);
	Object getCellValue(int row, int column, int index);
	Object getCellValue(int row, int column, String id);
	void setCellValue(int row, int column, int index, Object value);
	void setCellValue(int row, int column, String id, Object value);
	
	ElementFacade getCellsFacade();
}
