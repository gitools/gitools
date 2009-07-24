package org.gitools.model.table.impl;

import org.gitools.model.table.Table;

public class MatrixPropertyTableColumn 
	extends MatrixCellTableColumn{

	int property;
	
	public MatrixPropertyTableColumn() {
		super();
	}

	public MatrixPropertyTableColumn(Table table, int column, int property) {
		super(table, column);
		this.property = property;
	}

	@Override
	public Object getValue(int row) {
		return matrix.getCellValue(row, column, property);
	}

}
