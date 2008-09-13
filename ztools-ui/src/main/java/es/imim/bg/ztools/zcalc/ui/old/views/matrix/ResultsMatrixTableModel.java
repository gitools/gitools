package es.imim.bg.ztools.zcalc.ui.old.views.matrix;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.ObjectMatrix2D;

public class ResultsMatrixTableModel implements TableModel {

	private String[] colNames;
	private String[] rowNames;
	private ObjectMatrix2D data;
	private int paramIndex;
	
	public ResultsMatrixTableModel(
			String[] rowNames,
			String[] colNames,
			ObjectMatrix2D data,
			int paramIndex) {
		
		this.rowNames = rowNames;
		this.colNames = colNames;
		this.data = data;
		this.paramIndex = paramIndex;
	}
	
	public int getParamIndex() {
		return paramIndex;
	}
	
	public void setParamIndex(int paramIndex) {
		this.paramIndex = paramIndex;
	}
	
	public int getRowCount() {
		return data.rows();
	}
	
	public int getColumnCount() {
		return data.columns() + 1;
	}

	public String getColumnName(int col) {
		return col > 0 ? colNames[col - 1] : " ";
	}

	public Object getValueAt(int row, int col) {
		if (col > 0) {
			DoubleMatrix1D params = (DoubleMatrix1D) data.get(row, col - 1);
			return params.get(paramIndex);
		}
		else
			return rowNames[row];
	}
	
	public void setValueAt(Object value, int row, int col) {
		//data.set(row, col, (Double) value);
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void addTableModelListener(TableModelListener arg0) {
	}
	
	public void removeTableModelListener(TableModelListener arg0) {
	}
	
	public Class<?> getColumnClass(int col) {
		return col > 0 ? Double.class : String.class;
	}

}
