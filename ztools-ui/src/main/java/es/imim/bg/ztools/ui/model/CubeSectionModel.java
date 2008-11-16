package es.imim.bg.ztools.ui.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cern.colt.matrix.DoubleMatrix2D;

import es.imim.bg.ztools.ui.colormatrix.CellDecorationConfig;

public abstract class CubeSectionModel 
		extends AbstractModel implements ISectionModel {

	protected static final SectionLayout layout = SectionLayout.left;
	
	protected List<DoubleMatrix2D> data;
	
	protected int columnCount;
	protected int[] visibleCols;
	protected String[] columnNames;
	
	protected int rowCount;
	protected int[] visibleRows;
	protected String[] rowNames;

	protected int paramCount;
	protected int[] visibleParams;
	protected String[] paramNames;
	protected Map<String, Integer> paramIndexMap;
	protected int currentParam;
		
	protected int[] selectedColumns;
	protected int[] selectedRows;
	protected int selectedLeadColumn;
	protected int selectedLeadRow;

	protected SelectionMode selectionMode;
	
	protected String htmlInfo;
	
	protected Map<String, CellDecorationConfig> cellDecorationConfig;
	
	public CubeSectionModel() {
		this.cellDecorationConfig = 
			new HashMap<String, CellDecorationConfig>();
	}

	// ISectionModel
	
	@Override
	public int getTableCount() {
		return paramCount;
	}
	
	@Override
	public String getTableName(int param) {
		return paramNames[param];
	}

	@Override
	public int getCurrentTable() {
		return currentParam;
	}

	@Override
	public void setCurrentTable(int param) {
		int old = this.currentParam;
		this.currentParam = param;
		firePropertyChange(SELECTION_PARAM_PROPERTY, old, param);
	}
	
	@Override
	public void setCurrentTable(String paramName) {
		Integer idx = paramIndexMap.get(paramName);
		if (idx != null && idx.intValue() < paramCount)
			setCurrentTable(idx);
	}

	@Override
	public String[] getTableNames() {
		return paramNames;
	}
	
	@Override
	public double getValue(int column, int row, int param) {
		return getDataValue(visibleCols[column], visibleRows[row], param);
	}

	@Override
	public int addTable(String paramName, DoubleMatrix2D correctedMatrix) {
		data.add(correctedMatrix);
		int newParam = paramCount++;
		String[] oldParamNames = paramNames;
		paramNames = new String[paramCount];
		System.arraycopy(oldParamNames, 0, paramNames, 0, newParam);
		paramNames[newParam] = paramName;
		
		firePropertyChange(PARAM_COUNT_CHANGED_PROPERTY);
		
		return newParam;
	}
	
	@Override
	public CellDecorationConfig getTableDecoration(int param) {

		final String paramName = paramNames[param];
		
		CellDecorationConfig config = 
			cellDecorationConfig.get(paramName);
	
		if (config == null) {
			config = new CellDecorationConfig();
			cellDecorationConfig.put(paramName, config);
		}
		return config;
	}
	
	private void setParamDecoration(
			int param, CellDecorationConfig cellDecoration) {

		final String paramName = paramNames[param];
		cellDecorationConfig.put(paramName, cellDecoration);
	}
	
	@Override
	public ITableModel getTableModel() {
		return this;
	}

	@Override
	public SectionLayout getLayout() {
		return layout;
	}

	public int getParamIndex(String name) {
		return paramIndexMap.get(name);
	}
	
	// ITableModel
	
	@Override
	public int getColumnCount() {
		return columnCount;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[visibleCols[column]];
	}

	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public String getRowName(int row) {
		return rowNames[visibleRows[row]];
	}

	@Override
	public double getValue(int column, int row) {
		return getDataValue(
				visibleCols[column], 
				visibleRows[row], 
				currentParam);
	}
	
	@Override
	public DoubleMatrix2D getVisibleMatrix() {
		DoubleMatrix2D matrix =
			data.get(currentParam)
				.viewSelection(visibleRows, visibleCols);
		return matrix;
	}
	
	@Override
	public DoubleMatrix2D getMatrix() {
		DoubleMatrix2D matrix =
			data.get(currentParam);
		return matrix;
	}
	
	@Override
	public int[] getVisibleColumns() {
		return visibleCols;
	}
	
	@Override
	public void setVisibleColumns(int[] indices) {
		this.visibleCols = indices;
	}
	
	@Override
	public void hideColumns(int[] indices) {
		arrayRemove(visibleCols, columnCount, indices);
		columnCount -= indices.length;
		
		firePropertyChange(COLS_CHANGED_PROPERTY);
	}
	
	@Override
	public int[] getVisibleRows() {
		return visibleRows;
	}
	
	@Override
	public void setVisibleRows(int[] indices) {
		this.visibleRows = indices;
	}
	
	@Override
	public void hideRows(int[] indices) {
		arrayRemove(visibleRows, rowCount, indices);
		rowCount -= indices.length;
		
		firePropertyChange(ROWS_CHANGED_PROPERTY);
	}
	
	@Override
	public void moveColsLeft(int[] indices) {
		
		arrayMoveLeft(visibleCols, indices, selectedColumns);
		
		firePropertyChange(COLS_CHANGED_PROPERTY);
	}

	@Override
	public void moveColsRight(int[] indices) {
		
		arrayMoveRight(visibleCols, columnCount, indices, selectedColumns);
		
		firePropertyChange(COLS_CHANGED_PROPERTY);
	}

	@Override
	public void moveRowsUp(int[] indices) {
		
		arrayMoveLeft(visibleRows, indices, selectedRows);
		
		firePropertyChange(ROWS_CHANGED_PROPERTY);
	}
	
	@Override
	public void moveRowsDown(int[] indices) {
		
		arrayMoveRight(visibleRows, rowCount, indices, selectedRows);
		
		firePropertyChange(ROWS_CHANGED_PROPERTY);
	}

	@Override
	public void setSelectionMode(SelectionMode mode) {
		SelectionMode old = this.selectionMode;
		this.selectionMode = mode;
		firePropertyChange(SELECTION_MODE_PROPERTY, old, mode);
	}
	
	@Override
	public SelectionMode getSelectionMode() {
		return selectionMode;
	}
	
	@Override
	public int[] getSelectedColumns() {
		return selectedColumns;
	}
	
	//@Override
	@Deprecated
	public void setSelectedColumns(int[] selectedColumns) {
		int[] oldSelCols= this.selectedColumns;
		this.selectedColumns = selectedColumns;
		firePropertyChange(SELECTION_COLUMNS_PROPERTY, oldSelCols, selectedColumns);
	}
	
	@Override
	public int[] getSelectedRows() {
		return selectedRows;
	}
	
	//@Override
	@Deprecated
	public void setSelectedRows(int[] selectedRows) {
		int[] oldSelRows = this.selectedRows;
		this.selectedRows = selectedRows;
		firePropertyChange(SELECTION_ROWS_PROPERTY, oldSelRows, selectedRows);
	}

	@Override
	public void setSelection(
			int[] selectedColumns,
			int[] selectedRows) {
		
		this.selectedColumns = selectedColumns;
		this.selectedRows = selectedRows;
		firePropertyChange(SELECTION_PROPERTY);
	}
	
	@Override
	public void setLeadSelection(int column, int row) {
		int[] oldLead = new int[] {selectedLeadColumn, selectedLeadRow};
		
		selectedLeadColumn = column;
		selectedLeadRow = row;
		
		if (column < 0 || row < 0)
			setHtmlInfo("");
		else {
			final String colName = 
				getColumnName(column);
			final String rowName = 
				getRowName(row);
			final String[] paramNames = 
				getTableNames();
			
			StringBuilder sb = new StringBuilder();
			
			// Render parameters & values
			sb.append("<p><b>Column:</b><br>");
			sb.append(colName).append("</p>");
			sb.append("<p><b>Row:</b><br>");
			sb.append(rowName).append("</p>");
			
			for (int i = 0; i < paramNames.length; i++) {
				final String paramName = paramNames[i];
				final String value = Double.toString(
						getValue(column, row, i));
				
				sb.append("<p><b>");
				sb.append(paramName);
				sb.append(":</b><br>");
				sb.append(value);
				sb.append("</p>");
			}
			
			setHtmlInfo(sb.toString());
		}
		
		firePropertyChange(SELECTION_LEAD_PROPERTY, oldLead, 
				new int[] {selectedLeadColumn, selectedLeadRow});
	}
	
	@Override
	public int getSelectedLeadColumn() {
		return selectedLeadColumn;
	}
	
	@Override
	public int getSelectedLeadRow() {
		return selectedLeadRow;
	}
	
	@Override
	public void selectAll() {
		int rowCount = getRowCount();
		int[] selRows = new int[rowCount];
		for (int i = 0; i < rowCount; i++)
			selRows[i] = i;
		
		int colCount = getColumnCount();
		int[] selCols = new int[colCount];
		for (int i = 0; i < colCount; i++)
			selCols[i] = i;
		
		setSelection(selCols, selRows);
	}
	
	@Override
	public void resetSelection() {		
		setSelection(new int[0], new int[0]);
		setLeadSelection(-1, -1);		
	}
	
	@Override
	public String getHtmlInfo() {
		return htmlInfo;
	}
	
	@Override
	public void setHtmlInfo(String htmlInfo) {
		String old = this.htmlInfo;
		this.htmlInfo = htmlInfo;
		firePropertyChange(HTML_INFO_PROPERTY, old, htmlInfo);
	}
	
	@Override
	public CellDecorationConfig getCellDecoration() {
		return getTableDecoration(currentParam);
	}
	
	@Override
	public void setCellDecoration(CellDecorationConfig cellDecoration) {
		CellDecorationConfig old = getCellDecoration();
		setParamDecoration(currentParam, cellDecoration);
		firePropertyChange(CELL_DECORATION_PROPERTY, old, cellDecoration);
	}

	@Override
	public void sort(final List<SortCriteria> criteriaList) {
		
		//System.out.println(criteriaList);
		
		Integer[] indices = new Integer[rowCount];
		for (int i = 0; i < rowCount; i++)
			indices[i] = visibleRows[i];
		
		Arrays.sort(indices, new Comparator<Integer>() {
			@Override
			public int compare(Integer idx1, Integer idx2) {
				int res = 0;
				for (int i = 0; i < criteriaList.size() && res == 0; i++) {
					final SortCriteria c = criteriaList.get(i);
				
					double v1 = getDataValue(
							c.getColumnIndex(), idx1, c.getParamIndex());
					
					double v2 = getDataValue(
							c.getColumnIndex(), idx2, c.getParamIndex());
					
					res = (int) Math.signum(
							c.isAscending() ? (v1 - v2) : (v2 - v1));
				}
				return res;
			}
		});
		
		for (int i = 0; i < rowCount; i++)
			visibleRows[i] = indices[i];
	}
	
	@Override
	public void sortByFunc(final int[] selCols) {
			
		Integer[] indices = new Integer[rowCount];
		for (int i = 0; i < rowCount; i++)
			indices[i] = visibleRows[i];

		Arrays.sort(indices, new Comparator<Integer>() {
			@Override
			public int compare(Integer idx1, Integer idx2) {
				double se1 = 0, s1 = 0, ss1 = 0;
				double se2 = 0, s2 = 0, ss2 = 0;
				double m1 = 1.0, m2 = 1.0;
				
				int N = selCols.length;
				
				for (int i = 0; i < N; i++) {
					int colIdx = visibleCols[selCols[i]];
					
					double v1 = getDataValue(colIdx, idx1, currentParam);
				
					m1 *= v1;
					s1 += v1;
					ss1 += v1 * v1;
					se1 += Math.exp(s1);
					
					double v2 = getDataValue(colIdx, idx2, currentParam);
					
					m2 *= v2;
					s2 += v2;
					ss2 += v2 * v2;
					se2 += Math.exp(s2);
				}
				
				double var1 = (N * ss1) - (s1 * s1) / (N * N);
				double var2 = (N * ss2) - (s2 * s2) / (N * N);
				
				int res = (int) Math.signum(se1 - se2);
				//int res = (int) Math.signum(se1 - se2);
				//int res = (int) Math.signum(m1 - m2);
				return res != 0 ? res : (int) Math.signum(var1 - var2);
			}
		});
		
		for (int i = 0; i < rowCount; i++)
			visibleRows[i] = indices[i];
	}

	@Override
	public void fireMatrixChanged() {
		firePropertyChange(MATRIX_CHANGED_PROPERTY);
	}
	
	// internal
	
	private double getDataValue(int column, int row, int param) {
		return data.get(param).get(row, column);
	}
	
	private void arrayRemove(int[] array, int size, int[] indices) {
		for (int i = 0; i < indices.length; i++)
			arrayRemove(array, size, indices[i] - i);
	}
	
	private void arrayRemove(int[] array, int size, int index) {
		System.arraycopy(array, index + 1, array, index, size - index - 1);
	}
	
	private void arrayMoveLeft(
			int[] array, int[] indices, int[] selection) {
		
		if (indices.length == 0 || indices[0] == 0)
			return;
		
		Arrays.sort(indices);
		
		for (int idx : indices) {
			int tmp = array[idx - 1];
			array[idx - 1] = array[idx];
			array[idx] = tmp;
		}
		
		for (int i = 0; i < selection.length; i++)
			selection[i]--;
	}
	
	private void arrayMoveRight(
			int[] array, int count, int[] indices, int[] selection) {
		
		if (indices.length == 0 
				|| indices[indices.length - 1] == count - 1)
				return;
			
		Arrays.sort(indices);
		
		for (int i = indices.length - 1; i >= 0; i--) {
			int idx = indices[i];
			int tmp = array[idx + 1];
			array[idx + 1] = array[idx];
			array[idx] = tmp;
		}
		
		for (int i = 0; i < selection.length; i++)
			selection[i]++;
	}
}
