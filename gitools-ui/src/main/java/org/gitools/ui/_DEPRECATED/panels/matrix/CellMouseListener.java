package org.gitools.ui._DEPRECATED.panels.matrix;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import org.gitools.ui.model.SelectionMode;

public class CellMouseListener extends MouseInputAdapter {
	
	private MatrixPanel matrixPanel;
	
	protected int startRow;
	protected int endRow;
	protected int startColumn;
	protected int endColumn;

	protected int[] oldRowSelection = new int[0];
	
	public CellMouseListener(MatrixPanel matrixPanel) {
		this.matrixPanel = matrixPanel;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		int pressedColumn = matrixPanel.columnAtPoint(e.getPoint());
		
		int lastcol = matrixPanel.getColumnCount() - 1;
		if (lastcol == pressedColumn) {
			matrixPanel.setSelectionMode(SelectionMode.rows);
			startRow = matrixPanel.rowAtPoint(e.getPoint());
			startColumn = pressedColumn;
			matrixPanel.setLeadSelection(startRow, -1);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		int lastColumn = matrixPanel.getColumnCount() - 1;
		if (lastColumn == matrixPanel.columnAtPoint(e.getPoint())) {
			matrixPanel.setSelectedColumns(new int[0]);
			oldRowSelection = matrixPanel.getSelectedRows();
		}
		else
			setLead(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int pressedColumn = matrixPanel.columnAtPoint(e.getPoint());
		
		int lastColumn = matrixPanel.getColumnCount() - 1;
		if (lastColumn == pressedColumn) {
			
			endRow = matrixPanel.rowAtPoint(e.getPoint());
			endColumn = pressedColumn;
			matrixPanel.setLeadSelection(endRow, -1);

			int rows = Math.abs(endRow - startRow) + 1;
			
			int[] newSelectedRows = new int[rows];
			
			int start = (endRow < startRow) ? endRow : startRow;
			int stop = (endRow > startRow) ? endRow : startRow;
			int counter = 0;
			for (int i = start; i <= stop ; i++){
				newSelectedRows[counter] = i;
				counter++;
			}
			
			int[] selectedRows;
			
			if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK)
				selectedRows = SelectionUtils.mergeWithoutRepetitions(
						matrixPanel.getSelectedRows(), newSelectedRows);					
			else
				selectedRows = newSelectedRows;
						
			matrixPanel.setSelectedCells(new int[0], selectedRows);
			oldRowSelection = matrixPanel.getSelectedRows();
		}
	}
	
	private void setLead(MouseEvent e){
		matrixPanel.setSelectionMode(SelectionMode.cells);
		
		int row = matrixPanel.rowAtPoint(e.getPoint());
		int col = matrixPanel.columnAtPoint(e.getPoint());
		
		matrixPanel.setSelectedCells(new int[] { col }, new int[] { row });
		matrixPanel.setLeadSelection(row, col);
	}
}
