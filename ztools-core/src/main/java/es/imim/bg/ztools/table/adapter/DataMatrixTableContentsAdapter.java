package es.imim.bg.ztools.table.adapter;

import es.imim.bg.ztools.model.DataMatrix;
import es.imim.bg.ztools.table.ITableContents;
import es.imim.bg.ztools.table.element.IElementAdapter;
import es.imim.bg.ztools.table.element.basic.DoubleElementAdapter;
import es.imim.bg.ztools.table.element.basic.StringElementAdapter;

public class DataMatrixTableContentsAdapter implements ITableContents {

	private DataMatrix data;

	public DataMatrixTableContentsAdapter(DataMatrix data) {
		this.data = data;
	}

	public Object getCell(int row, int column) {
		return data.getData().get(row, column);
	}

	public Object getCellValue(int row, int column, int index) {
		return data.getData().get(row, column);
	}

	public Object getCellValue(int row, int column, String id) {
		return data.getData().get(row, column);
	}

	public Object getColumn(int index) {
		return data.getColNames()[index];
	}

	public int getColumnCount() {
		return data.getColNames().length;
	}

	public Object getRow(int index) {
		return data.getRowNames()[index];
	}

	public int getRowCount() {
		return data.getRowNames().length;
	}

	public void setCellValue(int row, int column, int index, Object value) {
		data.getData().set(row, column, (Double) value);
	}

	public void setCellValue(int row, int column, String id, Object value) {
		data.getData().set(row, column, (Double) value);
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
