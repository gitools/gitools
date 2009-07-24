package org.gitools.model.table;

import org.gitools.model.matrix.element.IElementAdapter;

public class TableView implements ITable {

	ITable contents;
	protected int[] visibleRows;
	protected int[] visibleColumns;

	public TableView() {

	}

	public TableView(ITable table) {
		this.contents = table;
	}

	@Override
	public IElementAdapter getCellColumnAdapter(int column) {
		return contents.getCellColumnAdapter(column);
	}

	@Override
	public ITableColumn getColumn(int index) {
		return contents.getColumn(index);

	}

	@Override
	public int getColumnCount() {
		return contents.getColumnCount();
	}

	@Override
	public String getHeader(int column) {
		return contents.getHeader(column);
	}

	@Override
	public int getRowCount() {
		return contents.getRowCount();
	}

	@Override
	public Object getValue(int row, int column) {
		return contents.getValue(row, column);
	}

	// getters and setters
	
	public ITable getContents() {
		return contents;
	}

	public void setContents(ITable contents) {
		this.contents = contents;
	}

	public int[] getVisibleRows() {
		return visibleRows;
	}

	public void setVisibleRows(int[] visibleRows) {
		this.visibleRows = visibleRows;
	}

	public int[] getVisibleColumns() {
		return visibleColumns;
	}

	public void setVisibleColumns(int[] visibleColumns) {
		this.visibleColumns = visibleColumns;
	}

}
