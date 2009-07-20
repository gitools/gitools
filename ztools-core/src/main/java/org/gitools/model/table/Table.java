package org.gitools.model.table;

import java.util.ArrayList;
import java.util.List;

import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.Matrix;
import org.gitools.model.matrix.TableFormatException;
import org.gitools.model.matrix.element.IElementAdapter;

public class Table implements ITable {

	int rowCount = 0;
	int rowIndices[];

	private Matrix matrix;
	private AnnotationMatrix annotations;

	private List<ITableColumn> columns;

	public Table() {

	}

	public Table(Matrix matrix, AnnotationMatrix annotations) {
		this.matrix = matrix;
		this.annotations = annotations;

		int count = matrix.getRowCount();

		// rowIndices
		List<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < count; i++) {

			int j = annotations.getRowIndex(matrix.getRow(i).toString());
			if (j >= 0)
				indices.add(i);
		}

		rowIndices = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++)
			rowIndices[i] = indices.get(i);

	}

	public void addColumn(ITableColumn column){
		rowCount = column.getRowCount();
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
	public Object getValue(int row, int column, int index) {
		return columns.get(column).getValue(row, index);
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

}
