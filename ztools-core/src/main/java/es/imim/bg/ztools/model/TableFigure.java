package es.imim.bg.ztools.model;


public abstract class TableFigure extends Figure {
	/**
	 * Its a Figure associated to a table.
	 */
	protected TableArtifact artifactTable;

	/** Positions of the rows to show **/
	protected int[] rowsToShow;

	public TableFigure() {
		super();
	}

	public TableArtifact getArtifactTable() {
		return artifactTable;
	}

	public void setArtifactTable(TableArtifact artifactTable) {
		this.artifactTable = artifactTable;
	}

	public int[] getRowsToShow() {
		return rowsToShow;
	}

	public void setRowsToShow(int[] rowsToShow) {
		this.rowsToShow = rowsToShow;
	}

}
