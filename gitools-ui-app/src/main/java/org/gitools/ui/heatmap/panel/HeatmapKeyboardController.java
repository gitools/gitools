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
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.matrix.model.IMatrixViewDimension;
import org.gitools.ui.platform.AppFrame;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class HeatmapKeyboardController extends KeyAdapter {

    private final IMatrixView mv;
    private int ctrlMask = AppFrame.getOsProperties().getCtrlMask();
    private int shiftMask = AppFrame.getOsProperties().getShiftMask();
    private int altMask = AppFrame.getOsProperties().getAltMask();
    private int metaMask = AppFrame.getOsProperties().getMetaMask();
    final HeatmapPanelInputProcessor ip;


    HeatmapKeyboardController(@NotNull IMatrixView matrixView, HeatmapPanelInputProcessor inputProcessor) {
        this.mv = matrixView;
        this.ip = inputProcessor;
    }


    @Override
    public void keyTyped(@NotNull KeyEvent e) {
        return;
    }

    @Override
    public void keyReleased(@NotNull KeyEvent e) {
        ip.clearPressedStates(e);
    }

    @Override
    public void keyPressed(@NotNull KeyEvent e) {

        IMatrixView mv = this.mv;

        int row = mv.getRows().getSelectionLead();
        int col = mv.getColumns().getSelectionLead();

        int key = e.getKeyCode();

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & shiftMask) != 0);
        boolean ctrlDown = ((modifiers & ctrlMask) != 0);
        boolean altDown = ((modifiers & altMask) != 0);
        if (ctrlDown) {
            e.consume();

        }

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP || key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_HOME || key == KeyEvent.VK_END || key == KeyEvent.VK_PAGE_UP || key == KeyEvent.VK_PAGE_DOWN) {

            if (((!shiftDown && !ctrlDown) || (shiftDown && !ctrlDown) || (!shiftDown && ctrlDown)) && !altDown) {

                moveLead(e);

            } else if (!shiftDown && !ctrlDown && altDown) {

                ip.moveSelection(e);

            }


        } else {
            switch (key) {
                case KeyEvent.VK_DELETE:
                    ip.hideSelected();
                    break;

                case KeyEvent.VK_BACK_SPACE:
                    ip.hideSelected();
                    break;

                case KeyEvent.VK_R:
                    ip.savePressedState(e);
                    switchLeadRowSelection(e);
                    break;

                case KeyEvent.VK_C:
                    ip.savePressedState(e);
                    switchLeadColSelection(e);
                    break;

                case KeyEvent.VK_B:
                    ip.savePressedState(e);
                    switchLeadColSelection(e);
                    switchLeadRowSelection(e);
                    break;
            }
        }
    }

    private void moveLead(KeyEvent e) {

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & shiftMask) != 0);

        IMatrixView mv = this.mv;
        int row = mv.getRows().getSelectionLead();
        int col = mv.getColumns().getSelectionLead();

        final int rowPageSize = 10; //TODO should depend on screen size
        final int colPageSize = 10; //TODO should depend on screen size

        boolean selectingRows = ip.isKeyPressed(KeyEvent.VK_R) | ip.isKeyPressed(KeyEvent.VK_B);
        boolean selectingColumns = ip.isKeyPressed(KeyEvent.VK_C) | ip.isKeyPressed(KeyEvent.VK_B);

        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                ip.setLeadRow(++row);
                if (selectingRows) {
                    ip.addToSelected(row, mv.getRows());
                    ip.setLastSelectedRow(row);
                }
                break;
            case KeyEvent.VK_UP:
                ip.setLeadRow(--row);
                if (selectingRows) {
                    ip.addToSelected(row, mv.getRows());
                    ip.setLastSelectedRow(row);
                }
                break;
            case KeyEvent.VK_RIGHT:
                ip.setLeadColumn(++col);
                if (selectingColumns) {
                    ip.addToSelected(col, mv.getColumns());
                    ip.setLastSelectedCol(col);
                }
                break;
            case KeyEvent.VK_LEFT:
                ip.setLeadColumn(--col);
                if (selectingColumns) {
                    ip.addToSelected(col, mv.getColumns());
                    ip.setLastSelectedCol(col);
                }
                break;
            case KeyEvent.VK_PAGE_UP:
                if (shiftDown) {
                    col -= colPageSize;
                    ip.setLeadColumn(++col);
                    if (selectingColumns) {
                        ip.addToSelected(col, mv.getColumns());
                        ip.setLastSelectedCol(col);
                    }
                } else {
                    row -= rowPageSize;
                    ip.setLeadRow(row);
                    if (selectingRows) {
                        ip.addToSelected(row, mv.getRows());
                        ip.setLastSelectedRow(row);
                    }
                }
                break;
            case KeyEvent.VK_PAGE_DOWN:
                if (shiftDown) {
                    col += colPageSize;
                    ip.setLeadColumn(++col);
                    if (selectingColumns) {
                        ip.addToSelected(col, mv.getColumns());
                        ip.setLastSelectedCol(col);
                    }
                } else {
                    row += rowPageSize;
                    ip.setLeadRow(row);
                    if (selectingRows) {
                        ip.addToSelected(row, mv.getRows());
                        ip.setLastSelectedRow(row);
                    }
                }
                break;
            case KeyEvent.VK_HOME:
                if (shiftDown) {
                    col = 0;
                    ip.setLeadColumn(++col);
                    if (selectingColumns) {
                        ip.addToSelected(col, mv.getColumns());
                        ip.setLastSelectedCol(col);
                    }
                } else {
                    row = 0;
                    ip.setLeadRow(row);
                    if (selectingRows) {
                        ip.addToSelected(row, mv.getRows());
                        ip.setLastSelectedRow(row);
                    }
                }
                break;
            case KeyEvent.VK_END:
                if (shiftDown) {
                    col = ip.getColumnMax();
                    ip.setLeadColumn(++col);
                    if (selectingColumns) {
                        ip.addToSelected(col, mv.getColumns());
                        ip.setLastSelectedCol(col);
                    }
                } else {
                    row = ip.getRowMax();
                    ip.setLeadRow(row);
                    if (selectingRows) {
                        ip.addToSelected(row, mv.getRows());
                        ip.setLastSelectedRow(row);
                    }
                }
        }
    }

    private void switchLeadRowSelection(@NotNull KeyEvent e) {

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & shiftMask) != 0);
        boolean ctrlDown = ((modifiers & ctrlMask) != 0);
        if (ctrlDown) {
            return;
        }

        IMatrixView mv = this.mv;
        IMatrixViewDimension dim = mv.getRows();
        int leadIndex = dim.getSelectionLead();
        int[] prevSel = dim.getSelected();

        if (leadIndex == -1) {
            return;
        }

        if (ArrayUtils.contains(prevSel, leadIndex)) {
            ip.removeFromSelected(leadIndex, dim);
        } else {
            if (shiftDown) {
                ip.addToSelected(ip.getLastSelectedRow(), leadIndex, dim);
            } else {
                ip.addToSelected(leadIndex, dim);
            }
            ip.setLastSelectedRow(leadIndex);
        }
    }

    private void switchLeadColSelection(@NotNull KeyEvent e) {

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & shiftMask) != 0);
        boolean ctrlDown = ((modifiers & ctrlMask) != 0);
        if (ctrlDown) {
            return;
        }

        IMatrixView mv = this.mv;
        IMatrixViewDimension dim = mv.getColumns();
        int leadIndex = dim.getSelectionLead();
        int[] prevSel = dim.getSelected();
        if (leadIndex == -1) {
            return;
        }

        int[] sel;
        if (ArrayUtils.contains(prevSel, leadIndex)) {
            ip.removeFromSelected(leadIndex, dim);
        } else {
            if (shiftDown) {
                //select all columns in between
                ip.addToSelected(ip.getLastSelectedCol(), leadIndex, dim);
            } else {
                ip.addToSelected(leadIndex, dim);
            }
            ip.setLastSelectedCol(leadIndex);
        }
    }


}
