package es.imim.bg.ztools.model;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;
import es.imim.bg.ztools.model.elements.ElementFacade;

@XmlType(
		propOrder = {
				"rowsFacade",
				"columnsFacade",
				"cellsFacade"})
				
public class ResultsMatrix {
	
	protected ObjectMatrix1D rows;
	protected ObjectMatrix1D columns;
	protected ObjectMatrix2D cells;

	protected ElementFacade rowsFacade;
	protected ElementFacade columnsFacade;
	protected ElementFacade cellsFacade;
	
	public ResultsMatrix() {
	}
	
	public ResultsMatrix(
			ObjectMatrix1D rows,
			ObjectMatrix1D columns,
			ObjectMatrix2D cells,
			ElementFacade rowsFacade,
			ElementFacade columnsFacade,
			ElementFacade cellsFacade) {
		
		this.rows = rows;
		this.columns = columns;
		this.cells = cells;
		
		this.rowsFacade = rowsFacade;
		this.columnsFacade = columnsFacade;
		this.cellsFacade = cellsFacade;
	}

	public void makeData() {
		cells = ObjectFactory2D.dense.make(
				rows.size(),
				columns.size());
	}
	
	@XmlTransient
	public ObjectMatrix1D getRows() {
		return rows;
	}
	
	public void setRows(ObjectMatrix1D rows) {
		this.rows = rows;
	}
	
	@XmlTransient
	public ObjectMatrix1D getColumns() {
		return columns;
	}
	
	public void setColumns(ObjectMatrix1D columns) {
		this.columns = columns;
	}
	
	@XmlTransient
	public final ObjectMatrix2D getCells() {
		return cells;
	}

	public void setCells(ObjectMatrix2D cells) {
		this.cells = cells;
	}
	
	@XmlTransient
	public final int getRowCount() {
		return cells.rows();
	}

	public Object getRow(int index) {
		return rows.get(index);
	}
	
	public void setRow(int index, Object row) {
		rows.set(index, row);
	}
	
	@XmlTransient
	public final int getColumnCount() {
		return cells.columns();
	}
	
	public Object getColumn(int index) {
		return columns.get(index);
	}
	
	public void setColumn(int index, Object column) {
		columns.set(index, column);
	}
	
	public final Object getCell(int row, int column) {
		return cells.get(row, column);
	}
	
	public void setCell(int row, int column, Object cell) {
		cells.set(row, column, cell);
	}
	
	public Object getCellValue(int row, int column, int property) {
		return cellsFacade.getValue(
				cells.get(row, column), property);
	}
	
	public Object getCellValue(int row, int column, String id) {
		return cellsFacade.getValue(
				cells.get(row, column), id);
	}
	
	public void setCellValue(int row, int column, int property, Object value) {
		cellsFacade.setValue(
				cells.get(row, column), property, value);
	}
	
	public void setCellValue(int row, int column, String id, Object value) {
		cellsFacade.setValue(
				cells.get(row, column), id, value);
	}
	
	//@XmlAnyElement
	public ElementFacade getRowsFacade() {
		return rowsFacade;
	}
	
	public void setRowsFacade(ElementFacade rowsFacade) {
		this.rowsFacade = rowsFacade;
	}
	
	//@XmlAnyElement
	public ElementFacade getColumnsFacade() {
		return columnsFacade;
	}
	
	public void setColumnsFacade(ElementFacade columnsFacade) {
		this.columnsFacade = columnsFacade;
	}
	
	//@XmlAnyElement
	public ElementFacade getCellsFacade() {
		return cellsFacade;
	}
	
	public void setCellsFacade(ElementFacade cellsFacade) {
		this.cellsFacade = cellsFacade;
	}
}
