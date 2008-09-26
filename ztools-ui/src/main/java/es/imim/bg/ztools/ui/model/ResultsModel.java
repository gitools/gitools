package es.imim.bg.ztools.ui.model;

import es.imim.bg.ztools.model.Results;

public class ResultsModel {

	private Results results;
	
	private int columnCount;
	private int[] columns;
	
	private int rowCount;
	private int[] rows;
	
	public ResultsModel(Results results) {
		this.results = results;
		
		reset();
	}
	
	public void reset() {
		columnCount = results.getData().columns();
		columns = new int[columnCount];
		for (int i = 0; i < columnCount; i++)
			columns[i] = i;
		
		rowCount = results.getData().rows();
		rows = new int[rowCount];
		for (int i = 0; i < rowCount; i++)
			rows[i] = i;
	}
	
	/*public Results getResults() {
		return results;
	}*/

	public int getColumnCount() {
		return columnCount;
	}

	public String getColumnName(int column) {
		return results.getColNames()[columns[column]];
	}

	public int getRowCount() {
		return rowCount;
	}

	public String getRowName(int row) {
		return results.getRowNames()[rows[row]];
	}

	public int getParamCount() {
		return results.getParamNames().length;
	}
	
	public String getParamName(int param) {
		return results.getParamNames()[param];
	}
	
	public Double getValue(int column, int row, int param) {
		return results.getDataValue(
				columns[column], rows[row], param);
	}

	public String[] getParamNames() {
		return results.getParamNames();
	}

	public int getParamIndex(String name) {
		return results.getParamIndex(name);
	}
	
	public void removeColumns(int[] indices) {
		arrayRemove(columns, columnCount, indices);
		columnCount -= indices.length;
	}
	
	public void removeRows(int[] indices) {
		arrayRemove(rows, rowCount, indices);
		rowCount -= indices.length;
	}

	private void arrayRemove(int[] array, int size, int[] indices) {
		for (int i = 0; i < indices.length; i++)
			arrayRemove(array, size, indices[i] - i);
	}
	
	private void arrayRemove(int[] array, int size, int index) {
		System.arraycopy(array, index + 1, array, index, size - index - 1);
	}
}
