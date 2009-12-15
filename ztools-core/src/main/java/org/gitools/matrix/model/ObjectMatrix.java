package org.gitools.matrix.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gitools.matrix.model.element.IElementAdapter;

import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;

//TODO remove JAXB support
@XmlAccessorType(XmlAccessType.NONE)

public class ObjectMatrix extends BaseMatrix {

	private static final long serialVersionUID = 4077172838934816719L;
	
	protected ObjectMatrix2D cells;
	
	public ObjectMatrix() {
		super();
	}
	
	public ObjectMatrix(
			String title,
			ObjectMatrix1D rows,
			ObjectMatrix1D columns,
			ObjectMatrix2D cells,
			IElementAdapter cellAdapter) {
	    
		super(title, rows, columns, cellAdapter);
		
		this.cells = cells;
	}

	// rows and columns
	
	@Override
	public int getRowCount() {
		return cells.rows();
	}
	
	@Override
	public int getColumnCount() {
		return cells.columns();
	}
	
	// cells
	
	//TODO rename to makeCells()
	public void makeData() {
		cells = ObjectFactory2D.dense.make(
				rows.size(),
				columns.size());
	}
	
	public ObjectMatrix2D getCells() {
		return cells;
	}
	
	public void setCells(ObjectMatrix2D cells) {
		this.cells = cells;
	}
	
	@Override
	public Object getCell(int row, int column) {
		return cells.get(row, column);
	}
	
	public void setCell(int row, int column, Object cell) {
		cells.set(row, column, cell);
	}
	
	@Override
	public Object getCellValue(int row, int column, int property) {
		return cellAdapter.getValue(getCell(row, column), property);
	}

	@Override
	public void setCellValue(int row, int column, int property, Object value) {
		cellAdapter.setValue(getCell(row, column), property, value);
	}
}
