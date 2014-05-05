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

import org.gitools.api.matrix.view.Direction;
import org.gitools.api.matrix.view.IMatrixViewDimension;
import org.gitools.heatmap.AbstractMatrixViewDimension;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.platform.os.OSProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static org.gitools.ui.app.heatmap.panel.HeatmapPanelInputProcessor.Mode.*;
import static org.gitools.ui.app.heatmap.panel.HeatmapPanelInputProcessor.getInteractionMode;
import static org.gitools.ui.app.heatmap.panel.HeatmapPanelInputProcessor.setMode;

public class HeatmapHeaderMouseController implements MouseListener, MouseMotionListener, MouseWheelListener {

    private final JViewport viewPort;
    private final HeatmapHeaderPanel headerPanel;

    private final HeatmapPanel panel;
    private final boolean horizontal;
    private Point point;
    private HeatmapPosition position;
    private int selLast;
    private Point startPoint;
    private Point startScrollValue;
    private final IMatrixViewDimension dimension;

    private final List<HeatmapMouseListener> listeners = new ArrayList<>(1);
    private int selectionMoveLastIndex;
    private boolean selectionHasMoved = false;

    private int ctrlMask = OSProperties.get().getCtrlMask();
    private int shiftMask = OSProperties.get().getShiftMask();
    private int altMask = OSProperties.get().getAltMask();
    private int metaMask = OSProperties.get().getMetaMask();
    private Timer timer;

    private HeatmapPanelInputProcessor ip;
    private MouseEvent lastMovingEvent;

