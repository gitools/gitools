package org.gitools.model.table.impl;

import java.io.Serializable;

import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.table.ITableColumn;
import org.gitools.model.table.Table;

@SuppressWarnings("unused")
public class AnnotationTableColumn extends AbstractTableColumn
	implements ITableColumn, Serializable {

	

	public AnnotationTableColumn() {

	}

	public AnnotationTableColumn( int column, Table table) {
		super(column, table);
		;
	}

	public AnnotationTableColumn(Table table, String id) {
		this(table.getAnnotations().getColumnIndex(id), table );
	}

	@Override
	public IElementAdapter getAdapter() {
		return table.getAnnotations().getCellAdapter();
	}

	@Override
	public String getHeader() {
		return table.getAnnotations().getColumnString(column);
	}

	@Override
	public int getRowCount() {
		return table.getAnnotations().getRowCount();
	}

	@Override
	public Object getValue(int row) {
		AnnotationMatrix annotations = table.getAnnotations();
		Object rowName = table.getMatrix().getRow(row);
		if (rowName == null)return null;
		
		int index = annotations.getRowIndex(rowName.toString());
		if (index == -1) return null;
		return annotations.getCell(index, column);
	}

}
