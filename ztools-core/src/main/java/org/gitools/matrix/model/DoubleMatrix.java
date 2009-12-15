package org.gitools.matrix.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gitools.matrix.model.element.DoubleElementAdapter;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.ObjectFactory1D;

//TODO remove JAXB support
@XmlAccessorType(XmlAccessType.NONE)

public final class DoubleMatrix extends BaseMatrix	{			

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
	
	@Deprecated // Use getTitle() instead.
	public final String getName() {
		return getTitle();
	}

	@Deprecated // Use setTitle() instead.
	public final void setName(String name) {
		setTitle(name);
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
	
	//TODO rename to getCells
	public final DoubleMatrix2D getData() {
		return cells;
	}

	//TODO rename to setCells
	public final void setData(DoubleMatrix2D data) {
		this.cells = data;
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
		cells.set(row, column, (Double)value);
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
