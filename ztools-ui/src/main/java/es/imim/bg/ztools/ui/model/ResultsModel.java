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
	
	public void sortByFunc(final int[] selCols, final int paramIndex) {
			
		Integer[] indices = new Integer[rowCount];
		for (int i = 0; i < rowCount; i++)
			indices[i] = rows[i];
		
		Arrays.sort(indices, new Comparator<Integer>() {
			@Override
			public int compare(Integer idx1, Integer idx2) {
				double se1 = 0, s1 = 0, ss1 = 0;
				double se2 = 0, s2 = 0, ss2 = 0;
				
				int N = selCols.length;
				
				for (int i = 0; i < N; i++) {
					double v1 = results.getDataValue(
							selCols[i], idx1, paramIndex);
					
					s1 += v1;
					ss1 += v1 * v1;
					se1 += Math.exp(s1);
					
					double v2 = results.getDataValue(
							selCols[i], idx2, paramIndex);
					
					s2 += v2;
					ss2 += v2 * v2;
					se2 += Math.exp(s2);
				}
				
				double var1 = (N * ss1) - (s1 * s1) / (N * N);
				double var2 = (N * ss2) - (s2 * s2) / (N * N);
				
				int res = (int) Math.signum(se1 - se2);
				return res != 0 ? res : (int) Math.signum(var1 - var2);
			}
		});
		
		for (int i = 0; i < rowCount; i++)
			rows[i] = indices[i];
	}
}
