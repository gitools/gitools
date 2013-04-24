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
import org.gitools.core.matrix.model.Direction;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.matrix.model.IMatrixViewDimension;
import org.jetbrains.annotations.NotNull;

import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

class HeatmapKeyboardController extends KeyAdapter {

    private final HeatmapPanel panel;
    private final Heatmap hm;

    private int rowSelStart;
    private int colSelStart;


    //alphanumerics used as shortcuts
    private static Map<Integer, Boolean> pressedAlphaNumerics = new HashMap<Integer, Boolean>();

    HeatmapKeyboardController(@NotNull HeatmapPanel panel) {
        this.panel = panel;
        this.hm = panel.getHeatmap();
        initAlphaNumerics();
    }

    private void initAlphaNumerics() {
        pressedAlphaNumerics.put(KeyEvent.VK_B,false);
        pressedAlphaNumerics.put(KeyEvent.VK_C,false);
        pressedAlphaNumerics.put(KeyEvent.VK_R,false);
        pressedAlphaNumerics.put(KeyEvent.VK_S,false);
        pressedAlphaNumerics.put(KeyEvent.VK_U,false);
    }

    @Override
    public void keyTyped(@NotNull KeyEvent e){
        return;
    }

    @Override
    public void keyReleased(@NotNull KeyEvent e){

        if (pressedAlphaNumerics.containsKey(e.getKeyCode())) {
            saveReleasedState(e);
        }
    }

