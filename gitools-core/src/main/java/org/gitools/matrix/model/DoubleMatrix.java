package org.gitools.matrix.model;

import org.gitools.matrix.model.element.DoubleElementAdapter;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.ObjectFactory1D;

public class DoubleMatrix extends BaseMatrix	{			

	private static final long serialVersionUID = -710485141066995079L;

	protected DoubleMatrix2D cells;
	
	public DoubleMatrix() {
		this("", new String[0], new String[0], DoubleFactory2D.dense.make(0, 0));
	}
	
	public DoubleMatrix(
			String title, 
			String[] colNames, 
			String[] rowNames, 
			DoubleMatrix2D cells) {

		super(
				title,
				ObjectFactory1D.dense.make(rowNames),
				ObjectFactory1D.dense.make(colNames),
				new DoubleElementAdapter());
		
		this.cells = cells;
	}

	// rows and columns
	
	@Override
	public int getColumnCount() {
		return columns.cardinality();
	}

	@Override
	public int getRowCount() {
		return rows.cardinality();
	}
	
	// cells
	
	public final DoubleMatrix2D getCells() {
		return cells;
	}

	public final void setCells(DoubleMatrix2D cells) {
		this.cells = cells;
	}
	
	@Override
	public Object getCell(int row, int column) {
		return cells.get(row, column);
	}
	
	@Override
	public Object getCellValue(int row, int column, int index) {
		return cells.get(row, column);
	}

	@Override
	public void setCellValue(int row, int column, int index, Object value) {
		// FIXME null and NaN are different things
		cells.set(row, column, value != null ? (Double) value : Double.NaN);
	}

	@Override
	public void makeCells(int rows, int columns) {
		this.cells = DoubleFactory2D.dense.make(rows, columns);
	}
	
	/*@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(name).append('\n');
		sb.append(colNames).append('\n');
		sb.append(rowNames).append('\n');
		sb.append(data).append('\n');
		
		return sb.toString();
	}*/
}
