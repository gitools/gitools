package es.imim.bg.ztools.model;

import es.imim.bg.ztools.table.element.Property;

public class TableFigure extends Figure {

	protected TableArtifact artifactTable;

	/** Property to show **/
	protected Property propToShow;

	/** Positions of the rows to show **/
	protected int[] rowsToShow;

	/** Positions of the columns to show **/
	protected int[] columnsToShow;

	public TableFigure() {
		super();
	}

	public TableArtifact getArtifactTable() {
		return artifactTable;
	}

	public void setArtifactTable(TableArtifact artifactTable) {
		this.artifactTable = artifactTable;
	}

	public Property getPropToShow() {
		return propToShow;
	}

	public void setPropToShow(Property propToShow) {
		this.propToShow = propToShow;
	}

	public int[] getRowsToShow() {
		return rowsToShow;
	}

	public void setRowsToShow(int[] rowsToShow) {
		this.rowsToShow = rowsToShow;
	}

	public int[] getColumnsToShow() {
		return columnsToShow;
	}

	public void setColumnsToShow(int[] columnsToShow) {
		this.columnsToShow = columnsToShow;
	}

}
