package es.imim.bg.ztools.table;

import es.imim.bg.ztools.model.ResultsMatrix;
import es.imim.bg.ztools.table.element.IElementAdapter;

public class ResultsMatrixTableContentsAdapter 
		implements ITableContents {

	protected ResultsMatrix results;
	
	public ResultsMatrixTableContentsAdapter(ResultsMatrix results) {
		this.results = results;
	}
	
	@Override
	public int getRowCount() {
		return results.getRowCount();
	}
	
	@Override
	public Object getRow(int index) {
		return results.getRow(index);
	}

	@Override
	public IElementAdapter getRowAdapter() {
		return results.getRowAdapter();
	}
	
	/* columns */
	
	@Override
	public int getColumnCount() {
		return results.getColumnCount();
	}
	
	@Override
	public Object getColumn(int index) {
		return results.getColumn(index);
	}

	@Override
	public IElementAdapter getColumnAdapter() {
		return results.getColumnAdapter();
	}

	/* cells */
	
	@Override
	public Object getCell(int row, int column) {
		return results.getCell(row, column);
	}

	@Override
	public Object getCellValue(int row, int column, int index) {
		return results.getCellValue(row, column, index);
	}

	@Override
	public Object getCellValue(int row, int column, String id) {
		return results.getCellValue(row, column, id);
	}
	
	@Override
	public void setCellValue(int row, int column, int index, Object value) {
		results.setCellValue(row, column, index, value);
	}

	@Override
	public void setCellValue(int row, int column, String id, Object value) {
		results.setCellValue(row, column, id, value);
	}
	
	@Override
	public IElementAdapter getCellAdapter() {
		return results.getCellAdapter();
	}
}
