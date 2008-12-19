package es.imim.bg.ztools.ui.colormatrix;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import es.imim.bg.ztools.ui.model.table.ITable;

public class TableModelAdapter implements TableModel {

	private ITable model;
	
	public TableModelAdapter(ITable model) {
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
				model.getColumn(col).toString() : " ";
	}

	public Object getValueAt(int row, int col) {
		return col < model.getColumnCount() ?
				model.getCell(row, col) :
				model.getRow(row).toString();
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
				model.getCellsFacade().getElementClass()
					: String.class;
	}
}
