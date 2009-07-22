package org.gitools.model.table.impl;

import java.util.ArrayList;
import java.util.List;

import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.Matrix;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.table.ITableColumn;
import org.gitools.model.table.Table;

@SuppressWarnings("unused")
public class AnnotationTableColumn implements ITableColumn {

	private int column;
	private Table table;
	private AnnotationMatrix annotations;

	public AnnotationTableColumn() {

	}

	public AnnotationTableColumn(Table table, int colunm) {
		this.table = table;
		this.column = colunm;
		annotations = table.getAnnotations();
	}

	public AnnotationTableColumn(Table table, String id) {
		this(table, table.getAnnotations().getColumnIndex(id));
	}

	@Override
	public IElementAdapter getAdapter() {
		return annotations.getCellAdapter();
	}

	@Override
	public String getHeader() {
		return annotations.getColumnString(column);
	}

	@Override
	public int getRowCount() {
		return table.getRowCount();
	}

	@Override
	public Object getValue(int row) {
		Object rowName = table.getMatrix().getRow(row);
		if (rowName == null)return null;
		
		int index = annotations.getRowIndex(rowName.toString());
		if (index == -1) return null;
		return annotations.getCell(index, column);
	}

}
