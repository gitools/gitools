package org.gitools.model.table.adapter;

import org.gitools.model.DataMatrix;
import org.gitools.model.table.ITableContents;
import org.gitools.model.table.element.IElementAdapter;
import org.gitools.model.table.element.basic.DoubleElementAdapter;
import org.gitools.model.table.element.basic.StringElementAdapter;
import org.gitools.resources.IResource;

public class DataMatrixTableContentsAdapter extends TableContentsAdapter<DataMatrix>
		implements ITableContents {

	public DataMatrixTableContentsAdapter(DataMatrix data, IResource<DataMatrix> resource) {
		super(data, resource);
	}

	public DataMatrixTableContentsAdapter(Object data) {
		super((DataMatrix) data);
	}

	public Object getCell(int row, int column) {
		return getMatrix().getData().get(row, column);
	}

	public Object getCellValue(int row, int column, int index) {
		return getMatrix().getData().get(row, column);
	}

	public Object getCellValue(int row, int column, String id) {
		return getMatrix().getData().get(row, column);
	}

	public Object getColumn(int index) {
		return getMatrix().getColNames()[index];
	}

	public int getColumnCount() {
		return getMatrix().getColNames().length;
	}

	public Object getRow(int index) {
		return getMatrix().getRowNames()[index];
	}

	public int getRowCount() {
		return getMatrix().getRowNames().length;
	}

	public void setCellValue(int row, int column, int index, Object value) {
		getMatrix().getData().set(row, column, (Double) value);
	}

	public void setCellValue(int row, int column, String id, Object value) {
		getMatrix().getData().set(row, column, (Double) value);
	}

	public IElementAdapter getCellAdapter() {
		// FIXME: the adapter must come with the resource
		return new DoubleElementAdapter();
	}

	public IElementAdapter getColumnAdapter() {
		return new StringElementAdapter();
	}

	public IElementAdapter getRowAdapter() {
		return new StringElementAdapter();
	}
}
