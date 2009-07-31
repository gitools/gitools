package org.gitools.model.matrix;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.matrix.element.basic.DoubleElementAdapter;
import org.gitools.model.matrix.element.basic.StringElementAdapter;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;

/*@XmlType(
		propOrder = {
				"name"///, 
				"colNames", 
				"rowNames", 
				"data"///})*/
				
@XmlAccessorType(XmlAccessType.NONE)
public final class DoubleMatrix 
	extends Matrix	{			

	protected String name;
	
	protected String[] colNames;
	protected String[] rowNames;
	
	protected DoubleMatrix2D data;

	private IElementAdapter cellAdapter;
	private IElementAdapter columnAdapter;
	private IElementAdapter rowAdapter;
	
	
	public DoubleMatrix(
			String name, 
			String[] colNames, 
			String[] rowNames, 
			DoubleMatrix2D data) {
		
		this.name = name;
		this.colNames = colNames;
		this.rowNames = rowNames;
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

	
	public final String[] getColNames() {
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


	public final DoubleMatrix2D getData() {
		return data;
	}

	public final void setData(DoubleMatrix2D data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(name).append('\n');
		sb.append(colNames).append('\n');
		sb.append(rowNames).append('\n');
		sb.append(data).append('\n');
		
		return sb.toString();
	}

	@Override
	public int getColumnCount() {
		return data.columns();
	}

	@Override
	public int getRowCount() {
		return data.rows();
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
	
	@Override
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
	}
}
