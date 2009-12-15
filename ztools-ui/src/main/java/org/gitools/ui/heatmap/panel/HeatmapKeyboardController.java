/*
 *  Copyright 2009 chris.
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
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;

public class HeatmapKeyboardController extends KeyAdapter {

	private HeatmapPanel panel;
	private Heatmap hm;

	HeatmapKeyboardController(HeatmapPanel panel) {
		this.panel = panel;
		this.hm = panel.getHeatmap();

		panel.addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		IMatrixView mv = hm.getMatrixView();

		int row = mv.getSelectionLeadRow();
		int col = mv.getSelectionLeadColumn();

		int key = e.getKeyCode();

		int modifiers = e.getModifiers();
		boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
		boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

		if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP
				|| key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {

			if (!shiftDown && !ctrlDown)
				changeLead(e);
			else if (!shiftDown && ctrlDown)
				moveLead(e);
			else if (shiftDown && !ctrlDown)
				moveSelection(e);
		}
		else {
			switch (key) {
				case KeyEvent.VK_DELETE:
					hideLead(e);
					break;
			}
		}
	}

	private void changeLead(KeyEvent e) {
		IMatrixView mv = hm.getMatrixView();
		int row = mv.getSelectionLeadRow();
		int col = mv.getSelectionLeadColumn();
		
		switch (e.getKeyCode()) {
			case KeyEvent.VK_DOWN:
				if (row < mv.getRowCount() - 1) {
					row++;
				}
				break;
			case KeyEvent.VK_UP:
				if (row > 0) {
					row--;
				} else if (col != -1) {
					row = -1;
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (col >= 0 && col < mv.getColumnCount() - 1) {
					col++;
				} else if (row != -1) {
					col = -1;
				}
				break;
			case KeyEvent.VK_LEFT:
				if (col > 0) {
					col--;
				} else if (col == -1) {
					col = mv.getColumnCount() - 1;
				}
				break;
		}

		mv.setLeadSelection(row, col);
	}

	private void hideLead(KeyEvent e) {
		IMatrixView mv = hm.getMatrixView();
		int row = mv.getSelectionLeadRow();
		int col = mv.getSelectionLeadColumn();

		if (row != -1) {
			int[] rows = mv.getVisibleRows();
			int[] vrows = new int[rows.length - 1];
			System.arraycopy(rows, 0, vrows, 0, row);
			System.arraycopy(rows, row + 1, vrows, row, rows.length - row - 1);
			mv.setVisibleRows(vrows);

			if (row > mv.getRowCount() - 1)
				row = mv.getRowCount() - 1;
		}

		if (col != -1) {
			int[] cols = mv.getVisibleColumns();
			int[] vcols = new int[cols.length - 1];
			System.arraycopy(cols, 0, vcols, 0, col);
			System.arraycopy(cols, col + 1, vcols, col, cols.length - col - 1);
			mv.setVisibleColumns(vcols);

			if (col > mv.getColumnCount() - 1)
				col = mv.getColumnCount() - 1;
		}

		mv.setLeadSelection(row, col);
	}

	private void moveLead(KeyEvent e) {
		IMatrixView mv = hm.getMatrixView();
		int row = mv.getSelectionLeadRow();
		int col = mv.getSelectionLeadColumn();

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

		changeLead(e);
		e.consume();
	}

	private void moveSelection(KeyEvent e) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
