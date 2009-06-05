package es.imim.bg.ztools.model;

import es.imim.bg.ztools.table.ITable;

public abstract class TableFigure extends Figure {
	/**
	 * Its a Figure associated to a table.
	 */
	protected ITable table;

	public TableFigure() {
		super();
	}

	public ITable getTable() {
		return table;
	}

	public void setTable(ITable table) {
		this.table = table;
	}

}
