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
import org.apache.commons.lang.ArrayUtils;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.drawer.HeatmapPosition;
import org.gitools.core.matrix.model.Direction;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.matrix.model.IMatrixViewDimension;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

public class HeatmapPanelInputProcessor {


    @NotNull
    private final List<HeatmapMouseListener> mouseListeners = new ArrayList<HeatmapMouseListener>();

    private IMatrixView mv;
    private Heatmap heatmap;
    private HeatmapPanel panel;

    public HeatmapPanelInputProcessor(HeatmapPanel heatmapPanel) {

        this.panel = heatmapPanel;
        this.mv = heatmapPanel.getHeatmap();
        this.heatmap = heatmapPanel.getHeatmap();

        HeatmapMouseListener mouseListenerProxy = new HeatmapMouseListener() {
            @Override
            public void mouseMoved(int row, int col, MouseEvent e) {
                for (HeatmapMouseListener l : mouseListeners)
                    l.mouseMoved(row, col, e);
            }

            @Override
            public void mouseClicked(int row, int col, MouseEvent e) {
                for (HeatmapMouseListener l : mouseListeners)
                    l.mouseClicked(row, col, e);
            }
        };


        HeatmapBodyMouseController bodyController = new HeatmapBodyMouseController(heatmapPanel, this);
        bodyController.addHeatmapMouseListener(mouseListenerProxy);


        HeatmapHeaderMouseController colMouseCtrl = new HeatmapHeaderMouseController(heatmapPanel, this, true);
        colMouseCtrl.addHeatmapMouseListener(mouseListenerProxy);


        HeatmapHeaderMouseController rowMouseCtrl = new HeatmapHeaderMouseController(heatmapPanel, this, false);
        rowMouseCtrl.addHeatmapMouseListener(mouseListenerProxy);


    }

    public void addHeatmapMouseListener(HeatmapMouseListener listener) {
        mouseListeners.add(listener);
    }

    private int lastSelectedRow;
    private int lastSelectedCol;


    //alphanumerics used as shortcuts
    private Map<Integer, Boolean> pressedAlphaNumerics = new HashMap<Integer, Boolean>();

    public void savePressedState(KeyEvent e) {
        pressedAlphaNumerics.put(e.getKeyCode(), true);
    }

    public void clearPressedStates(KeyEvent e) {
        pressedAlphaNumerics.clear();
    }

    public boolean isKeyPressed(int charCode) {
        if (pressedAlphaNumerics.containsKey(charCode)) {
            return pressedAlphaNumerics.get(charCode);
        } else {
            return false;
        }
    }

    public void shiftSelStart(IMatrixViewDimension dimension, int size) {
        if (dimension == mv.getColumns()) {
            this.setLastSelectedCol(this.lastSelectedCol + size);
        } else {
            this.setLastSelectedRow(this.lastSelectedRow + size);
        }
    }

    public void setLastSelectedRow(int lastSelectedRow) {
        if (lastSelectedRow > -1 && lastSelectedRow < mv.getColumns().size()) {
            this.lastSelectedRow = lastSelectedRow;
        } else if (lastSelectedRow >= mv.getRows().size()) {
            this.lastSelectedRow = mv.getRows().size() - 1;
        } else {
            this.lastSelectedRow = 0;
        }
    }

    public void scroll(int units, boolean horizontal) {

        if (horizontal) {
            HeatmapPosition pos = panel.getScrollPosition();
            panel.setScrollColumnPosition(pos.column + units);
        } else {
            HeatmapPosition pos = panel.getScrollPosition();
            panel.setScrollRowPosition(pos.row + units);
        }
    }


    public void zoomHeatmap(int units) {

        if (!isKeyPressed(KeyEvent.VK_R)) {
            int width = heatmap.getColumns().getCellSize() + units * -1;
            if (width < 1) {
                width = 1;
            }
            heatmap.getColumns().setCellSize(width);
        }

        if (!isKeyPressed(KeyEvent.VK_C)) {
            int height = heatmap.getRows().getCellSize() + units * -1;
            if (height < 1) {
                height = 1;
            }
            heatmap.getRows().setCellSize(height);
        }
    }

    public void setLastSelectedCol(int lastSelectedCol) {
        if (lastSelectedCol > -1 && lastSelectedCol < mv.getColumns().size()) {
            this.lastSelectedCol = lastSelectedCol;
        } else if (lastSelectedCol >= mv.getColumns().size()) {
            this.lastSelectedCol = mv.getColumns().size() - 1;
        } else {
            this.lastSelectedCol = 0;
        }
    }

    public int getLastSelectedRow() {
        return lastSelectedRow;
    }

    public int getLastSelectedCol() {
        return lastSelectedCol;
    }


    public void addToSelected(int toAdd, IMatrixViewDimension dim) {

        int[] prevSel = dim.getSelected();
        if (ArrayUtils.contains(prevSel, toAdd) ||
                dim.size() < toAdd + 1 ||
                toAdd < 0) {
            return;
        }

        int[] sel;
        sel = new int[prevSel.length + 1];
        System.arraycopy(prevSel, 0, sel, 0, prevSel.length);
        sel[sel.length - 1] = toAdd;
        Arrays.sort(sel);
        dim.setSelected(sel);
    }

