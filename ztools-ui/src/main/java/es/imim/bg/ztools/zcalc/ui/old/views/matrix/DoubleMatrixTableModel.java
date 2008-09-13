package es.imim.bg.ztools.zcalc.ui.old.views.matrix;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import cern.colt.matrix.DoubleMatrix2D;

public class DoubleMatrixTableModel implements TableModel {

	private String[] colNames;
	private String[] rowNames;
	private DoubleMatrix2D data;
	
	public DoubleMatrixTableModel(
			String[] rowNames,
			String[] colNames,
			DoubleMatrix2D data) {
		
		this.rowNames = rowNames;
		this.colNames = colNames;
		this.data = data;
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
		return col > 0 ? data.get(row, col - 1) : rowNames[row];
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