    public HeatmapHeaderMouseController(HeatmapPanel panel,
                                        HeatmapPanelInputProcessor inputProcessor,
                                        boolean horizontal) {
        this.viewPort = horizontal ? panel.getColumnViewPort() : panel.getRowViewPort();
        this.headerPanel = horizontal ? panel.getColumnPanel() : panel.getRowPanel();
        this.panel = panel;
        this.horizontal = horizontal;
        this.dimension = horizontal ? panel.getHeatmap().getColumns() : panel.getHeatmap().getRows();

        viewPort.addMouseListener(this);
        viewPort.addMouseMotionListener(this);
        viewPort.addMouseWheelListener(this);

        setMode(none);
        this.ip = inputProcessor;
        this.timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                processStoredEvents();
            }
        });
    }

    private void processStoredEvents() {
        if (lastMovingEvent != null) {
            updateSelectionMove(lastMovingEvent, true);
            lastMovingEvent = null;
        }
    }

    public void addHeatmapMouseListener(HeatmapMouseListener listener) {
        listeners.add(listener);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Skip right click
        if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
            return;
        }

        panel.requestFocusInWindow();

        int index = convertToIndex(e);
        if (!isValidIndex(index)) {
            return;
        }

        int row = horizontal ? -1 : index;
        int col = horizontal ? index : -1;

        for (HeatmapMouseListener l : listeners)
            l.mouseClicked(row, col, e);
    }

    private int convertToIndex(MouseEvent e) {
        point = e.getPoint();
        Point viewPosition = viewPort.getViewPosition();
        point.translate(viewPosition.x, viewPosition.y);
        position = headerPanel.getDrawer().getPosition(point);

        return horizontal ? position.column : position.row;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Skip right click
        if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
            return;
        }

        // check if it's a already selected
        int index = convertToIndex(e);

        if (isSelectedIndex(index)) {
            setMode(movingSelected);
        } else {
            setMode(selectingRowsAndCols);
        }


        switch (getInteractionMode()) {
            case selectingRowsAndCols:
                updateSelection(e, false);
                break;
            case movingSelected:
                selectionHasMoved = false;
                updateSelectionMove(e, false);
                break;
        }

        panel.requestFocusInWindow();
    }

    private boolean isSelectedIndex(int index) {
        return isValidIndex(index) && dimension.getSelected().contains(dimension.getLabel(index));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        panel.mouseReleased(e);
        int modifiers = e.getModifiers();
        boolean altDown = ((modifiers & altMask) != 0);

        int index = convertToIndex(e);
        if (getInteractionMode() == movingSelected) {
            if (!selectionHasMoved) {
                //There was no dragging, user wanted to unselect the selection
                ip.switchSelection(dimension, index, altDown);
                setLeading(e);
                setMode(none);
            } else {
                setMode(none);
                dimension.forceUpdate(AbstractMatrixViewDimension.PROPERTY_VISIBLE);
                //updateSelectionMove(e, true);
                selectionHasMoved = false;
            }
        }
    }

    private void setLeading(MouseEvent e) {
        int index = convertToIndex(e);
        if (isValidIndex(index)) {
            dimension.setFocus(dimension.getLabel(index));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (getInteractionMode()) {
            case selectingRowsAndCols:
                updateSelection(e, true);
                break;
            case moving:
                updateScroll(e, true);
                break;
            case movingSelected:
                updateSelectionMove(e, true);
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int index = convertToIndex(e);
        if (!isValidIndex(index)) {
            return;
        }

        int row = horizontal ? -1 : index;
        int col = horizontal ? index : -1;

        for (HeatmapMouseListener l : listeners)
            l.mouseMoved(row, col, e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int unitsToScroll = e.getUnitsToScroll();

        int modifiers = e.getModifiers();
        boolean ctrlDown = ((modifiers & ctrlMask) != 0);

        setMode((ctrlDown) ? zooming : scrolling);

        if (getInteractionMode() == scrolling) {
            HeatmapPosition pos = panel.getScrollPosition();

            if (horizontal) {
                panel.setScrollColumnPosition(pos.column + unitsToScroll);
            } else {
                panel.setScrollRowPosition(pos.row + unitsToScroll);
            }
        } else {

            if (ip.isKeyPressed(KeyEvent.VK_C) ||
                    ip.isKeyPressed(KeyEvent.VK_R)) {
                ip.zoomHeatmap(unitsToScroll);
            } else {

                point = e.getPoint();
                Point viewPosition = viewPort.getViewPosition();
                point.translate(viewPosition.x, viewPosition.y);

                HeatmapHeader header = headerPanel.getHeaderDrawer().getHeader(point);

                if (ip.isKeyPressed(KeyEvent.VK_M)) {
                    int margin = header.getMargin() + unitsToScroll * -2;

                    if (margin < 1) {
                        margin = 1;
                    } else {
                        int size = header.getSize() + unitsToScroll * -2;
                        if (size < 10) {
                            size = 10;
                        }
                        header.setSize(size);
                    }
                    header.setMargin(margin);
                } else {
                    int size = header.getSize() + unitsToScroll * -2;
                    if (size < 10) {
                        size = 10;
                    }
                    header.setSize(size);
                }

                header.getHeatmapDimension().updateHeaders();

            }

        }
    }

    private void updateSelectionMove(MouseEvent e, boolean dragging) {

        if (!timer.isRunning()) {
            timer.start();
        } else {
            lastMovingEvent = e;
            return;
        }

        // Scroll heatmap if needed
        Point point = e.getPoint();
        if (horizontal) {
            int left = (int) point.getX() - viewPort.getWidth();
            if (left > 0) {
                panel.setScrollColumnValue(panel.getScrollValue().x + left);
            } else {
                if (point.getX() < 0) {
                    panel.setScrollColumnValue(panel.getScrollValue().x + (int) point.getX());
                }
            }
        } else {
            int down = (int) point.getY() - viewPort.getHeight();
            if (down > 0) {
                panel.setScrollRowValue(panel.getScrollValue().y + down);
            } else {
                if (point.getY() < 0) {
                    panel.setScrollRowValue(panel.getScrollValue().y + (int) point.getY());
                }
            }
        }

        int index = convertToIndex(e);

        if (!isValidIndex(index)) {
            return;
        }

        if (!dragging) {
            selectionMoveLastIndex = index;
        } else {

            int indexDiff = selectionMoveLastIndex - index;
            selectionMoveLastIndex = index;

            if (indexDiff > 0) {
                selectionHasMoved = true;
                for (int i = 0; i < indexDiff; i++) {
                    dimension.move(Direction.LEFT, dimension.getSelected());
                    ip.shiftSelStart(dimension, -1);
                }
            }

            if (indexDiff < 0) {
                selectionHasMoved = true;
                for (int i = 0; i > indexDiff; i--) {
                    dimension.move(Direction.RIGHT, dimension.getSelected());
                    ip.shiftSelStart(dimension, 1);
                }
            }
        }
    }

    private void updateSelection(MouseEvent e, boolean dragging) {
        int index = convertToIndex(e);

        if (!isValidIndex(index)) {
            return;
        }

        boolean indexChanged = (selLast != index);
        if (indexChanged) {
            setLeading(e);
        }
        selLast = index;

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & shiftMask) != 0);

        if (!dragging && !shiftDown) {
            ip.switchSelection(dimension, index, false);
        } else if (shiftDown) {

            int lastSelected = horizontal ? ip.getLastSelectedCol() : ip.getLastSelectedRow();
            ip.addToSelected(lastSelected, index, dimension);
            ip.setLastSelected(index, horizontal);

        }
    }

    private int getIndexCount() {
        return dimension.size();
    }

    private boolean isValidIndex(int index) {
        if (index < 0 || index >= getIndexCount()) {
            return false;
        }
        return true;
    }

    private void updateScroll(MouseEvent e, boolean dragging) {
        point = e.getPoint();

        if (!dragging) {
            startPoint = point;
            startScrollValue = panel.getScrollValue();
        } else {
            int widthOffset = point.x - startPoint.x;
            int heightOffset = point.y - startPoint.y;

            if (horizontal) {
                panel.setScrollColumnValue(startScrollValue.x - widthOffset);
            } else {
                panel.setScrollRowValue(startScrollValue.y - heightOffset);
            }
        }
    }

}
