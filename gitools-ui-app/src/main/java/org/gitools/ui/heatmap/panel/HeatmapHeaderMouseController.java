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

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.drawer.HeatmapPosition;
import org.gitools.matrix.model.IMatrixView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeatmapHeaderMouseController implements MouseListener, MouseMotionListener, MouseWheelListener
{

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
    private int selectionMoveLastIndex;

    public HeatmapHeaderMouseController(HeatmapPanel panel, boolean horizontal)
    {
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

    public void addHeatmapMouseListener(HeatmapMouseListener listener)
    {
        listeners.add(listener);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        panel.requestFocusInWindow();

        int index = convertToIndex(e);
        if (!isValidIndex(index))
        {
            return;
        }

        int row = horizontal ? -1 : index;
        int col = horizontal ? index : -1;

        for (HeatmapMouseListener l : listeners)
            l.mouseClicked(row, col, e);
    }

    private int convertToIndex(MouseEvent e)
    {
        point = e.getPoint();
        Point viewPosition = viewPort.getViewPosition();
        point.translate(viewPosition.x, viewPosition.y);
        position = headerPanel.getDrawer().getPosition(point);

        return horizontal ? position.column : position.row;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

        // check if it's a already selected
        int index = convertToIndex(e);

        if (isSelectedIndex(index))
        {
            mode = Mode.movingSelected;
        }
        else
        {
            int modifiers = e.getModifiers();
            boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
            boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

            mode = shiftDown && ctrlDown ? Mode.moving : Mode.selecting;
        }


        switch (mode)
        {
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

    private boolean isSelectedIndex(int index)
    {
        if (!isValidIndex(index))
        {
            return false;
        }

        IMatrixView matrixView = heatmap.getMatrixView();
        if (horizontal)
        {
            return matrixView.isColumnSelected(index);
        }

        return matrixView.isRowSelected(index);

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        panel.mouseReleased(e);

        setLeading(e);
        mode = Mode.none;
    }

    private void setLeading(MouseEvent e)
    {
        int index = convertToIndex(e);
        if (isValidIndex(index))
        {
            IMatrixView matrixView = heatmap.getMatrixView();
            if (horizontal)
            {
                matrixView.setLeadSelection(matrixView.getLeadSelectionRow(), index);
            }
            else
            {
                matrixView.setLeadSelection(index, matrixView.getLeadSelectionColumn());
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        switch (mode)
        {
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
    public void mouseMoved(MouseEvent e)
    {
        int index = convertToIndex(e);
        if (!isValidIndex(index))
        {
            return;
        }

        int row = horizontal ? -1 : index;
        int col = horizontal ? index : -1;

        for (HeatmapMouseListener l : listeners)
            l.mouseMoved(row, col, e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        int rotation = e.getWheelRotation();

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
        boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

        if (!shiftDown && !ctrlDown)
        {
            HeatmapPosition pos = panel.getScrollPosition();

            if (horizontal)
            {
                panel.setScrollColumnPosition(pos.column + rotation);
            }
            else
            {
                panel.setScrollRowPosition(pos.row + rotation);
            }
        }
        else
        {
            int width = heatmap.getCellWidth() + rotation * -1;
            if (width < 1)
            {
                width = 1;
            }

            int height = heatmap.getCellHeight() + rotation * -1;
            if (height < 1)
            {
                height = 1;
            }

            heatmap.setCellWidth(width);
            heatmap.setCellHeight(height);
        }
    }

    private void updateSelectionMove(MouseEvent e, boolean dragging)
    {
        int index = convertToIndex(e);

        if (!isValidIndex(index))
        {
            return;
        }

        setLeading(e);

        if (!dragging)
        {
            selectionMoveLastIndex = index;
        }
        else
        {

            int indexDiff = selectionMoveLastIndex - index;
            selectionMoveLastIndex = index;

            IMatrixView matrixView = heatmap.getMatrixView();
            if (indexDiff > 0)
            {
                if (horizontal)
                {
                    for (int i = 0; i < indexDiff; i++)
                    {
                        matrixView.moveColumnsLeft(matrixView.getSelectedColumns());
                    }
                }
                else
                {
                    for (int i = 0; i < indexDiff; i++)
                    {
                        matrixView.moveRowsUp(matrixView.getSelectedRows());
                    }
                }
            }

            if (indexDiff < 0)
            {
                if (horizontal)
                {
                    for (int i = 0; i > indexDiff; i--)
                    {
                        matrixView.moveColumnsRight(matrixView.getSelectedColumns());
                    }
                }
                else
                {
                    for (int i = 0; i > indexDiff; i--)
                    {
                        matrixView.moveRowsDown(matrixView.getSelectedRows());
                    }
                }
            }
        }
    }

    private void updateSelection(MouseEvent e, boolean dragging)
    {
        int index = convertToIndex(e);

        if (!isValidIndex(index))
        {
            return;
        }

        boolean indexChanged = (selLast != index);
        if (indexChanged) {
            setLeading(e);
        }
        selLast = index;

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
        boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

        IMatrixView mv = heatmap.getMatrixView();

        int[] sel = null;

        if (!dragging && !shiftDown && !ctrlDown)
        {
            selStart = selEnd = index;
            sel = new int[]{index};
        }
        else if (ctrlDown)
        {
            boolean selected = horizontal ?
                    mv.isColumnSelected(index) : mv.isRowSelected(index);

            int[] prevSel = horizontal ?
                    mv.getSelectedColumns() : mv.getSelectedRows();

            if (dragging && !indexChanged)
            {
                sel = prevSel;
            }
            else
            {
                if (!selected)
                {
                    sel = new int[prevSel.length + 1];
                    System.arraycopy(prevSel, 0, sel, 0, prevSel.length);
                    sel[sel.length - 1] = index;
                    Arrays.sort(sel);
                }
                else
                {
                    sel = new int[prevSel.length - 1];
                    int i = 0;
                    int j = 0;
                    while (i < prevSel.length)
                    {
                        if (prevSel[i] != index)
                        {
                            sel[j++] = prevSel[i];
                        }
                        i++;
                    }
                }
            }
        }
        else if (dragging || shiftDown)
        {
            selEnd = index;

            int start = selStart <= selEnd ? selStart : selEnd;
            int end = selStart <= selEnd ? selEnd : selStart;

            int size = end - start + 1;
            sel = new int[size];
            for (int i = start; i <= end; i++)
                sel[i - start] = i;
        }
        else
        {
            sel = new int[0];
        }

        if (horizontal)
        {
            if (mv.getSelectedRows().length != 0)
            {
                mv.setSelectedRows(new int[0]);
            }
            mv.setSelectedColumns(sel);
        }
        else
        {
            if (mv.getSelectedColumns().length != 0)
            {
                mv.setSelectedColumns(new int[0]);
            }
            mv.setSelectedRows(sel);
        }

    }

    private int getIndexCount()
    {
        return horizontal ? heatmap.getMatrixView().getColumnCount()
                : heatmap.getMatrixView().getRowCount();
    }

    private boolean isValidIndex(int index)
    {
        if (index < 0 || index >= getIndexCount())
        {
            return false;
        }

        return true;
    }

    private void updateScroll(MouseEvent e, boolean dragging)
    {
        point = e.getPoint();

        if (!dragging)
        {
            startPoint = point;
            startScrollValue = panel.getScrollValue();
        }
        else
        {
            int widthOffset = point.x - startPoint.x;
            int heightOffset = point.y - startPoint.y;

            if (horizontal)
            {
                panel.setScrollColumnValue(startScrollValue.x - widthOffset);
            }
            else
            {
                panel.setScrollRowValue(startScrollValue.y - heightOffset);
            }
        }
    }

    private enum Mode
    {
        none, selecting, moving, movingSelected
    }
}
