/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.heatmap.panel;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.drawer.HeatmapPosition;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.ui.platform.AppFrame;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class HeatmapBodyMouseController implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    private enum Mode {
        none, selecting, moving, zooming, scrolling
    }

    private final Heatmap heatmap;
    private final JViewport viewPort;
    @NotNull
    private final HeatmapPanel panel;
    private final HeatmapBodyPanel bodyPanel;

    private Mode mode;
    private Point point;
    private HeatmapPosition coord;

    private Point startPoint;
    private Point startScrollValue;

    private int keyPressed;

    private HeatmapKeyboardController keyboardController;

    private int ctrlMask = AppFrame.getOsProperties().getCtrlMask();
    private int shiftMask = AppFrame.getOsProperties().getShiftMask();
    private int altMask = AppFrame.getOsProperties().getAltMask();
    private int metaMask = AppFrame.getOsProperties().getMetaMask();

    @NotNull
    private final List<HeatmapMouseListener> listeners = new ArrayList<HeatmapMouseListener>(1);

    public HeatmapBodyMouseController(@NotNull HeatmapPanel panel) {
        this.heatmap = panel.getHeatmap();
        this.viewPort = panel.getBodyViewPort();
        this.bodyPanel = panel.getBodyPanel();
        this.panel = panel;

        viewPort.addMouseListener(this);
        viewPort.addMouseMotionListener(this);
        viewPort.addMouseWheelListener(this);

        panel.addKeyListener(this);
        keyboardController = new HeatmapKeyboardController(panel);

        this.mode = Mode.none;
    }

    public void addHeatmapMouseListener(HeatmapMouseListener listener) {
        listeners.add(listener);
    }

    public void removeHeatmapMouseListener(HeatmapMouseListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void mouseClicked(@NotNull MouseEvent e) {
        panel.requestFocusInWindow();

        point = e.getPoint();
        Point viewPosition = viewPort.getViewPosition();
        point.translate(viewPosition.x, viewPosition.y);
        coord = bodyPanel.getDrawer().getPosition(point);

        for (HeatmapMouseListener l : listeners)
            l.mouseClicked(coord.row, coord.column, e);
    }

    @Override
    public void mousePressed(@NotNull MouseEvent e) {
        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & shiftMask) != 0);
        boolean ctrlDown = ((modifiers & ctrlMask) != 0);

        mode = shiftDown || ctrlDown ? Mode.moving : Mode.selecting;
        switch (mode) {
            case selecting:
                updateSelection(e);
                break;
            case moving:
                updateScroll(e, false);
                break;
        }

        panel.requestFocusInWindow();
    }

    @Override
    public void mouseReleased(@NotNull MouseEvent e) {
        panel.mouseReleased(e);
        mode = Mode.none;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(@NotNull MouseEvent e) {
        switch (mode) {
            case selecting:
                updateSelection(e);
                break;
            case moving:
                updateScroll(e, true);
                break;
        }
    }

    @Override
    public void mouseMoved(@NotNull MouseEvent e) {
        point = e.getPoint();
        Point viewPosition = viewPort.getViewPosition();
        point.translate(viewPosition.x, viewPosition.y);
        coord = bodyPanel.getDrawer().getPosition(point);

        for (HeatmapMouseListener l : listeners)
            l.mouseMoved(coord.row, coord.column, e);
    }

    @Override
    public void mouseWheelMoved(@NotNull MouseWheelEvent e) {
        int unitsToScroll = e.getUnitsToScroll();

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & shiftMask) != 0);
        boolean ctrlDown = ((modifiers & ctrlMask) != 0);

        mode = (ctrlDown) ? Mode.zooming : Mode.scrolling;

        if (mode == Mode.scrolling && !shiftDown) {
            HeatmapPosition pos = panel.getScrollPosition();
            panel.setScrollRowPosition(pos.row + unitsToScroll);
        } else if (mode == Mode.scrolling && shiftDown) {
            HeatmapPosition pos = panel.getScrollPosition();
            panel.setScrollColumnPosition(pos.column + unitsToScroll);
        } else if (mode == Mode.zooming) {

            if (keyPressed != KeyEvent.VK_R) {
                int width = heatmap.getColumns().getCellSize() + unitsToScroll * -1;
                if (width < 1) {
                    width = 1;
                }
                heatmap.getColumns().setCellSize(width);
            }

            if (keyPressed != KeyEvent.VK_C) {
                int height = heatmap.getRows().getCellSize() + unitsToScroll * -1;
                if (height < 1) {
                    height = 1;
                }
                heatmap.getRows().setCellSize(height);
            }
        }
    }

    private void updateSelection(@NotNull MouseEvent e) {
        point = e.getPoint();
        Point viewPosition = viewPort.getViewPosition();
        point.translate(viewPosition.x, viewPosition.y);
        coord = bodyPanel.getDrawer().getPosition(point);

        IMatrixView mv = heatmap;
        mv.getRows().setSelectionLead(coord.row);
        mv.getColumns().setSelectionLead(coord.column);
        mv.getRows().setSelected(new int[0]);
        mv.getColumns().setSelected(new int[0]);
    }

    private void updateScroll(@NotNull MouseEvent e, boolean dragging) {
        point = e.getPoint();

        if (!dragging) {
            startPoint = point;
            startScrollValue = panel.getScrollValue();
        } else {
            int widthOffset = point.x - startPoint.x;
            int heightOffset = point.y - startPoint.y;

            panel.setScrollColumnValue(startScrollValue.x - widthOffset);
            panel.setScrollRowValue(startScrollValue.y - heightOffset);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        keyboardController.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressed = e.getKeyCode();
        keyboardController.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed = -1;
        keyboardController.keyReleased(e);
    }
}

