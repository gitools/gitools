package org.gitools.model.table;

import org.gitools.model.matrix.element.IElementAdapter;

public interface ITableColumn {
	
	//header and column values
	public String getHeader();
	public Object getValue(int row);
	
	//adapter
	public IElementAdapter getAdapter();
	
	public int getRowCount();
}
