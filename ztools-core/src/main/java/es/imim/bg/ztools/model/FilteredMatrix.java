package es.imim.bg.ztools.model;

import es.imim.bg.ztools.table.element.Property;

public class FilteredMatrix extends TableFigure {
	/**
	 * Represents a matrix filtered, original data or final resuts of an analysis
	 */
	protected TableArtifact artifactTable;

	/** Property to show **/
	//FIXME: cell decoration and detailed panel representation.
	protected Property propToShow;

	/** Positions of the columns to show **/
	protected int[] columnsToShow;

	public FilteredMatrix() {
		super();
	}

	public Property getPropToShow() {
		return propToShow;
	}

	public void setPropToShow(Property propToShow) {
		this.propToShow = propToShow;
	}

	public int[] getColumnsToShow() {
		return columnsToShow;
	}

	public void setColumnsToShow(int[] columnsToShow) {
		this.columnsToShow = columnsToShow;
	}

}
