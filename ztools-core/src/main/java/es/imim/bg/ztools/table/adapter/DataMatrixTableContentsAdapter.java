package es.imim.bg.ztools.table.adapter;

import es.imim.bg.ztools.model.DataMatrix;
import es.imim.bg.ztools.resources.IResource;
import es.imim.bg.ztools.table.ITableContents;
import es.imim.bg.ztools.table.element.IElementAdapter;
import es.imim.bg.ztools.table.element.basic.DoubleElementAdapter;
import es.imim.bg.ztools.table.element.basic.StringElementAdapter;

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
