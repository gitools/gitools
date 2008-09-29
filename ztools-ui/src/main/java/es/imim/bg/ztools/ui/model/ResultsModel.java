package es.imim.bg.ztools.ui.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import es.imim.bg.ztools.model.Results;

public class ResultsModel {

	private Results results;
	
	private int columnCount;
	private int[] columns;
	
	private int rowCount;
	private int[] rows;

	private int[] selectedColumns;
	private int[] selectedRows;
	
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
		
		resetSelection();
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
		//System.out.println(Arrays.toString(columns));
		arrayRemove(columns, columnCount, indices);
		//System.out.println(Arrays.toString(columns));
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

	public void resetSelection() {
		selectedColumns = new int[0];
		selectedRows = new int[0];
	}
	
	public int[] getSelectedColumns() {
		return selectedColumns;
	}
	
	public void setSelectedColumns(int[] selectedColumns) {
		this.selectedColumns = selectedColumns;
	}
	
	public int[] getSelectedRows() {
		return selectedRows;
	}
	
	public void setSelectedRows(int[] selectedRows) {
		this.selectedRows = selectedRows;
	}

	public void sort(final List<SortCriteria> criteriaList) {
		
		System.out.println(criteriaList);
		
		Integer[] indices = new Integer[rowCount];
		for (int i = 0; i < rowCount; i++)
			indices[i] = rows[i];
		
		Arrays.sort(indices, new Comparator<Integer>() {
			@Override
			public int compare(Integer idx1, Integer idx2) {
				int res = 0;
				for (int i = 0; i < criteriaList.size() && res == 0; i++) {
					final SortCriteria c = criteriaList.get(i);
				
					double v1 = results.getDataValue(
							c.getColumnIndex(), idx1, c.getParamIndex());
					
					double v2 = results.getDataValue(
							c.getColumnIndex(), idx2, c.getParamIndex());
					
					res = (int) Math.signum(
							c.isAscending() ? (v1 - v2) : (v2 - v1));
				}
				return res;
			}
		});
		
		for (int i = 0; i < rowCount; i++)
			rows[i] = indices[i];
	}
	
}
