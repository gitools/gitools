package org.gitools.model.table.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gitools.model.matrix.Matrix;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.table.ITableColumn;
import org.gitools.model.table.Table;

@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixTableColumn implements ITableColumn {
	
	private int column;
	private Matrix matrix;
	
	public MatrixTableColumn(){
		
	}
	
	public MatrixTableColumn(Table table, int column){
		this.matrix = table.getMatrix();
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
	public Object getValue(int row, int index) {
		if (index==0 )
			return matrix.getCell(row,column);
		return matrix.getCellValue(row, column, index);
	}

	
}
