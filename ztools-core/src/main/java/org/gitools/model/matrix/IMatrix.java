package org.gitools.model.matrix;

import java.util.List;

import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.matrix.element.IElementProperty;

public interface IMatrix {

	// rows
	
	int getRowCount();
	String getRowLabel(int index);
	
	//IElementAdapter getRowAdapter();
	
	// columns
	
	int getColumnCount();
	String getColumnLabel(int index);
	//IElementAdapter getColumnAdapter();
	
	// cells
	
	Object getCell(int row, int column);
	Object getCellValue(int row, int column, int index);
	Object getCellValue(int row, int column, String id);
	void setCellValue(int row, int column, int index, Object value);
	void setCellValue(int row, int column, String id, Object value);
	
	IElementAdapter getCellAdapter();
	
	List<IElementProperty> getCellAttributes();
}
