package org.gitools.model.figure;

import org.gitools.model.matrix.ITableView;

public class TableFigure extends Figure {

	private static final long serialVersionUID = 9006041133309250290L;

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
