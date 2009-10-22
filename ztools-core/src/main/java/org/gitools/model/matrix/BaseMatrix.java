package org.gitools.model.matrix;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.gitools.model.matrix.element.IElementAdapter;

import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;

@XmlSeeAlso( { 
	ObjectMatrix.class,
	StringMatrix.class,
	DoubleMatrix.class,
	AnnotationMatrix.class })
@XmlAccessorType(XmlAccessType.NONE)
public abstract class BaseMatrix extends Matrix {

	private static final long serialVersionUID = 4021765485781500318L;

	protected ObjectMatrix1D rows;
	protected ObjectMatrix1D columns;
	
	protected IElementAdapter rowAdapter;
	protected IElementAdapter columnAdapter;
	protected IElementAdapter cellAdapter;
	
	public BaseMatrix() {
	}
	
	public BaseMatrix(
			ObjectMatrix1D rows,
			ObjectMatrix1D columns,
			IElementAdapter rowAdapter,
			IElementAdapter columnAdapter,
			IElementAdapter cellAdapter) {
		
		this.rows = rows;
		this.columns = columns;
		
		this.rowAdapter = rowAdapter;
		this.columnAdapter = columnAdapter;
		this.cellAdapter = cellAdapter;
	}
	
	
	public ObjectMatrix1D getRows() {
		return rows;
	}
	
	public String[] getRowStrings() {
		String[] a = new String[rows.size()];
		rows.toArray(a);
		return a;
	}
	
	public void setRows(ObjectMatrix1D rows) {
		this.rows = rows;
	}

	public void setRows(String[] names) {
		this.rows = ObjectFactory1D.dense.make(names);
	}
	
	public Object getRow(int index) {
		return rows.get(index);
	}
	
	public String getRowString(int index) {
		return (String) rows.get(index);
	}
	
	public void setRow(int index, Object row) {
		rows.set(index, row);
	}
	
	
	public ObjectMatrix1D getColumns() {
		return columns;
	}
	
	public String[] getColumnStrings() {
		String[] a = new String[columns.size()];
		columns.toArray(a);
		return a;
	}
	
	public void setColumns(ObjectMatrix1D columns) {
		this.columns = columns;
	}
	
	public void setColumns(String[] names) {
		this.columns = ObjectFactory1D.dense.make(names);
	}
	
	public Object getColumn(int index) {
		return columns.get(index);
	}
	
	public String getColumnString(int index) {
		return (String) columns.get(index);
	}
	
	
	public void setColumn(int index, Object column) {
		columns.set(index, column);
	}
	
	public Object getCellValue(int row, int column, int property) {
		return cellAdapter.getValue(
				getCell(row, column), property);
	}
	
	public Object getCellValue(int row, int column, String id) {
		return cellAdapter.getValue(
				getCell(row, column), id);
	}
	
	public void setCellValue(int row, int column, int property, Object value) {
		cellAdapter.setValue(
				getCell(row, column), property, value);
	}
	
	public void setCellValue(int row, int column, String id, Object value) {
		cellAdapter.setValue(
				getCell(row, column), id, value);
	}
	
	//@XmlAnyElement
	//@XmlElement
	public IElementAdapter getRowAdapter() {
		return rowAdapter;
	}
	
	public void setRowAdapter(IElementAdapter rowAdapter) {
		this.rowAdapter = rowAdapter;
	}
	
	//@XmlAnyElement
	//@XmlElement
	public IElementAdapter getColumnAdapter() {
		return columnAdapter;
	}
	
	public void setColumnAdapter(IElementAdapter columnAdapter) {
		this.columnAdapter = columnAdapter;
	}
	
	//@XmlAnyElement
	//@XmlElement
	public IElementAdapter getCellAdapter() {
		return cellAdapter;
	}
	
	public void setCellAdapter(IElementAdapter cellAdapter) {
		this.cellAdapter = cellAdapter;
	}
}
