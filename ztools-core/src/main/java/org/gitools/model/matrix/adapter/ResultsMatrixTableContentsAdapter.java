package org.gitools.model.matrix.adapter;

import org.gitools.model.matrix.IMatrix;
import org.gitools.model.matrix.ObjectMatrix;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.resources.IResource;

@Deprecated // Now ResultsMatrix is named ObjectMatrix and implements IMatrix
public class ResultsMatrixTableContentsAdapter extends
		TableContentsAdapter<ObjectMatrix> implements IMatrix {

	public ResultsMatrixTableContentsAdapter(ObjectMatrix data,
			IResource<ObjectMatrix> resource) {
		super(data, resource);
	}

	public ResultsMatrixTableContentsAdapter(ObjectMatrix data) {
		super(data);
	}

	@Override
	public int getRowCount() {
		return getMatrix().getRowCount();
	}

	@Override
	public Object getRow(int index) {
		return getMatrix().getRow(index);
	}

	@Override
	public IElementAdapter getRowAdapter() {
		return getMatrix().getRowAdapter();
	}

	/* columns */

	@Override
	public int getColumnCount() {
		return getMatrix().getColumnCount();
	}

	@Override
	public Object getColumn(int index) {
		return getMatrix().getColumn(index);
	}

	@Override
	public IElementAdapter getColumnAdapter() {
		return getMatrix().getColumnAdapter();
	}

	/* cells */

	@Override
	public Object getCell(int row, int column) {
		return getMatrix().getCell(row, column);
	}

	@Override
	public Object getCellValue(int row, int column, int index) {
		return getMatrix().getCellValue(row, column, index);
	}

	@Override
	public Object getCellValue(int row, int column, String id) {
		return getMatrix().getCellValue(row, column, id);
	}

	@Override
	public void setCellValue(int row, int column, int index, Object value) {
		getMatrix().setCellValue(row, column, index, value);
	}

	@Override
	public void setCellValue(int row, int column, String id, Object value) {
		getMatrix().setCellValue(row, column, id, value);
	}

	@Override
	public IElementAdapter getCellAdapter() {
		return getMatrix().getCellAdapter();
	}

}
