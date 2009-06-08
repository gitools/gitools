package org.gitools.model.REVIEW;

import org.gitools.model.Artifact;
import org.gitools.model.table.ITableContents;

public class TableArtifact extends Artifact {

	/** The table associated to this artifact. */
	protected ITableContents tableContents;

	/** The group of the rows **/
	protected Group rowsGroup;

	/** The group of the columns **/
	protected Group columnsGroup;

	public TableArtifact() {
	}

	public ITableContents getTableContents() {
		return tableContents;
	}

	public void setTableContents(ITableContents tableContents) {
		this.tableContents = tableContents;
	}

	public Group getRowsGroup() {
		return rowsGroup;
	}

	public void setRowsGroup(Group rowsGroup) {
		this.rowsGroup = rowsGroup;
	}

	public Group getColumnsGroup() {
		return columnsGroup;
	}

	public void setColumnsGroup(Group columnsGroup) {
		this.columnsGroup = columnsGroup;
	}

}
