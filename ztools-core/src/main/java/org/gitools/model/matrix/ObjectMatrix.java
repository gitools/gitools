package org.gitools.model.matrix;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.matrix.element.array.ArrayElementAdapter;
import org.gitools.model.matrix.element.basic.StringElementAdapter;
import org.gitools.model.matrix.element.bean.BeanElementAdapter;

import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;

/*@XmlType(
		propOrder = {
				"rowAdapter",
				"columnAdapter",
				"cellAdapter"})*/

@XmlSeeAlso(value = {
		BeanElementAdapter.class, 
		StringElementAdapter.class,
		ArrayElementAdapter.class})
		
public class ObjectMatrix extends AbstractMatrix {

	private static final long serialVersionUID = 4077172838934816719L;

	protected ObjectMatrix2D cells;
	
	public ObjectMatrix() {
		super();
	}
	
	public ObjectMatrix(
			ObjectMatrix1D rows,
			ObjectMatrix1D columns,
			ObjectMatrix2D cells,
			IElementAdapter rowAdapter,
			IElementAdapter columnAdapter,
			IElementAdapter cellAdapter) {
	    
		super(rows, columns, rowAdapter, columnAdapter, cellAdapter);
	    
		this.cells = cells;
	}

	public void makeData() {
		cells = ObjectFactory2D.dense.make(
				rows.size(),
				columns.size());
	}
	
	@XmlTransient
	public int getRowCount() {
		return cells.rows();
	}
	
	@XmlTransient
	public int getColumnCount() {
		return cells.columns();
	}
	
	@XmlTransient
	public ObjectMatrix2D getCells() {
		return cells;
	}

	public void setCells(ObjectMatrix2D cells) {
		this.cells = cells;
	}
	
	public Object getCell(int row, int column) {
		return cells.get(row, column);
	}
	
	public void setCell(int row, int column, Object cell) {
		cells.set(row, column, cell);
	}
}
