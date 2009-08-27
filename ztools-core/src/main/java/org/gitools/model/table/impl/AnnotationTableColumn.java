package org.gitools.model.table.impl;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.table.ITableColumn;
import org.gitools.model.table.Table;

@SuppressWarnings("unused")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnnotationTableColumn extends AbstractTableColumn implements
		ITableColumn, Serializable {

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
			return table.getMatrix().getRow(row);
		return table.getAnnotations().getCell(row, column);
	}

}
