package org.gitools.ui._DEPRECATED.panels.matrix;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import org.gitools.ui.model.SelectionMode;

public class ColumnsMouseListener extends MouseInputAdapter {

	private MatrixPanel matrixPanel;

	protected int startColumn;
	protected int endColumn;

	public ColumnsMouseListener(MatrixPanel matrixPanel) {
		this.matrixPanel = matrixPanel;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {

		int pressedColumn = matrixPanel.columnAtPoint(e.getPoint());

		int lastColumn = matrixPanel.getColumnCount() - 1;

		if (lastColumn == pressedColumn) {
			matrixPanel.selectAll();
			// for (ColorMatrixListener listener : listeners)
			// listener.selectionAll();
		} 
		else {
			matrixPanel.setSelectionMode(SelectionMode.columns);

			startColumn = pressedColumn;
			int[] selectedColumns;

			if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK) {
				int nb = matrixPanel.getSelectedColumns().length + 1;
				selectedColumns = new int[nb];
				System.arraycopy(matrixPanel.getSelectedColumns(), 0, selectedColumns, 0, nb - 1);
				selectedColumns[nb - 1] = pressedColumn;
			} else
				selectedColumns = new int[] { pressedColumn };

			matrixPanel.setSelectedCells(selectedColumns, new int[0]);
			matrixPanel.setLeadSelection(-1, pressedColumn);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		endColumn = matrixPanel.columnAtPoint(e.getPoint());
		matrixPanel.setLeadSelection(-1, endColumn);

		int numCols = Math.abs(endColumn - startColumn) + 1;
		
		int[] newSelectedColumns = new int[numCols];

		int start = (endColumn < startColumn) ? endColumn : startColumn;
		int stop = (endColumn > startColumn) ? endColumn : startColumn;
		int counter = 0;
		for (int i = start; i <= stop; i++) {
			newSelectedColumns[counter] = i;
			counter++;
		}

		int[] selectedColumns;
		if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK)
			selectedColumns = SelectionUtils.mergeWithoutRepetitions(
					matrixPanel.getSelectedColumns(), newSelectedColumns);
		else
			selectedColumns = newSelectedColumns;

		matrixPanel.setSelectedCells(selectedColumns, new int[0]);
	}
}
