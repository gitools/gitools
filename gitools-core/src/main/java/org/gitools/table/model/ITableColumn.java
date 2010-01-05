package org.gitools.table.model;

import org.gitools.matrix.model.element.IElementAdapter;

public interface ITableColumn {
	
	//header and column values
	public String getHeader();
	public Object getValue(int row);
	
	//adapter
	public IElementAdapter getAdapter();
	
	public int getRowCount();
}
