/*
 *  Copyright 2009 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.heatmap.panel;

import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;

public class HeatmapKeyboardController extends KeyAdapter {

	private HeatmapPanel panel;
	private Heatmap hm;

	private int selStart;
	private int selEnd;
	private int selLast;

	HeatmapKeyboardController(HeatmapPanel panel) {
		this.panel = panel;
		this.hm = panel.getHeatmap();

		selStart = selEnd = selLast = -1;
		
		panel.addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		IMatrixView mv = hm.getMatrixView();

		int row = mv.getLeadSelectionRow();
		int col = mv.getLeadSelectionColumn();

		int key = e.getKeyCode();

		int modifiers = e.getModifiers();
		boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
		boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);
		boolean altDown = ((modifiers & InputEvent.ALT_MASK) != 0);

		if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP
				|| key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT
				|| key == KeyEvent.VK_HOME || key == KeyEvent.VK_END
				|| key == KeyEvent.VK_PAGE_UP || key == KeyEvent.VK_PAGE_DOWN) {

			if (((!shiftDown && !ctrlDown)
					|| (shiftDown && !ctrlDown)
					|| (!shiftDown && ctrlDown)) && !altDown)
				changeLead(e);
			else if (shiftDown && ctrlDown && !altDown)
				moveLead(e);
			else if (!shiftDown && !ctrlDown && altDown)
				moveSelection(e);

			if (ctrlDown)
				e.consume();
		}
		else {
			switch (key) {
				case KeyEvent.VK_DELETE:
					hideLead(e);
					break;

				case KeyEvent.VK_SPACE:
					selectLead(e);
					break;
			}
		}
	}

	//TODO shift selection
	private void changeLead(KeyEvent e) {

		int modifiers = e.getModifiers();
		boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
		boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

		IMatrixView mv = hm.getMatrixView();
		int row = mv.getLeadSelectionRow();
		int col = mv.getLeadSelectionColumn();

		int prevRow = row;
		int prevCol = col;

		final int rowPageSize = 10; //FIXME should depend on screen size
		final int colPageSize = 10; //FIXME should depend on screen size

		switch (e.getKeyCode()) {
			case KeyEvent.VK_DOWN:
				if (row < mv.getRowCount() - 1)
					row++;
				break;
			case KeyEvent.VK_UP:
				if (row > 0)
					row--;
				else if (col != -1)
					row = -1;
				break;
			case KeyEvent.VK_RIGHT:
				if (col >= 0 && col < mv.getColumnCount() - 1)
					col++;
				else if (row != -1)
					col = -1;
				break;
			case KeyEvent.VK_LEFT:
				if (col > 0)
					col--;
				else if (col == -1)
					col = mv.getColumnCount() - 1;
				break;
			case KeyEvent.VK_PAGE_UP:
				if (row != -1) {
					row -= rowPageSize;
					if (row < 0)
						row = 0;
				}
				else if (row == -1 && col != -1) {
					col -= colPageSize;
					if (col < 0)
						col = 0;
				}
				break;
			case KeyEvent.VK_PAGE_DOWN:
				if (row != -1) {
					row += rowPageSize;
					if (row >= mv.getRowCount())
						row = mv.getRowCount() - 1;
				}
				else if (row == -1 && col != -1) {
					col += colPageSize;
					if (col >= mv.getColumnCount())
						col = mv.getColumnCount() - 1;
				}
				break;
			case KeyEvent.VK_HOME:
				if (row != -1)
					row = 0;
				else if (row == -1 && col != -1)
					col = 0;
				break;
			case KeyEvent.VK_END:
				if (row != -1)
					row = mv.getRowCount() - 1;
				else if (row == -1 && col != -1)
					col = mv.getColumnCount() - 1;
				break;
		}

		// update selection

		boolean clearSelection = true;

		boolean onRow = row != -1 && col == -1;
		boolean onBody = row != -1 && col != -1;
		
		int[] sel = null;

		if (onBody || (!shiftDown && !ctrlDown))
			selStart = selEnd = -1;
		else if (!onBody && (shiftDown && !ctrlDown)) {
			selEnd = onRow ? row : col;
			if (selStart == -1)
				selStart = selLast != -1 ? selLast : selEnd;

			int start = selStart <= selEnd ? selStart : selEnd;
			int end = selStart <= selEnd ? selEnd : selStart;

			int size = end - start + 1;
			sel = new int[size];
			for (int i = start; i <= end; i++)
				sel[i - start] = i;

			clearSelection = false;
		}
		else if (!shiftDown && ctrlDown)
			clearSelection = false;

		selLast = !onBody ? (onRow ? row : col) : -1;
		
		mv.setLeadSelection(row, col);

		if (clearSelection)
			mv.clearSelection();
		else if (!onBody && sel != null) {
			if (onRow)
				mv.setSelectedRows(sel);
			else
				mv.setSelectedColumns(sel);
		}
	}

	private void hideLead(KeyEvent e) {
		IMatrixView mv = hm.getMatrixView();
		int row = mv.getLeadSelectionRow();
		int col = mv.getLeadSelectionColumn();

		if (row != -1)
			mv.hideRows(mv.getSelectedRows());

		if (col != -1)
			mv.hideColumns(mv.getSelectedColumns());
	}

	private void moveLead(KeyEvent e) {
		IMatrixView mv = hm.getMatrixView();
		int row = mv.getLeadSelectionRow();
		int col = mv.getLeadSelectionColumn();

		switch (e.getKeyCode()) {
			case KeyEvent.VK_DOWN:
				if (row >= 0 && row < mv.getRowCount() - 1)
					mv.moveRowsDown(new int[] { row });
				break;
			case KeyEvent.VK_UP:
				if (row > 0 && row < mv.getRowCount())
					mv.moveRowsUp(new int[] { row });
				break;
			case KeyEvent.VK_RIGHT:
				if (col >= 0 && col < mv.getColumnCount() - 1)
					mv.moveColumnsRight(new int[] { col });
				break;
			case KeyEvent.VK_LEFT:
				if (col > 0 && col < mv.getColumnCount())
					mv.moveColumnsLeft(new int[] { col });
				break;
		}
	}

	private void moveSelection(KeyEvent e) {
		IMatrixView mv = hm.getMatrixView();
		int row = mv.getLeadSelectionRow();
		int col = mv.getLeadSelectionColumn();

		if (row != -1 && col != -1)
			return;

		boolean horizontal = (row == -1 && col >= 0);

		int[] sel = horizontal ? mv.getSelectedColumns() : mv.getSelectedRows();
		
		switch (e.getKeyCode()) {
			case KeyEvent.VK_DOWN:
				if (row >= 0 && row < mv.getRowCount() - 1)
					mv.moveRowsDown(sel);
				break;
			case KeyEvent.VK_UP:
				if (row > 0 && row < mv.getRowCount())
					mv.moveRowsUp(sel);
				break;
			case KeyEvent.VK_RIGHT:
				if (col >= 0 && col < mv.getColumnCount() - 1)
					mv.moveColumnsRight(sel);
				break;
			case KeyEvent.VK_LEFT:
				if (col > 0 && col < mv.getColumnCount())
					mv.moveColumnsLeft(sel);
				break;
		}
	}

	private void selectLead(KeyEvent e) {
		IMatrixView mv = hm.getMatrixView();
		int row = mv.getLeadSelectionRow();
		int col = mv.getLeadSelectionColumn();

		if (row != -1 && col != -1)
			return;

		int modifiers = e.getModifiers();
		boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
		boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

		boolean horizontal = (row == -1 && col >= 0);

		int index = horizontal ? col : row;

		int[] sel = null;

		if (!ctrlDown) {
			if (row >= 0 && col == -1)
				sel = new int[] { row };
			else if (row == -1 && col >= 0)
				sel = new int[] { col };
		}
		else {
			boolean selected = horizontal ?
					mv.isColumnSelected(col) : mv.isRowSelected(row);

			int[] prevSel = horizontal ?
					mv.getSelectedColumns() : mv.getSelectedRows();

			if (!selected) {
				sel = new int[prevSel.length + 1];
				System.arraycopy(prevSel, 0, sel, 0, prevSel.length);
				sel[sel.length - 1] = index;
				Arrays.sort(sel);
			}
			else {
				sel = new int[prevSel.length - 1];
				int i = 0;
				int j = 0;
				while (i < prevSel.length) {
					if (prevSel[i] != index)
						sel[j++] = prevSel[i];
					i++;
				}
			}
		}

		if (horizontal) {
			if (mv.getSelectedRows().length != 0)
				mv.setSelectedRows(new int[0]);
			mv.setSelectedColumns(sel);
		}
		else {
			if (mv.getSelectedColumns().length != 0)
				mv.setSelectedColumns(new int[0]);
			mv.setSelectedRows(sel);
		}
	}
}
