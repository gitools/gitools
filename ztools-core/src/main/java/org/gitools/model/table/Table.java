package org.gitools.model.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.Matrix;
import org.gitools.model.matrix.element.IElementAdapter;

public class Table implements ITable, Serializable {

	private int  rowCount = 0;
	private Matrix matrix;
	private AnnotationMatrix annotations;

	private List<ITableColumn> columns;

	public Table() {
		columns = new ArrayList<ITableColumn>();
	}

	public Table(Matrix matrix, AnnotationMatrix annotations) {
		rowCount = matrix.getRowCount();
		columns = new ArrayList<ITableColumn>();
		this.matrix = matrix;
		this.annotations = annotations;
	}

	public void addColumn(ITableColumn column){
		columns.add(column);
	}

	@Override
	public IElementAdapter getCellColumnAdapter(int column) {
		return columns.get(column).getAdapter();
	}

	@Override
	public ITableColumn getColumn(int index) {
		return columns.get(index);
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public String getHeader(int column) {
		return columns.get(column).getHeader();
	}

	@Override
	public Object getValue(int row, int column) {
		return columns.get(column).getValue(row);
	}

	@Override
	public int getRowCount() {
		return rowCount;
	}

	public void removeColumn(int index) {
		columns.remove(index);

	}

	public void setAnnotations(AnnotationMatrix annotations) {
		this.annotations = annotations;
	}

	public AnnotationMatrix getAnnotations() {
		return annotations;
	}

	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

}