    public void addToSelected(int start, int end, IMatrixViewDimension dim) {
        int[] prevSel = dim.getSelected();

        if (end < start) {
            int temp = end;
            end = start;
            start = temp;
        }

        if (start >= dim.size() || end < 0) {
            return;
        }
        if (start < 0) {
            start = 0;
        }
        if (end >= dim.size()) {
            end = dim.size() - 1;
        }

        Set<Integer> newSet = new HashSet<Integer>();
        for (int i = start; i <= end; i++) {
            if (!ArrayUtils.contains(prevSel, i)) {
                newSet.add(i);
            }
        }
        addToSelected(Ints.toArray(newSet), dim);
    }

    private void addToSelected(int[] toAdd, IMatrixViewDimension dim) {
        int[] prevSel = dim.getSelected();

        int[] sel;
        sel = new int[prevSel.length + toAdd.length];
        System.arraycopy(prevSel, 0, sel, 0, prevSel.length);
        System.arraycopy(toAdd, 0, sel, prevSel.length, toAdd.length);
        Arrays.sort(sel);
        dim.setSelected(sel);
    }

    public void removeFromSelected(int toRemove, IMatrixViewDimension dim) {
        int[] prevSel = dim.getSelected();
        int[] sel;
        int posInArray = ArrayUtils.indexOf(prevSel, toRemove);
        if (posInArray != -1) {
            sel = ArrayUtils.remove(prevSel, posInArray);
            dim.setSelected(sel);
        }
    }

    public void removeFromSelected(int[] toRemove, IMatrixViewDimension dim) {
        int[] prevSel = dim.getSelected();
        int[] sel = prevSel;
        for (int i = 0; i < toRemove.length; i++) {
            int posInArray = ArrayUtils.indexOf(sel, toRemove[i]);
            if (posInArray != -1) {
                sel = ArrayUtils.remove(sel, posInArray);
            }
        }
        dim.setSelected(sel);
    }

    public void setLeadRow(int i) {
        if (i >= getRowMax()) {
            mv.getRows().setSelectionLead(getRowMax());
        } else if (i < 0) {
            mv.getRows().setSelectionLead(0);
        } else {
            mv.getRows().setSelectionLead(i);
        }
    }

    public void setLeadColumn(int i) {
        if (i >= getColumnMax()) {
            mv.getColumns().setSelectionLead(getColumnMax());
        } else if (i < 0) {
            mv.getRows().setSelectionLead(0);
        } else {
            mv.getColumns().setSelectionLead(i);
        }
    }

    public void setLead(int row, int col) {
        setLeadRow(row);
        setLeadColumn(col);
    }

    public int getLeadRow() {
        return mv.getRows().getSelectionLead();
    }

    public int getLeadColumn() {
        return mv.getColumns().getSelectionLead();
    }

    void hideSelected(int[] rows, int[] cols) {

        if (rows.length > 0 && !isKeyPressed(KeyEvent.VK_C)) {
            mv.getRows().hide(rows);
        }

        if (cols.length > 0 && !isKeyPressed(KeyEvent.VK_R)) {
            mv.getColumns().hide(cols);
        }
    }

    void hideSelected() {
        int[] rows = mv.getRows().getSelected();
        int[] cols = mv.getColumns().getSelected();

        hideSelected(rows, cols);

    }

    public void moveSelection(@NotNull KeyEvent e) {
        int row = mv.getRows().getSelectionLead();
        int col = mv.getColumns().getSelectionLead();
        int shift = 0;
        Direction dir = null;
        IMatrixViewDimension dimension = null;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                if (row >= 0 && row < mv.getRows().size() - 1) {
                    dimension = mv.getRows();
                    dir = Direction.DOWN;
                    shift = 1;
                }
                break;
            case KeyEvent.VK_UP:
                if (row > 0 && row < mv.getRows().size()) {
                    dir = Direction.UP;
                    dimension = mv.getRows();
                    shift = -1;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (col >= 0 && col < mv.getColumns().size() - 1) {
                    dir = Direction.RIGHT;
                    shift = 1;
                    dimension = mv.getColumns();
                }
                break;
            case KeyEvent.VK_LEFT:
                if (col > 0 && col < mv.getColumns().size()) {
                    dir = Direction.LEFT;
                    dimension = mv.getColumns();
                    shift = -1;
                }
                break;
        }
        int[] sel = dimension.getSelected();
        if (sel.length > 0) {
            dimension.move(dir, sel);
            shiftSelStart(dimension, shift);
        }
    }

    int getRowMax() {
        return mv.getRows().size() - 1;
    }

    int getColumnMax() {
        return mv.getColumns().size() - 1;
    }

    public void setLastSelected(int index, boolean horizontal) {
        if (horizontal) {
            setLastSelectedCol(index);
        } else {
            setLastSelectedRow(index);
        }
    }

    enum Mode {
        none, selectingLead, selectingRowsAndCols, dragging, moving, zooming, scrolling, movingSelected
    }
}