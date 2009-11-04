package org.gitools.ui.panels.matrix;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.gitools.model.matrix.IMatrixView;

public class MatrixModelAdapter implements TableModel {

	private IMatrixView model;
	
	public MatrixModelAdapter(IMatrixView model) {
		this.model = model;
	}
	
	public int getRowCount() {
		return model.getRowCount();
	}
	
	public int getColumnCount() {
		return model.getColumnCount() + 1;
	}

	public String getColumnName(int col) {
		return col < model.getColumnCount() ? 
				model.getColumnLabel(col) : " ";
	}

	public Object getValueAt(int row, int col) {
		return col < model.getColumnCount() ?
				model.getCell(row, col) :
				model.getRowLabel(row);
	}
	
	public void setValueAt(Object value, int row, int col) {
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void addTableModelListener(TableModelListener arg0) {
	}
	
	public void removeTableModelListener(TableModelListener arg0) {
	}
	
	public Class<?> getColumnClass(int col) {
		return col < model.getColumnCount() ? 
				model.getCellAdapter().getElementClass()
					: String.class;
	}
}
