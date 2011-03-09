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

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JViewport;
import org.gitools.heatmap.drawer.HeatmapPosition;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;

public class HeatmapHeaderMouseController
		implements MouseListener, MouseMotionListener, MouseWheelListener {

	private enum Mode {
		none, selecting, moving
	}

	private final Heatmap heatmap;
	private final JViewport viewPort;
	private final HeatmapHeaderPanel headerPanel;
	private final HeatmapPanel panel;

	private final boolean horizontal;
	private Mode mode;
	private Point point;
	private HeatmapPosition position;

	private int selStart;
	private int selEnd;
	private int selLast;

	private Point startPoint;
	private Point startScrollValue;

	private List<HeatmapMouseListener> listeners = new ArrayList<HeatmapMouseListener>(1);

	public HeatmapHeaderMouseController(HeatmapPanel panel, boolean horizontal) {
		this.heatmap = panel.getHeatmap();
		this.viewPort = horizontal ? panel.getColumnViewPort() : panel.getRowViewPort();
		this.headerPanel = horizontal ? panel.getColumnPanel() : panel.getRowPanel();
		this.panel = panel;
		this.horizontal = horizontal;

		viewPort.addMouseListener(this);
		viewPort.addMouseMotionListener(this);
		viewPort.addMouseWheelListener(this);

		this.mode = Mode.none;
	}

	public void addHeatmapMouseListener(HeatmapMouseListener listener) {
		listeners.add(listener);
	}

	public void removeHeatmapMouseListener(HeatmapMouseListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		panel.requestFocusInWindow();

		point = e.getPoint();
		Point viewPosition = viewPort.getViewPosition();
		point.translate(viewPosition.x, viewPosition.y);
		position = headerPanel.getDrawer().getPosition(point);

		int index = horizontal ? position.column : position.row;
		int limit = horizontal ? heatmap.getMatrixView().getColumnCount()
				: heatmap.getMatrixView().getRowCount();
		if (index < 0 || index >= limit)
			return;

		int row = horizontal ? -1 : index;
		int col = horizontal ? index : -1;

		for (HeatmapMouseListener l : listeners)
			l.mouseClicked(row, col, e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int modifiers = e.getModifiers();
		boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
		boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

		mode = shiftDown && ctrlDown ? Mode.moving : Mode.selecting;
		switch (mode) {
			case selecting: updateSelection(e, false); break;
			case moving: updateScroll(e, false); break;
		}

		panel.requestFocusInWindow();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mode = Mode.none;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//System.out.println("entered");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//System.out.println("exited");
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		//System.out.println("dragged");
		switch (mode) {
			case selecting: updateSelection(e, true); break;
			case moving: updateScroll(e, true); break;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		point = e.getPoint();
		Point viewPosition = viewPort.getViewPosition();
		point.translate(viewPosition.x, viewPosition.y);
		position = headerPanel.getDrawer().getPosition(point);

		int index = horizontal ? position.column : position.row;
		int limit = horizontal ? heatmap.getMatrixView().getColumnCount()
				: heatmap.getMatrixView().getRowCount();
		if (index < 0 || index >= limit)
			return;

		int row = horizontal ? -1 : index;
		int col = horizontal ? index : -1;

		for (HeatmapMouseListener l : listeners)
			l.mouseMoved(row, col, e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int rotation = e.getWheelRotation();
		
		int modifiers = e.getModifiers();
		boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
		boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

		if (!shiftDown && !ctrlDown) {
			HeatmapPosition pos = panel.getScrollPosition();
			
			if (horizontal)
				panel.setScrollColumnPosition(pos.column + rotation);
			else
				panel.setScrollRowPosition(pos.row + rotation);
		}
		else {
			int width = heatmap.getCellWidth() + rotation * -1;
			if (width < 1)
				width = 1;

			int height = heatmap.getCellHeight() + rotation * -1;
			if (height < 1)
				height = 1;

			heatmap.setCellWidth(width);
			heatmap.setCellHeight(height);
		}
	}

	private void updateSelection(MouseEvent e, boolean dragging) {
		point = e.getPoint();
		Point viewPosition = viewPort.getViewPosition();
		point.translate(viewPosition.x, viewPosition.y);
		position = headerPanel.getDrawer().getPosition(point);

		int index = horizontal ? position.column : position.row;
		int limit = horizontal ? heatmap.getMatrixView().getColumnCount()
				: heatmap.getMatrixView().getRowCount();
		if (index < 0 || index >= limit)
			return;

		boolean indexChanged = (selLast != index);
		selLast = index;

		int modifiers = e.getModifiers();
		boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
		boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

		IMatrixView mv = heatmap.getMatrixView();

		int[] sel = null;

		if (!dragging && !shiftDown && !ctrlDown) {
			selStart = selEnd = index;
			sel = new int[] { index };
		}
		else if (ctrlDown) {
			boolean selected = horizontal ?
					mv.isColumnSelected(index) : mv.isRowSelected(index);

			int[] prevSel = horizontal ?
					mv.getSelectedColumns() : mv.getSelectedRows();

			if (dragging && !indexChanged)
				sel = prevSel;
			else {
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
		}
		else if (dragging || shiftDown) {
			selEnd = index;

			int start = selStart <= selEnd ? selStart : selEnd;
			int end = selStart <= selEnd ? selEnd : selStart;
			
			int size = end - start + 1;
			sel = new int[size];
			for (int i = start; i <= end; i++)
				sel[i - start] = i;
		}
		else
			sel = new int[0];

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

		if (horizontal)
			mv.setLeadSelection(-1, index);
		else
			mv.setLeadSelection(index, -1);

		//System.out.println(dragging + " " + shiftDown + " " + ctrlDown + " " + Arrays.toString(sel));
		//System.out.println(mode + " " + point + " -> " + coord);
	}

	private void updateScroll(MouseEvent e, boolean dragging) {
		point = e.getPoint();

		if (!dragging) {
			startPoint = point;
			startScrollValue = panel.getScrollValue();
		}
		else {
			int widthOffset = point.x - startPoint.x;
			int heightOffset = point.y - startPoint.y;

			if (horizontal)
				panel.setScrollColumnValue(startScrollValue.x - widthOffset);
			else
				panel.setScrollRowValue(startScrollValue.y - heightOffset);
		}
	}
}
