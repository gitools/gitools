package es.imim.bg.ztools.ui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.DoubleMatrix3D;

import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.ui.colormatrix.CellDecorationConfig;

public class ResultsModel 
		extends AbstractModel implements ISectionModel {
	
	private static final SectionLayout layout = SectionLayout.left;
	
	private Results results;
	
	private List<DoubleMatrix2D> data;
	
	private int columnCount;
	private int[] columns;
	private String[] columnNames;
	
	private int rowCount;
	private int[] rows;
	private String[] rowNames;

	private int paramCount;
	private int[] params;
	private String[] paramNames;
	private Map<String, Integer> paramIndexMap;
	private int selectedParam;
		
	private int[] selectedColumns;
	private int[] selectedRows;
	private int selectedLeadColumn;
	private int selectedLeadRow;

	private SelectionMode selectionMode;
	
	private String htmlInfo;
	
	private Map<String, CellDecorationConfig> cellDecorationConfig;
	
	public ResultsModel(Results results) {
		this.results = results;
		
		this.cellDecorationConfig = 
			new HashMap<String, CellDecorationConfig>();

		initialize();
	}
	
	public void initialize() {
		columnCount = results.getData().columns();
		columns = new int[columnCount];
		for (int i = 0; i < columnCount; i++)
			columns[i] = i;
		columnNames = results.getColNames();
		
		rowCount = results.getData().rows();
		rows = new int[rowCount];
		for (int i = 0; i < rowCount; i++)
			rows[i] = i;
		rowNames = results.getRowNames();
		
		DoubleMatrix3D cube = results.getData();
		data = new ArrayList<DoubleMatrix2D>(cube.slices());
		for (int i = 0; i < cube.slices(); i++)
			data.add(cube.viewSlice(i));
		
		paramCount = cube.slices();
		params = new int[paramCount];
		paramIndexMap = new HashMap<String, Integer>();
		paramNames = results.getParamNames();
		for (int i = 0; i < paramCount; i++) {
			params[i] = i;
			paramIndexMap.put(paramNames[i], i);
		}
		
		//FIXME
		selectedParam = paramIndexMap.get("right-p-value");
		
		resetSelection();
	}
	
	@Override
	public String getName() {
		return "Results";
	}
	
	@Override
	public ITableModel getTableModel() {
		return this;
	}

	@Override
	public SectionLayout getLayout() {
		return layout;
	}
	
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
		return columnNames[columns[column]];
	}

	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public String getRowName(int row) {
		return rowNames[rows[row]];
	}

	@Override
	public double getValue(int column, int row) {
		return getDataValue(columns[column], rows[row], selectedParam);
	}
	
	@Override
	public void removeColumns(int[] indices) {
		arrayRemove(columns, columnCount, indices);
		columnCount -= indices.length;
		
		firePropertyChange(COLS_CHANGED_PROPERTY, null, null);
	}
	
	@Override
	public void removeRows(int[] indices) {
		arrayRemove(rows, rowCount, indices);
		rowCount -= indices.length;
		
		firePropertyChange(ROWS_CHANGED_PROPERTY, null, null);
	}
	
	@Override
	public void moveColsLeft(int[] indices) {
		if (indices.length == 0 || indices[0] == 0)
			return;
		
		Arrays.sort(indices);
		
		for (int idx : indices) {
			int tmp = columns[idx - 1];
			columns[idx - 1] = columns[idx];
			columns[idx] = tmp;
		}
		
		for (int i = 0; i < selectedColumns.length; i++)
			selectedColumns[i]--;
		
		firePropertyChange(COLS_CHANGED_PROPERTY, null, null);
	}

	@Override
	public void moveColsRight(int[] indices) {
		if (indices.length == 0 
			|| indices[indices.length - 1] == columnCount - 1)
			return;
		
		Arrays.sort(indices);
		
		for (int i = indices.length - 1; i >= 0; i--) {
			int idx = indices[i];
			int tmp = columns[idx + 1];
			columns[idx + 1] = columns[idx];
			columns[idx] = tmp;
		}
		
		for (int i = 0; i < selectedColumns.length; i++)
			selectedColumns[i]++;
		
		firePropertyChange(COLS_CHANGED_PROPERTY, null, null);
	}

	@Override
	public void moveRowsUp(int[] indices) {
		if (indices.length == 0 || indices[0] == 0)
			return;
		
		Arrays.sort(indices);
		
		for (int idx : indices) {
			int tmp = rows[idx - 1];
			rows[idx - 1] = rows[idx];
			rows[idx] = tmp;
		}
		
		for (int i = 0; i < selectedRows.length; i++)
			selectedRows[i]--;
		
		firePropertyChange(ROWS_CHANGED_PROPERTY, null, null);
	}
	
	@Override
	public void moveRowsDown(int[] indices) {
		if (indices.length == 0 
			|| indices[indices.length - 1] == rowCount - 1)
			return;
		
		Arrays.sort(indices);
		
		for (int i = indices.length - 1; i >= 0; i--) {
			int idx = indices[i];
			int tmp = rows[idx + 1];
			rows[idx + 1] = rows[idx];
			rows[idx] = tmp;
		}
		
		for (int i = 0; i < selectedRows.length; i++)
			selectedRows[i]++;
		
		firePropertyChange(ROWS_CHANGED_PROPERTY, null, null);
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
		firePropertyChange(SELECTION_PROPERTY, null, null);
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
				getParamNames();
			
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
		//setSelectedRows(selRows);
		
		int colCount = getColumnCount();
		int[] selCols = new int[colCount];
		for (int i = 0; i < colCount; i++)
			selCols[i] = i;
		//setSelectedColumns(selCols);
		
		setSelection(selCols, selRows);
		
		//firePropertyChange(SELECTION_ALL_PROPERTY, null, null);
	}
	
	@Override
	public void resetSelection() {
		//setSelectedColumns(new int[0]);
		//setSelectedRows(new int[0]);
		
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
		return getParamDecoration(selectedParam);
	}
	
	@Override
	public void setCellDecoration(CellDecorationConfig cellDecoration) {
		CellDecorationConfig old = getCellDecoration();
		setParamDecoration(selectedParam, cellDecoration);
		firePropertyChange(CELL_DECORATION_PROPERTY, old, cellDecoration);
	}

	@Override
	public void sort(final List<SortCriteria> criteriaList) {
		
		//System.out.println(criteriaList);
		
		Integer[] indices = new Integer[rowCount];
		for (int i = 0; i < rowCount; i++)
			indices[i] = rows[i];
		
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
			rows[i] = indices[i];
	}
	
	@Override
	public void sortByFunc(final int[] selCols) {
			
		Integer[] indices = new Integer[rowCount];
		for (int i = 0; i < rowCount; i++)
			indices[i] = rows[i];

		Arrays.sort(indices, new Comparator<Integer>() {
			@Override
			public int compare(Integer idx1, Integer idx2) {
				double se1 = 0, s1 = 0, ss1 = 0;
				double se2 = 0, s2 = 0, ss2 = 0;
				double m1 = 1.0, m2 = 1.0;
				
				int N = selCols.length;
				
				for (int i = 0; i < N; i++) {
					int colIdx = columns[selCols[i]];
					
					double v1 = getDataValue(colIdx, idx1, selectedParam);
				
					m1 *= v1;
					s1 += v1;
					ss1 += v1 * v1;
					se1 += Math.exp(s1);
					
					double v2 = getDataValue(colIdx, idx2, selectedParam);
					
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
			rows[i] = indices[i];
	}
	
	// ISectionModel
	
	@Override
	public int getParamCount() {
		return paramCount;
	}
	
	@Override
	public String getParamName(int param) {
		return paramNames[param];
	}

	@Override
	public int getSelectedParam() {
		return selectedParam;
	}

	@Override
	public void setSelectedParam(int param) {
		int old = this.selectedParam;
		this.selectedParam = param;
		firePropertyChange(SELECTION_PARAM_PROPERTY, old, param);
	}
	
	@Override
	public void setSelectedParam(String paramName) {
		Integer idx = paramIndexMap.get(paramName);
		if (idx != null && idx.intValue() < paramCount)
			setSelectedParam(idx);
	}

	@Override
	public String[] getParamNames() {
		return paramNames;
	}
	
	@Override
	public double getValue(int column, int row, int param) {
		return getDataValue(columns[column], rows[row], param);
	}

	@Override
	public CellDecorationConfig getParamDecoration(int param) {

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
}
