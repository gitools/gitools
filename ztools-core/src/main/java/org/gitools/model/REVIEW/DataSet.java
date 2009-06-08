package org.gitools.model.REVIEW;

import org.gitools.model.figure.TableFigure;
import org.gitools.model.matrix.element.Property;

public abstract class DataSet extends TableFigure {
	/**
	 * Data or Results filtered
	 */
	protected TableArtifact artifactTable;

	/** Property to show **/
	protected Property propToShow;

	/** Positions of the columns to show **/
	protected int[] columnsToShow;

	public DataSet() {
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

	public int[] getColumnsToShow() {
		return columnsToShow;
	}

	public void setColumnsToShow(int[] columnsToShow) {
		this.columnsToShow = columnsToShow;
	}

}
