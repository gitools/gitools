package org.gitools.model.table;

import org.gitools.model.matrix.element.IElementAdapter;

public interface ITable {

	// columns
	//public void addColumn(ITableColumn column) throws TableFormatException;
	//public void removeColumn(int index);

	public ITableColumn getColumn(int index);

	// values
	public String getHeader(int column);
	public Object getValue(int row, int column);

	// cell column adapter
	public IElementAdapter getCellColumnAdapter(int column);

	// consultors
	public int getColumnCount();
	public int getRowCount();

}
