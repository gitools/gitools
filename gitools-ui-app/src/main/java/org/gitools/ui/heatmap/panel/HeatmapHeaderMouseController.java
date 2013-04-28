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

import org.gitools.core.heatmap.drawer.HeatmapPosition;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.matrix.model.IMatrixViewDimension;
import org.gitools.ui.heatmap.panel.HeatmapPanelInputProcessor.Mode;
import org.gitools.ui.platform.AppFrame;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class HeatmapHeaderMouseController implements MouseListener, MouseMotionListener, MouseWheelListener {

    private final JViewport viewPort;
    private final HeatmapHeaderPanel headerPanel;
    @NotNull
    private final HeatmapPanel panel;
    private final boolean horizontal;
    private Mode mode;
    private Point point;
    private HeatmapPosition position;
    private int selLast;
    private Point startPoint;
    private Point startScrollValue;
    private final IMatrixViewDimension dimension;
    @NotNull
    private final List<HeatmapMouseListener> listeners = new ArrayList<HeatmapMouseListener>(1);
    private int selectionMoveLastIndex;
    private boolean selectionHasMoved = false;

    private int ctrlMask = AppFrame.getOsProperties().getCtrlMask();
    private int shiftMask = AppFrame.getOsProperties().getShiftMask();
    private int altMask = AppFrame.getOsProperties().getAltMask();
    private int metaMask = AppFrame.getOsProperties().getMetaMask();

    private HeatmapPanelInputProcessor ip;

    public HeatmapHeaderMouseController(@NotNull HeatmapPanel panel,
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

        this.mode = Mode.none;
        this.ip = inputProcessor;
    }

    public void addHeatmapMouseListener(HeatmapMouseListener listener) {
        listeners.add(listener);
    }

    @Override
    public void mouseClicked(@NotNull MouseEvent e) {
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

    private int convertToIndex(@NotNull MouseEvent e) {
        point = e.getPoint();
        Point viewPosition = viewPort.getViewPosition();
        point.translate(viewPosition.x, viewPosition.y);
        position = headerPanel.getDrawer().getPosition(point);

        return horizontal ? position.column : position.row;
    }

    @Override
    public void mousePressed(@NotNull MouseEvent e) {
        // Skip right click
        if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
            return;
        }

        // check if it's a already selected
        int index = convertToIndex(e);

        if (isSelectedIndex(index)) {
            mode = Mode.movingSelected;
        } else {
            mode = Mode.selectingRowsAndCols;
        }


        switch (mode) {
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
        if (!isValidIndex(index)) {
            return false;
        }
        return dimension.isSelected(index);
    }

    @Override
    public void mouseReleased(@NotNull MouseEvent e) {
        panel.mouseReleased(e);
        int modifiers = e.getModifiers();
        boolean altDown = ((modifiers & altMask) != 0);

        int index = convertToIndex(e);
        if (mode == Mode.movingSelected) {
            if (!selectionHasMoved) {
                //There was no dragging, user wanted to unselect the selection
                ip.switchSelection(dimension, index, altDown);
            } else {
                selectionHasMoved = false;
            }
        }

        setLeading(e);
        mode = Mode.none;
    }

    private void setLeading(@NotNull MouseEvent e) {
        int index = convertToIndex(e);
        if (isValidIndex(index)) {
            dimension.setSelectionLead(index);
            //horizontal ? ip.setLastSelectedCol(index) : ip.setLastSelectedRow(index);
        }
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
    public void mouseMoved(@NotNull MouseEvent e) {
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
    public void mouseWheelMoved(@NotNull MouseWheelEvent e) {
        int unitsToScroll = e.getUnitsToScroll();

        int modifiers = e.getModifiers();
        boolean ctrlDown = ((modifiers & ctrlMask) != 0);

        mode = (ctrlDown) ? Mode.zooming : Mode.scrolling;

        if (mode == Mode.scrolling) {
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

                int size = header.getSize() + unitsToScroll * -2;
                if (size < 10) {
                    size = 10;
                }
                header.setSize(size);
                header.getHeatmapDimension().updateHeaders();

            }

        }
    }

    private void updateSelectionMove(@NotNull MouseEvent e, boolean dragging) {
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
                if (horizontal) {
                    for (int i = 0; i < indexDiff; i++) {
                        dimension.move(org.gitools.core.matrix.model.Direction.LEFT, dimension.getSelected());
                        ip.shiftSelStart(dimension, -1);
                    }
                } else {
                    for (int i = 0; i < indexDiff; i++) {
                        dimension.move(org.gitools.core.matrix.model.Direction.UP, dimension.getSelected());
                        ip.shiftSelStart(dimension, -1);
                    }
                }
            }

            if (indexDiff < 0) {
                selectionHasMoved = true;
                if (horizontal) {
                    for (int i = 0; i > indexDiff; i--) {
                        dimension.move(org.gitools.core.matrix.model.Direction.RIGHT, dimension.getSelected());
                        ip.shiftSelStart(dimension, 1);
                    }
                } else {
                    for (int i = 0; i > indexDiff; i--) {
                        dimension.move(org.gitools.core.matrix.model.Direction.DOWN, dimension.getSelected());
                        ip.shiftSelStart(dimension, 1);
                    }
                }
            }
        }

        setLeading(e);
    }

    private void updateSelection(@NotNull MouseEvent e, boolean dragging) {
        int index = convertToIndex(e);
        int selStart = 0;
        int selEnd;


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

        } else if (dragging) {

            int[] prevSel = dimension.getSelected();
            selStart = prevSel[0];
            selEnd = prevSel[prevSel.length - 1];

            int start = selStart <= selEnd ? selStart : selEnd;
            int end = selStart <= selEnd ? selEnd : selStart;

            int size = end - start + 1;

            int[] sel = new int[size];
            for (int i = start; i <= end; i++)
                sel[i - start] = i;

            if (horizontal) {
                dimension.setSelected(sel);
            } else {
                dimension.setSelected(sel);
            }

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

    private void updateScroll(@NotNull MouseEvent e, boolean dragging) {
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
