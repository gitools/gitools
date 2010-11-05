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

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JViewport;
import org.gitools.heatmap.drawer.HeatmapPosition;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;

public class HeatmapBodyMouseController
		implements MouseListener, MouseMotionListener, MouseWheelListener {

	private enum Mode {
		none, selecting, moving
	}

	private final Heatmap heatmap;
	private final JViewport viewPort;
	private final HeatmapPanel panel;
	private final HeatmapBodyPanel bodyPanel;

	private Mode mode;
	private Point point;
	private HeatmapPosition coord;

	private Point startPoint;
	private Point startScrollValue;
	
	public HeatmapBodyMouseController(HeatmapPanel panel) {
		this.heatmap = panel.getHeatmap();
		this.viewPort = panel.getBodyViewPort();
		this.bodyPanel = panel.getBodyPanel();
		this.panel = panel;

		viewPort.addMouseListener(this);
		viewPort.addMouseMotionListener(this);
		viewPort.addMouseWheelListener(this);
		//viewPort.addKeyListener(this);

		this.mode = Mode.none;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		panel.requestFocusInWindow();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int modifiers = e.getModifiers();
		boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
		boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

		mode = shiftDown || ctrlDown ? Mode.moving : Mode.selecting;
		switch (mode) {
			case selecting: updateSelection(e); break;
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
			case selecting: updateSelection(e); break;
			case moving: updateScroll(e, true); break;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//System.out.println("moved");
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int rotation = e.getWheelRotation();

		int modifiers = e.getModifiers();
		boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
		boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

		if (!shiftDown && !ctrlDown) {
			HeatmapPosition pos = panel.getScrollPosition();
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

	private void updateSelection(MouseEvent e) {
		point = e.getPoint();
		Point viewPosition = viewPort.getViewPosition();
		point.translate(viewPosition.x, viewPosition.y);
		coord = bodyPanel.getDrawer().getPosition(point);

		IMatrixView mv = heatmap.getMatrixView();
		mv.setLeadSelection(coord.row, coord.column);
		mv.setSelectedRows(new int[0]);
		mv.setSelectedColumns(new int[0]);

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

			panel.setScrollColumnValue(startScrollValue.x - widthOffset);
			panel.setScrollRowValue(startScrollValue.y - heightOffset);
		}
	}
}
