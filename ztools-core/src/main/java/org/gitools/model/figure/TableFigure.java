package org.gitools.model.figure;

import org.gitools.model.table.IMatrixView;

public abstract class TableFigure extends Figure {
	/**
	 * Its a Figure associated to a table.
	 */
	protected IMatrixView matrixView;

	public TableFigure() {
		super();
	}

	public IMatrixView getTable() {
		return matrixView;
	}

	public void setTable(IMatrixView matrixView) {
		this.matrixView = matrixView;
	}

}
