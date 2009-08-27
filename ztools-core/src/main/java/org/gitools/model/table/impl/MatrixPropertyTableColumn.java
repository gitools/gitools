package org.gitools.model.table.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gitools.model.table.Table;

@XmlAccessorType(XmlAccessType.FIELD)
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
