package org.gitools.table.model.impl;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.table.model.ITableColumn;
import org.gitools.table.model.Table;


@XmlAccessorType(XmlAccessType.FIELD)
public class AnnotationTableColumn
		extends AbstractTableColumn
		implements ITableColumn, Serializable {

	private static final long serialVersionUID = 212215870893703067L;

	public AnnotationTableColumn() {
	}

	public AnnotationTableColumn(Table table, int column) {
		super(column, table);
	}

	public AnnotationTableColumn(Table table, String id) {
		this(table, table.getAnnotations().getColumnIndex(id));
	}

	@Override
	public IElementAdapter getAdapter() {
		return table.getAnnotations().getCellAdapter();
	}

	@Override
	public String getHeader() {
		if (column <0)
			return "";
		return table.getAnnotations().getColumnString(column);
	}

	@Override
	public int getRowCount() {
		return table.getAnnotations().getRowCount();
	}

	@Override
	public Object getValue(int row) {
		if (column < 0) 
			return table.getMatrix().getRowLabel(row);
		return table.getAnnotations().getCell(row, column);
	}

}
