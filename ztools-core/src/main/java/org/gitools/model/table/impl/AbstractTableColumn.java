package org.gitools.model.table.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.gitools.model.table.Table;

@XmlSeeAlso({
	AnnotationTableColumn.class, 
	MatrixCellTableColumn.class, 
	MatrixPropertyTableColumn.class})

	@XmlAccessorType(XmlAccessType.FIELD)
public class AbstractTableColumn {
	// internal column, 
	// reference of a Table or Matrix column
	protected int column;
	
	@XmlTransient
	protected Table table;

	public AbstractTableColumn() {
		super();
	}

	public AbstractTableColumn(int column, Table table) {
		this.column = column;
		this.table = table;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}
}
