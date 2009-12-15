package org.gitools.matrix.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.gitools.matrix.model.element.StringElementAdapter;

import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;

//TODO remove JAXB support
@XmlAccessorType(XmlAccessType.NONE)

public class StringMatrix extends ObjectMatrix {

	private static final long serialVersionUID = 5061265701379494159L;

	public StringMatrix() {
		super();
		
		setCellAdapter(new StringElementAdapter());
	}
	
	public StringMatrix(
			String title,
			ObjectMatrix1D rows,
			ObjectMatrix1D columns,
			ObjectMatrix2D cells) {
	    
		super(title, rows, columns, cells, new StringElementAdapter());
	}
	
	@Override
	public void makeData() {
		cells = ObjectFactory2D.dense.make(
				rows.size(),
				columns.size());
	}
	
	@XmlTransient
	@Override
	public int getRowCount() {
		return cells.rows();
	}
	
	@XmlTransient
	@Override
	public int getColumnCount() {
		return cells.columns();
	}
	
	@Override
	public String getCell(int row, int column) {
		return (String) cells.get(row, column);
	}
	
	public void setCell(int row, int column, String cell) {
		cells.set(row, column, cell);
	}
}
