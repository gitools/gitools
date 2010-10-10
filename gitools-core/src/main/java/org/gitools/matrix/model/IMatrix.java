package org.gitools.matrix.model;

import java.util.List;

import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;

public interface IMatrix {

	// rows
	
	int getRowCount();
	String getRowLabel(int index);
	
	// columns
	
	int getColumnCount();
	String getColumnLabel(int index);
	
	// cells

	@Deprecated
	Object getCell(int row, int column);

	Object getCellValue(int row, int column, int index);
	Object getCellValue(int row, int column, String id);
	void setCellValue(int row, int column, int index, Object value);
	void setCellValue(int row, int column, String id, Object value);

	@Deprecated
	IElementAdapter getCellAdapter();
	
	List<IElementAttribute> getCellAttributes();
}
