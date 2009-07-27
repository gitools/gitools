package org.gitools.model.table.impl;

import org.gitools.model.table.Table;

public class AbstractTableColumn {

	protected int column;
	protected Table table;

	public AbstractTableColumn() {
		super();
	}

	public AbstractTableColumn(int column, Table table) {
		this.column = column;
		this.table = table;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}
}
