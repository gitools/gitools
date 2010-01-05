package org.gitools.table.model.impl;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.table.model.ITableColumn;
import org.gitools.table.model.Table;

@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixCellTableColumn
		extends AbstractTableColumn
		implements ITableColumn, Serializable {

	private static final long serialVersionUID = -5968245911627777748L;

	public MatrixCellTableColumn() {
	}

	public MatrixCellTableColumn(int column, Table table) {
		super(column, table);

	}

	@Override
	public IElementAdapter getAdapter() {
		return table.getMatrix().getCellAdapter();
	}

	@Override
	public String getHeader() {
	    Object object = table.getMatrix().getColumnLabel(column);
		return object == null ? "null" : object.toString();
	}

	@Override
	public int getRowCount() {
		return table.getMatrix().getRowCount();
	}

	@Override
	public Object getValue(int row) {
		return table.getMatrix().getCell(row, column);

	}
}
