package org.gitools.model.figure;

import org.gitools.model.table.ITableView;

public class TableFigure extends Figure {
	
	protected ITableView tableView;

	public TableFigure() {
	}

	public ITableView getTableView() {
		return tableView;
	}
	
	public void setTableView(ITableView tableView) {
		this.tableView = tableView;
	}
}
