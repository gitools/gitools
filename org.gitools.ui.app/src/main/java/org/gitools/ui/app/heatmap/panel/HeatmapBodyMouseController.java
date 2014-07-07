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
package org.gitools.ui.app.heatmap.panel;

import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.interaction.Interaction;
import org.gitools.ui.platform.os.OSProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class HeatmapBodyMouseController implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    private final IMatrixView heatmap;
    private final JViewport viewPort;

    private final HeatmapPanel panel;
    private final HeatmapBodyPanel bodyPanel;

    private Interaction interaction;
    private Point point;
    private HeatmapPosition coord;

    private Point startPoint;
    private Point startScrollValue;

    private int keyPressed;

    private HeatmapKeyboardController keyboardController;
    private HeatmapPanelInputProcessor ip;

    private int shiftMask = OSProperties.get().getShiftMask();
    private int ctrlMask = OSProperties.get().getCtrlMask();
    private int altMask = OSProperties.get().getAltMask();
    private int metaMask = OSProperties.get().getMetaMask();


    private final List<HeatmapMouseListener> listeners = new ArrayList<>(1);

    public HeatmapBodyMouseController(HeatmapPanel panel, HeatmapPanelInputProcessor inputProcessor) {
        this.heatmap = panel.getHeatmap();
        this.viewPort = panel.getBodyViewPort();
        this.bodyPanel = panel.getBodyPanel();
        this.panel = panel;

        viewPort.addMouseListener(this);
        viewPort.addMouseMotionListener(this);
        viewPort.addMouseWheelListener(this);

        panel.addKeyListener(this);
        this.ip = inputProcessor;
        keyboardController = new HeatmapKeyboardController(heatmap, inputProcessor);

        this.interaction = Interaction.none;
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
        coord = bodyPanel.getDrawer().getPosition(point);

        for (HeatmapMouseListener l : listeners)
            l.mouseClicked(coord.row, coord.column, e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & shiftMask) != 0);

        interaction = shiftDown ? Interaction.selectingRowsAndCols : Interaction.dragging;
        switch (interaction) {
            case dragging:
                updateLeadSelection(e);
                dragHeatmap(e, true);
                break;
            case selectingRowsAndCols:
                selectRowsAndCols(e);
                break;
        }
        panel.requestFocusInWindow();
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        panel.mouseReleased(e);
        interaction = Interaction.none;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dragHeatmap(e, false);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        point = e.getPoint();
        Point viewPosition = viewPort.getViewPosition();
        point.translate(viewPosition.x, viewPosition.y);
        coord = bodyPanel.getDrawer().getPosition(point);

        for (HeatmapMouseListener l : listeners)
            l.mouseMoved(coord.row, coord.column, e);
    }

    public static HeatmapPosition wheelPosition;
    public static Point wheelPoint;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int unitsToScroll = e.getUnitsToScroll();

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & shiftMask) != 0);
        boolean ctrlDown = ((modifiers & ctrlMask) != 0);

        if (ctrlDown) {
            if (wheelPoint == null && wheelPosition == null) {
                wheelPoint = e.getPoint();
                Point viewPosition = viewPort.getViewPosition();
                Point absPoint = new Point(wheelPoint);
                absPoint.translate(viewPosition.x, viewPosition.y);
                wheelPosition = bodyPanel.getDrawer().getPosition(absPoint);
            }
            wheelPoint = e.getPoint();
        } else {
            wheelPoint = null;
            wheelPosition = null;
        }

        interaction = (ctrlDown) ? Interaction.zooming : Interaction.scrolling;

        if (interaction == Interaction.scrolling) {
            ip.scroll(unitsToScroll, shiftDown);

        } else if (interaction == Interaction.zooming) {

            ip.zoomHeatmap(unitsToScroll > 0 ? 1 : -1);
        }
    }


    private void updateLeadSelection(MouseEvent e) {
        point = e.getPoint();
        Point viewPosition = viewPort.getViewPosition();
        point.translate(viewPosition.x, viewPosition.y);
        coord = bodyPanel.getDrawer().getPosition(point);

        ip.setLead(coord.row, coord.column);
    }

    private void selectRowsAndCols(MouseEvent e) {
        int corner1Row = ip.getLead(heatmap.getRows());
        int corner1Col = ip.getLead(heatmap.getColumns());

        point = e.getPoint();
        Point viewPosition = viewPort.getViewPosition();
        point.translate(viewPosition.x, viewPosition.y);
        coord = bodyPanel.getDrawer().getPosition(point);

        ip.addToSelected(corner1Col, coord.column, heatmap.getColumns());
        ip.addToSelected(corner1Row, coord.row, heatmap.getRows());
        ip.setLastSelectedCol(coord.column);
        ip.setLastSelectedRow(coord.row);
    }

    private void dragHeatmap(MouseEvent e, boolean initStartPoint) {
        point = e.getPoint();

        if (initStartPoint) {
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

        if (wheelPoint != null) {
            wheelPoint = null;
            wheelPosition = null;
        }

        keyPressed = -1;
        keyboardController.keyReleased(e);
    }
}

