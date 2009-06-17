package org.gitools.model.REVIEW;

import org.gitools.model.Artifact;
import org.gitools.model.matrix.IMatrix;

@Deprecated // for the time being if we need to show a table then will use a figure
public class TableArtifact extends Artifact {

	/** The table associated to this artifact. */
	protected IMatrix matrix;

	/** The group of the rows **/
	protected Group rowsGroup;

	/** The group of the columns **/
	protected Group columnsGroup;

	public TableArtifact() {
	}

	public IMatrix getTableContents() {
		return matrix;
	}

	public void setTableContents(IMatrix matrix) {
		this.matrix = matrix;
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
