package org.gitools.model.table.impl;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gitools.model.matrix.Matrix;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.table.ITableColumn;
import org.gitools.model.table.Table;

@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixCellTableColumn 
	implements ITableColumn, Serializable {
	
	private static final long serialVersionUID = -5968245911627777748L;
	
	protected int column;
	protected Matrix matrix;
	
	public MatrixCellTableColumn(){
		
	}
	
	public MatrixCellTableColumn(Table table, int column){
		this.matrix = (Matrix) table.getMatrix();
		this.column	= column;
	}
	
	@Override
	public IElementAdapter getAdapter() {
		return matrix.getCellAdapter();
	}

	@Override
	public String getHeader() {
		return matrix.getColumn(column).toString();
	}

	@Override
	public int getRowCount() {
		return matrix.getRowCount();
	}

	@Override
	public Object getValue(int row) {
		return matrix.getCell(row,column);
	
	}
}