    @Override
    public void keyPressed(@NotNull KeyEvent e) {

        IMatrixView mv = hm;

        int row = mv.getRows().getSelectionLead();
        int col = mv.getColumns().getSelectionLead();

        int key = e.getKeyCode();

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
        boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);
        boolean altDown = ((modifiers & InputEvent.ALT_MASK) != 0);

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP || key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_HOME || key == KeyEvent.VK_END || key == KeyEvent.VK_PAGE_UP || key == KeyEvent.VK_PAGE_DOWN) {

            if (((!shiftDown && !ctrlDown) || (shiftDown && !ctrlDown) || (!shiftDown && ctrlDown)) && !altDown) {
                changeLead(e);
            } else if (!shiftDown && !ctrlDown && altDown) {
                moveSelection(e);
            }

            if (ctrlDown) {
                e.consume();
            }
        } else {
            switch (key) {
                case KeyEvent.VK_DELETE:
                    hideSelection(e);
                    break;

                case KeyEvent.VK_BACK_SPACE:
                    hideSelection(e);
                    break;

                case KeyEvent.VK_SPACE:
                    selectLead(e);
                    break;

                case KeyEvent.VK_R:
                    savePressedState(e);
                    switchLeadRowSelection(e);
                    break;

                case KeyEvent.VK_C:
                    savePressedState(e);
                    switchLeadColSelection(e);
                    break;

                case KeyEvent.VK_B:
                    savePressedState(e);
                    switchLeadColSelection(e);
                    switchLeadRowSelection(e);
                    break;
            }
        }
    }

    private void savePressedState(KeyEvent e) {
        pressedAlphaNumerics.put(e.getKeyCode(), true);
    }

    private void saveReleasedState(KeyEvent e) {
        pressedAlphaNumerics.put(e.getKeyCode(),false);
    }

    private boolean keyInPressedState(int charCode) {
        if (pressedAlphaNumerics.containsKey(charCode)) {
            return pressedAlphaNumerics.get(charCode);
        } else {
            return false;
        }
    }

    //TODO shift selection
    private void changeLead(@NotNull KeyEvent e) {

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
        boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

        IMatrixView mv = hm;
        int row = mv.getRows().getSelectionLead();
        int col = mv.getColumns().getSelectionLead();

        final int rowPageSize = 10; //FIXME should depend on screen size
        final int colPageSize = 10; //FIXME should depend on screen size
        boolean selectingRows = keyInPressedState(KeyEvent.VK_R) | keyInPressedState(KeyEvent.VK_B);
        int[] selectedRows = mv.getRows().getSelected();
        boolean selectingColumns = keyInPressedState(KeyEvent.VK_C) | keyInPressedState(KeyEvent.VK_B) ;
        int[] selectedColumns = mv.getColumns().getSelected();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                if (row < mv.getRows().size() - 1) {
                    row++;
                    if (selectingRows) {
                        selectedRows = addToSelected(selectedRows, row);
                    }
                }
                break;
            case KeyEvent.VK_UP:
                if (row > 0) {
                    row--;
                    if (selectingRows) {
                        selectedRows = addToSelected(selectedRows,row);
                    }
                } else if (col != -1) {
                    row = -1;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (col >= 0 && col < mv.getColumns().size() - 1) {
                    col++;
                    if(selectingColumns){
                        selectedColumns = addToSelected(selectedColumns,col);
                    }
                } else if (row != -1) {
                    col = -1;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (col > 0) {
                    col--;
                    if(selectingColumns){
                        selectedColumns = addToSelected(selectedColumns,col);
                    }
                } else if (col == -1) {
                    col = mv.getColumns().size() - 1;
                }
                break;
            case KeyEvent.VK_PAGE_UP:
                if (row != -1) {
                    row -= rowPageSize;
                    if (selectingRows) {
                        selectedRows = addToSelected(selectedRows,row);
                    }
                    if (row < 0) {
                        row = 0;
                    }
                } else if (row == -1 && col != -1) {
                    col -= colPageSize;
                    if (col < 0) {
                        col = 0;
                    }
                }
                break;
            case KeyEvent.VK_PAGE_DOWN:
                if (row != -1) {
                    row += rowPageSize;
                    if (row >= mv.getRows().size()) {
                        row = mv.getRows().size() - 1;
                    }
                } else if (row == -1 && col != -1) {
                    col += colPageSize;
                    if (col >= mv.getColumns().size()) {
                        col = mv.getColumns().size() - 1;
                    }
                }
                break;
            case KeyEvent.VK_HOME:
                if (row != -1) {
                    row = 0;
                } else if (row == -1 && col != -1) {
                    col = 0;
                }
                break;
            case KeyEvent.VK_END:
                if (row != -1) {
                    row = mv.getRows().size() - 1;
                } else if (row == -1 && col != -1) {
                    col = mv.getColumns().size() - 1;
                }
                break;
        }

        // update selection


        if (selectingRows) {
            mv.getRows().setSelected(selectedRows);
        }
        if (selectingColumns) {
            mv.getColumns().setSelected(selectedColumns);
        }
        mv.getColumns().setSelectionLead(col);
        mv.getRows().setSelectionLead(row);

    }

    private void hideSelection(KeyEvent e) {
        IMatrixView mv = hm;
        int[] rows = mv.getRows().getSelected();
        int[] cols = mv.getColumns().getSelected();

        if (rows.length > 0) {
            mv.getRows().hide(rows);
        }

        if (cols.length > 0) {
            mv.getColumns().hide(cols);
        }
    }

    @Deprecated /*Only selected items should be moved*/
    private void moveLead(@NotNull KeyEvent e) {

        IMatrixView mv = hm;
        int row = mv.getRows().getSelectionLead();
        int col = mv.getColumns().getSelectionLead();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                if (row >= 0 && row < mv.getRows().size() - 1) {
                    mv.getRows().move(Direction.DOWN, new int[]{row});
                }
                break;
            case KeyEvent.VK_UP:
                if (row > 0 && row < mv.getRows().size()) {
                    mv.getRows().move(Direction.UP, new int[]{row});
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (col >= 0 && col < mv.getColumns().size() - 1) {
                    mv.getColumns().move(Direction.RIGHT, new int[]{col});
                }
                break;
            case KeyEvent.VK_LEFT:
                if (col > 0 && col < mv.getColumns().size()) {
                    mv.getColumns().move(Direction.LEFT, new int[]{col});
                }
                break;
        }
    }

    private void moveSelection(@NotNull KeyEvent e) {
        IMatrixView mv = hm;
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
            shiftSelStart(dimension,shift);
        }
    }

    private void selectLead(@NotNull KeyEvent e) {
        IMatrixView mv = hm;
        int row = mv.getRows().getSelectionLead();
        int col = mv.getColumns().getSelectionLead();

        if (row != -1 && col != -1) {
            return;
        }

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
        boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);

        boolean horizontal = (row == -1 && col >= 0);

        int index = horizontal ? col : row;

        int[] sel = null;

        if (!ctrlDown) {
            if (row >= 0 && col == -1) {
                sel = new int[]{row};
            } else if (row == -1 && col >= 0) {
                sel = new int[]{col};
            }
        } else {
            boolean selected = horizontal ? mv.getColumns().isSelected(col) : mv.getRows().isSelected(row);

            int[] prevSel = horizontal ? mv.getColumns().getSelected() : mv.getRows().getSelected();

            if (!selected) {
                sel = addToSelected(prevSel,index);
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

        if (horizontal) {
            if (mv.getRows().getSelected().length != 0) {
                mv.getRows().setSelected(new int[0]);
            }
            mv.getColumns().setSelected(sel);
        } else {
            if (mv.getColumns().getSelected().length != 0) {
                mv.getColumns().setSelected(new int[0]);
            }
            mv.getRows().setSelected(sel);
        }
    }

    private void switchLeadRowSelection(@NotNull KeyEvent e) {

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
        boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);
        if (ctrlDown) {
            return;
        }

        IMatrixView mv = hm;
        IMatrixViewDimension dim = mv.getRows();
        int leadIndex = dim.getSelectionLead();
        int[] prevSel = dim.getSelected();

        if (leadIndex == -1) {
            return;
        }

        int[] sel;
        if (ArrayUtils.contains(prevSel, leadIndex)) {
            sel = removeFromSelected(prevSel, leadIndex);
        } else {
            sel = addToSelected(prevSel, leadIndex);
            if (shiftDown) {
                sel = addToSelected(prevSel,rowSelStart,leadIndex);
            } else {
                setRowSelStart(leadIndex);
            }
        }
        dim.setSelected(sel);
    }

    private void switchLeadColSelection(@NotNull KeyEvent e) {

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & InputEvent.SHIFT_MASK) != 0);
        boolean ctrlDown = ((modifiers & InputEvent.CTRL_MASK) != 0);
        if (ctrlDown) {
            return;
        }

        IMatrixView mv = hm;
        IMatrixViewDimension dim = mv.getColumns();
        int leadIndex = dim.getSelectionLead();
        int[] prevSel = dim.getSelected();
        if (leadIndex == -1) {
            return;
        }

        int[] sel;
        if (ArrayUtils.contains(prevSel, leadIndex)) {
            sel = removeFromSelected(prevSel, leadIndex);
        } else {
            sel = addToSelected(prevSel, leadIndex);
            if (shiftDown) {
                //select all columns in between
                sel = addToSelected(prevSel,colSelStart,leadIndex);
            } else {
                setColSelStart(leadIndex);
            }
        }
        dim.setSelected(sel);
    }



    private int[] addToSelected(int[] prevSel, int toAdd) {

        if (ArrayUtils.contains(prevSel,toAdd)) {
            return prevSel;
        }

        int[] sel;
        sel = new int[prevSel.length + 1];
        System.arraycopy(prevSel, 0, sel, 0, prevSel.length);
        sel[sel.length - 1] = toAdd;
        Arrays.sort(sel);
        return sel;
    }

    private int[] addToSelected(int[] prevSel, int start, int end) {
        if (start < 0 || end < 0) {
            return prevSel;
        }

        if (end < start) {
            int temp = end;
            end = start;
            start = temp;
        }
        Set<Integer> newSet = new HashSet<Integer>();
        for (int i = start; i <= end; i++){
            if (!ArrayUtils.contains(prevSel,i)){
                newSet.add(i);
            }
        }
        return addToSelected(prevSel,Ints.toArray(newSet));
    }

    private int[] addToSelected(int[] prevSel, int[] toAdd) {
        int emptyFields = 0;
        int[] sel;
        sel = new int[prevSel.length + toAdd.length];
        System.arraycopy(prevSel, 0, sel, 0, prevSel.length);
        System.arraycopy(toAdd,0,sel,prevSel.length,toAdd.length);
        Arrays.sort(sel);
        return sel;
    }

    private int[] removeFromSelected(int[] prevSel, int toRemove) {
        int[] sel;
        int posInArray = ArrayUtils.indexOf(prevSel, toRemove);
        sel = ArrayUtils.remove(prevSel, posInArray);
        return sel;
    }

    private int[] removeFromSelected(int[] prevSel, int[] toRemove) {
        int[] sel = prevSel;
        for (int i = 0; i< toRemove.length; i++){
            int posInArray = ArrayUtils.indexOf(sel, toRemove[i]);
            sel = ArrayUtils.remove(sel,posInArray);
        }
        return sel;
    }

    public void shiftSelStart(IMatrixViewDimension dimension, int size) {
        if (dimension == hm.getColumns()) {
            setColSelStart(colSelStart + size);
        } else {
            setRowSelStart(rowSelStart + size);
        }
    }


    public void setRowSelStart(int rowSelStart) {
        if (colSelStart > -1 && colSelStart < hm.getColumns().size())  {
            this.rowSelStart = rowSelStart;
        } else if (rowSelStart >= hm.getRows().size()) {
            this.rowSelStart = hm.getRows().size()-1;
        } else {
            this.rowSelStart = 0;
        }
    }

    public void setColSelStart(int colSelStart) {
        if (colSelStart > -1 && colSelStart < hm.getColumns().size()) {
            this.colSelStart = colSelStart;
        } else if (colSelStart >= hm.getColumns().size()) {
            this.colSelStart = hm.getColumns().size()-1;
        } else {
            this.colSelStart = 0;
        }
    }
}
