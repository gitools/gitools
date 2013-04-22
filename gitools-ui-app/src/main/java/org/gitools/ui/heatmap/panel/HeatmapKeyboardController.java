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

import org.apache.commons.lang.ArrayUtils;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.IMatrixViewDimension;
import org.jetbrains.annotations.NotNull;

import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

class HeatmapKeyboardController extends KeyAdapter {

    private final HeatmapPanel panel;
    private final Heatmap hm;

    private int selStart;
    private int selEnd;
    private int selLast;

    HeatmapKeyboardController(@NotNull HeatmapPanel panel) {
        this.panel = panel;
        this.hm = panel.getHeatmap();

        selStart = selEnd = selLast = -1;

        //panel.addKeyListener(this);
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
            } else if (shiftDown && ctrlDown && !altDown) {
                moveLead(e);
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
                    switchLeadRowSelection(e);
                    break;

                case KeyEvent.VK_C:
                    switchLeadColSelection(e);
                    break;

                case KeyEvent.VK_B:
                    switchLeadColSelection(e);
                    switchLeadRowSelection(e);
                    break;
            }
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

        int prevRow = row;
        int prevCol = col;

        final int rowPageSize = 10; //FIXME should depend on screen size
        final int colPageSize = 10; //FIXME should depend on screen size

        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                if (row < mv.getRows().size() - 1) {
                    row++;
                }
                break;
            case KeyEvent.VK_UP:
                if (row > 0) {
                    row--;
                } else if (col != -1) {
                    row = -1;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (col >= 0 && col < mv.getColumns().size() - 1) {
                    col++;
                } else if (row != -1) {
                    col = -1;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (col > 0) {
                    col--;
                } else if (col == -1) {
                    col = mv.getColumns().size() - 1;
                }
                break;
            case KeyEvent.VK_PAGE_UP:
                if (row != -1) {
                    row -= rowPageSize;
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

        boolean clearSelection = false;

        boolean onRow = row != -1 && col == -1;
        boolean onBody = row != -1 && col != -1;

        int[] sel = null;

        if (onBody || (!shiftDown && !ctrlDown)) {
            selStart = selEnd = -1;
        } else if (!onBody && (shiftDown && !ctrlDown)) {
            selEnd = onRow ? row : col;
            if (selStart == -1) {
                selStart = selLast != -1 ? selLast : selEnd;
            }

            int start = selStart <= selEnd ? selStart : selEnd;
            int end = selStart <= selEnd ? selEnd : selStart;

            int size = end - start + 1;
            sel = new int[size];
            for (int i = start; i <= end; i++)
                sel[i - start] = i;

            clearSelection = false;
        } else if (!shiftDown && ctrlDown) {
            clearSelection = false;
        }

        selLast = !onBody ? (onRow ? row : col) : -1;

        mv.getColumns().setSelectionLead(col);
        mv.getRows().setSelectionLead(row);

        if (clearSelection) {
            mv.getRows().clearSelection();
            mv.getColumns().clearSelection();
        } else if (!onBody && sel != null) {
            if (onRow) {
                mv.getRows().setSelected(sel);
            } else {
                mv.getColumns().setSelected(sel);
            }
        }
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

    private void moveLead(@NotNull KeyEvent e) {
        IMatrixView mv = hm;
        int row = mv.getRows().getSelectionLead();
        int col = mv.getColumns().getSelectionLead();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                if (row >= 0 && row < mv.getRows().size() - 1) {
                    mv.getRows().move(org.gitools.matrix.model.Direction.DOWN, new int[]{row});
                }
                break;
            case KeyEvent.VK_UP:
                if (row > 0 && row < mv.getRows().size()) {
                    mv.getRows().move(org.gitools.matrix.model.Direction.UP, new int[]{row});
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (col >= 0 && col < mv.getColumns().size() - 1) {
                    mv.getColumns().move(org.gitools.matrix.model.Direction.RIGHT, new int[]{col});
                }
                break;
            case KeyEvent.VK_LEFT:
                if (col > 0 && col < mv.getColumns().size()) {
                    mv.getColumns().move(org.gitools.matrix.model.Direction.LEFT, new int[]{col});
                }
                break;
        }
    }

    private void moveSelection(@NotNull KeyEvent e) {
        IMatrixView mv = hm;
        int row = mv.getRows().getSelectionLead();
        int col = mv.getColumns().getSelectionLead();

        boolean horizontal = (row == -1 && col >= 0);

        int[] sel = horizontal ? mv.getColumns().getSelected() : mv.getRows().getSelected();
        if (sel.length == 0 && ((horizontal && col != -1) || (!horizontal && row != -1))) {
            sel = horizontal ? new int[]{col} : new int[]{row};
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                if (row >= 0 && row < mv.getRows().size() - 1) {
                    mv.getRows().move(org.gitools.matrix.model.Direction.DOWN, sel);
                }
                break;
            case KeyEvent.VK_UP:
                if (row > 0 && row < mv.getRows().size()) {
                    mv.getRows().move(org.gitools.matrix.model.Direction.UP, sel);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (col >= 0 && col < mv.getColumns().size() - 1) {
                    mv.getColumns().move(org.gitools.matrix.model.Direction.RIGHT, sel);
                }
                break;
            case KeyEvent.VK_LEFT:
                if (col > 0 && col < mv.getColumns().size()) {
                    mv.getColumns().move(org.gitools.matrix.model.Direction.LEFT, sel);
                }
                break;
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
        IMatrixView mv = hm;
        IMatrixViewDimension dim = mv.getRows();
        int leadIndex = dim.getSelectionLead();
        int[] prevSel = dim.getSelected();

        siwtchLeadSelection(dim, leadIndex, prevSel);

    }

    private void switchLeadColSelection(@NotNull KeyEvent e) {
        IMatrixView mv = hm;
        IMatrixViewDimension dim = mv.getColumns();
        int leadIndex = dim.getSelectionLead();
        int[] prevSel = dim.getSelected();

        siwtchLeadSelection(dim, leadIndex, prevSel);
    }

    private void siwtchLeadSelection(IMatrixViewDimension dim, int leadIndex, int[] prevSel) {
        if (leadIndex == -1) {
            return;
        }

        int[] sel;


        if (ArrayUtils.contains(prevSel, leadIndex)) {
            int posInArray = ArrayUtils.indexOf(prevSel, leadIndex);
            sel = ArrayUtils.remove(prevSel, posInArray);
        } else {
            sel = new int[prevSel.length + 1];
            System.arraycopy(prevSel, 0, sel, 0, prevSel.length);
            sel[sel.length - 1] = leadIndex;
            Arrays.sort(sel);
        }
        dim.setSelected(sel);
    }
}
