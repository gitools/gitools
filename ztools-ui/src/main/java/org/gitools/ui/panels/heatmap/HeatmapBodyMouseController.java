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

package org.gitools.ui.panels.heatmap;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JViewport;
import org.gitools.model.figure.MatrixFigure;

/**
 *
 * @author chris
 */
public class HeatmapBodyMouseController
		implements MouseListener, MouseMotionListener, MouseWheelListener {

	private void updateSelection(MouseEvent e) {
		point = e.getPoint();
		Point viewPosition = viewPort.getViewPosition();
		point.translate(viewPosition.x, viewPosition.y);
		coord = panel.getDrawer().getCoordinates(point);
		heatmap.getMatrixView().setLeadSelection(coord.x, coord.y);
		System.out.println(mode + " " + point + " -> " + coord);
	}

	private enum Mode {
		none, selecting, moving
	}

	private final MatrixFigure heatmap;
	private final JViewport viewPort;
	private final HeatmapBodyPanel panel;

	private Mode mode;
	private Point point;
	private Point coord;

	public HeatmapBodyMouseController(MatrixFigure heatmap, JViewport viewPort, HeatmapBodyPanel panel) {
		this.heatmap = heatmap;
		this.viewPort = viewPort;
		this.panel = panel;

		viewPort.addMouseListener(this);
		viewPort.addMouseMotionListener(this);
		viewPort.addMouseWheelListener(this);

		this.mode = Mode.none;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("clicked");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int modifiers = e.getModifiers();
		boolean ctrlDown = ((modifiers & InputEvent.CTRL_DOWN_MASK) != 0);
		mode = ctrlDown ? Mode.moving : Mode.selecting;
		switch (mode) {
			case selecting: updateSelection(e); break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mode = Mode.none;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("entered");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("exited");
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("dragged");
		switch (mode) {
			case selecting: updateSelection(e); break;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println("moved");
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		System.out.println("wheel moved");
	}

}
