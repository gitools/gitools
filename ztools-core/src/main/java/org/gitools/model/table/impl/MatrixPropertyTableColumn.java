package org.gitools.model.table.impl;

import org.gitools.model.table.Table;

public class MatrixPropertyTableColumn extends MatrixCellTableColumn {

	int property;

	public MatrixPropertyTableColumn() {
		super();
	}

	public MatrixPropertyTableColumn(int column, Table table, int property) {
		super(column, table);
		this.property = property;
	}

	@Override
	public Object getValue(int row) {
		return table.getMatrix().getCellValue(row, column, property);
	}

}
