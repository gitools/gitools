package org.gitools.model.matrix;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.matrix.element.IElementProperty;

import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;

//TODO remove JAXB support
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
	
	protected IElementAdapter cellAdapter;
	
	public BaseMatrix() {
		this(
				"",
				ObjectFactory1D.dense.make(0),
				ObjectFactory1D.dense.make(0),
				null);
	}
	
	public BaseMatrix(
			String title,
			ObjectMatrix1D rows,
			ObjectMatrix1D columns,
			IElementAdapter cellAdapter) {
		
		this.title = title;
		
		this.rows = rows;
		this.columns = columns;
		
		this.cellAdapter = cellAdapter;
	}
	
	// rows
	
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
	
	@Deprecated // Use getRowLabel() instead
	public String getRowString(int index) {
		return (String) rows.get(index);
	}
	
	public String getRowLabel(int index) {
		return (String) rows.get(index);
	}
	
	public void setRow(int index, Object row) {
		rows.set(index, row);
	}
	
	// columns
	
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
	
	@Deprecated // Use getColumnLabel() instead
	public String getColumnString(int index) {
		return (String) columns.get(index);
	}
	
	public String getColumnLabel(int index) {
		return (String) columns.get(index);
	}
	
	public void setColumn(int index, Object column) {
		columns.set(index, column);
	}
	
	// cells
	
	@Override
	public Object getCellValue(int row, int column, String id) {
		return getCellValue(row, column, getCellAttributeIndex(id));
	}

	@Override
	public void setCellValue(int row, int column, String id, Object value) {
		setCellValue(row, column, getCellAttributeIndex(id), value);
	}
	
	// adapters
	
	@Override
	public IElementAdapter getCellAdapter() {
		return cellAdapter;
	}
	
	public void setCellAdapter(IElementAdapter cellAdapter) {
		this.cellAdapter = cellAdapter;
	}
	
	// attributes
	
	@Override
	public List<IElementProperty> getCellAttributes() {
		return cellAdapter.getProperties();
	}
	
	private int getCellAttributeIndex(String id) {
		Integer index = cellAdapter.getPropertyIndex(id);
		if (index == null)
			throw new RuntimeException("There isn't any property with id: " + id);
		
		return index.intValue();
	}
}
