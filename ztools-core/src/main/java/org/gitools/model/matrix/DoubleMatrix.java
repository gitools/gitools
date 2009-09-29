package org.gitools.model.matrix;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gitools.model.matrix.element.basic.DoubleElementAdapter;
import org.gitools.model.matrix.element.basic.StringElementAdapter;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.ObjectFactory1D;

/*@XmlType(
		propOrder = {
				"name"///, 
				"colNames", 
				"rowNames", 
				"data"///})*/

@XmlAccessorType(XmlAccessType.NONE)
public final class DoubleMatrix extends BaseMatrix	{			

	private static final long serialVersionUID = -710485141066995079L;

	protected String name;
	
	/*protected String[] colNames;
	protected String[] rowNames;*/
	
	protected DoubleMatrix2D data;

	/*private IElementAdapter cellAdapter;
	private IElementAdapter columnAdapter;
	private IElementAdapter rowAdapter;*/
	
	public DoubleMatrix(
			String name, 
			String[] colNames, 
			String[] rowNames, 
			DoubleMatrix2D data) {
		
		this.name = name;
		
		this.columns = ObjectFactory1D.dense.make(colNames);
		this.rows = ObjectFactory1D.dense.make(rowNames);
		
		this.data = data;
		
		this.cellAdapter = new DoubleElementAdapter();
		this.rowAdapter = new StringElementAdapter();
		this.columnAdapter = new StringElementAdapter();
	}

	public DoubleMatrix() {
		this("", new String[0], new String[0], DoubleFactory2D.dense.make(0, 0));
	}
	
	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	/*public final String[] getColNames() {
		return colNames;
	}

	public final void setColNames(String[] colNames) {
		this.colNames = colNames;
	}

	public final String[] getRowNames() {
		return rowNames;
	}

	public final void setRowNames(String[] rowNames) {
		this.rowNames = rowNames;
	}
*/
	public final DoubleMatrix2D getData() {
		return data;
	}

	public final void setData(DoubleMatrix2D data) {
		this.data = data;
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

	@Override
	public int getColumnCount() {
		//return data.columns();
		return columns.cardinality();
	}

	@Override
	public int getRowCount() {
		//return data.rows();
	return rows.cardinality();
	}
	
	@Override
	public Object getCell(int row, int column) {
		return data.get(row, column);
	}

	@Override
	public Object getCellValue(int row, int column, int index) {
		return data.get(row, column);
	}

	@Override
	public Object getCellValue(int row, int column, String id) {
		return data.get(row, column);
	}

	@Override
	public void setCellValue(int row, int column, int index, Object value) {
		data.set(row, column, (Double)value);
	}

	@Override
	public void setCellValue(int row, int column, String id, Object value) {
		data.set(row, column, (Double)value);
	}
	
	/*@Override
	public Object getColumn(int index) {
		return colNames[index];
	}

	@Override
	public Object getRow(int index) {
		return rowNames[index];
	}

	@Override
	public IElementAdapter getCellAdapter() {
		return cellAdapter;
	}

	@Override
	public IElementAdapter getColumnAdapter() {
		return columnAdapter;
	}

	@Override
	public IElementAdapter getRowAdapter() {
		return rowAdapter;
	}*/
}
