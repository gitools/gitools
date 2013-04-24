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

import com.google.common.primitives.Ints;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.drawer.HeatmapPosition;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.matrix.model.IMatrixView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class HeatmapHeaderMouseController implements MouseListener, MouseMotionListener, MouseWheelListener {

    private final Heatmap heatmap;
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
    @NotNull
    private final List<HeatmapMouseListener> listeners = new ArrayList<HeatmapMouseListener>(1);
    private int selectionMoveLastIndex;

    public HeatmapHeaderMouseController(@NotNull HeatmapPanel panel, boolean horizontal) {
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
            int modifiers = e.getModifiers();
            boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
            boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

            mode = shiftDown && ctrlDown ? Mode.moving : Mode.selecting;
        }


        switch (mode) {
            case selecting:
                updateSelection(e, false);
                break;
            case moving:
                updateScroll(e, false);
                break;
            case movingSelected:
                updateSelectionMove(e, false);
                break;
        }

        panel.requestFocusInWindow();
    }

    private boolean isSelectedIndex(int index) {
        if (!isValidIndex(index)) {
            return false;
        }

        IMatrixView matrixView = heatmap;
        if (horizontal) {
            return matrixView.getColumns().isSelected(index);
        }

        return matrixView.getRows().isSelected(index);

    }

    @Override
    public void mouseReleased(@NotNull MouseEvent e) {
        panel.mouseReleased(e);

        setLeading(e);
        mode = Mode.none;
    }

    private void setLeading(@NotNull MouseEvent e) {
        int index = convertToIndex(e);
        if (isValidIndex(index)) {
            IMatrixView matrixView = heatmap;
            if (horizontal) {
                matrixView.getColumns().setSelectionLead(index);
                panel.setColSelStart(index);
            } else {
                matrixView.getRows().setSelectionLead(index);
                panel.setRowSelStart(index);
            }
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
            case selecting:
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
        boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
        boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

        if (!shiftDown && !ctrlDown) {
            HeatmapPosition pos = panel.getScrollPosition();

            if (horizontal) {
                panel.setScrollColumnPosition(pos.column + unitsToScroll);
            } else {
                panel.setScrollRowPosition(pos.row + unitsToScroll);
            }
        } else {

            point = e.getPoint();
            Point viewPosition = viewPort.getViewPosition();
            point.translate(viewPosition.x, viewPosition.y);

            HeatmapHeader header = headerPanel.getHeaderDrawer().getHeader(point);

            int size = header.getSize() + unitsToScroll * -2;
            if (size < 5) {
                size = 5;
            }
            header.setSize(size);
            header.getHeatmapDimension().updateHeaders();
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

            IMatrixView matrixView = heatmap;
            if (indexDiff > 0) {
                if (horizontal) {
                    for (int i = 0; i < indexDiff; i++) {
                        matrixView.getColumns().move(org.gitools.core.matrix.model.Direction.LEFT, matrixView.getColumns().getSelected());
                        panel.shiftSelStart(matrixView.getColumns(), -1);
                    }
                } else {
                    for (int i = 0; i < indexDiff; i++) {
                        matrixView.getRows().move(org.gitools.core.matrix.model.Direction.UP, matrixView.getRows().getSelected());
                        panel.shiftSelStart(matrixView.getRows(), -1);
                    }
                }
            }

            if (indexDiff < 0) {
                if (horizontal) {
                    for (int i = 0; i > indexDiff; i--) {
                        matrixView.getColumns().move(org.gitools.core.matrix.model.Direction.RIGHT, matrixView.getColumns().getSelected());
                        panel.shiftSelStart(matrixView.getColumns(), 1);
                    }
                } else {
                    for (int i = 0; i > indexDiff; i--) {
                        matrixView.getRows().move(org.gitools.core.matrix.model.Direction.DOWN, matrixView.getRows().getSelected());
                        panel.shiftSelStart(matrixView.getRows(), 1);
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
            selStart = horizontal ? panel.getColSelStart() : panel.getRowSelStart();
            setLeading(e);
        }
        selLast = index;

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
        boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

        IMatrixView mv = heatmap;

        int[] sel = new int[0];

        if (!dragging && !shiftDown && !ctrlDown) {
            selStart = selEnd = index;
            sel = new int[]{index};
        } else if (ctrlDown) {
            boolean selected = horizontal ? mv.getColumns().isSelected(index) : mv.getRows().isSelected(index);

            int[] prevSel = horizontal ? mv.getColumns().getSelected() : mv.getRows().getSelected();

            if (dragging && !indexChanged) {
                sel = prevSel;
            } else {
                if (!selected) {
                    sel = new int[prevSel.length + 1];
                    System.arraycopy(prevSel, 0, sel, 0, prevSel.length);
                    sel[sel.length - 1] = index;
                    Arrays.sort(sel);
                } else {
                    sel = new int[prevSel.length - 1];
                    int i = 0;
                    int j = 0;
                    while (i < prevSel.length) {
                        if (prevSel[i] != index) {
                            sel[j++] = prevSel[i];
                        }
                        i++;
                    }
                }
            }
        } else if (dragging) {

            int[] prevSel = horizontal ? heatmap.getColumns().getSelected() :
                    heatmap.getRows().getSelected();
            selStart = prevSel[0];
            selEnd = prevSel[prevSel.length - 1];

            int start = selStart <= selEnd ? selStart : selEnd;
            int end = selStart <= selEnd ? selEnd : selStart;

            int size = end - start + 1;

            sel = new int[size];
            for (int i = start; i <= end; i++)
                sel[i - start] = i;

        } else if (shiftDown) {
            selEnd = index;
            //selStart = horizontal ? panel.getColSelStart() : panel.getRowSelStart();
            int[] prevSel = horizontal ? heatmap.getColumns().getSelected() :
                    heatmap.getRows().getSelected();

            if (selEnd < selStart) {
                int temp = selEnd;
                selEnd = selStart;
                selStart = temp;
            }
            Set<Integer> newSet = new HashSet<Integer>();
            for (int i = 0; i < prevSel.length; i++) {
                newSet.add(prevSel[i]);
            }
            for (int i = selStart; i <= selEnd; i++) {
                newSet.add(i);
            }

            sel = Ints.toArray(newSet);

        }

        if (horizontal) {
            mv.getColumns().setSelected(sel);
        } else {
            mv.getRows().setSelected(sel);
        }

    }

    private int getIndexCount() {
        return horizontal ? heatmap.getColumns().size() : heatmap.getRows().size();
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

    private enum Mode {
        none, selecting, moving, movingSelected
    }
}
