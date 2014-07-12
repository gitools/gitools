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
import org.gitools.api.matrix.view.IMatrixViewDimension;
import org.gitools.ui.platform.os.OSProperties;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class HeatmapKeyboardController extends KeyAdapter {

    private final IMatrixView mv;
    private int ctrlMask = OSProperties.get().getCtrlMask();
    private int shiftMask = OSProperties.get().getShiftMask();
    private int altMask = OSProperties.get().getAltMask();
    private int metaMask = OSProperties.get().getMetaMask();
    final HeatmapPanelInputProcessor ip;


    HeatmapKeyboardController(IMatrixView matrixView, HeatmapPanelInputProcessor inputProcessor) {
        this.mv = matrixView;
        this.ip = inputProcessor;
    }


    @Override
    public void keyTyped(KeyEvent e) {
        return;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        ip.clearPressedStates(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {

        IMatrixView mv = this.mv;

        int row = mv.getRows().indexOf(mv.getRows().getFocus());
        int col = mv.getColumns().indexOf(mv.getColumns().getFocus());

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
                    if (ip.isKeyPressed(KeyEvent.VK_U)) {
                        unselectRows();
                    } else if (shiftDown) {
                        ip.selectRange(mv.getColumns().indexOf(mv.getColumns().getFocus()), false);
                    } else {
                        switchLeadRowSelection(e);
                    }
                    break;

                case KeyEvent.VK_C:
                    ip.savePressedState(e);
                    if (ip.isKeyPressed(KeyEvent.VK_U)) {
                        unselectColumns();
                    } else if (shiftDown) {
                        ip.selectRange(mv.getColumns().indexOf(mv.getColumns().getFocus()), true);
                    } else {
                        switchLeadColSelection(e);
                    }
                    break;

                case KeyEvent.VK_B:
                    ip.savePressedState(e);
                    if (ip.isKeyPressed(KeyEvent.VK_U)) {
                        unselectColumns();
                        unselectRows();
                    } else if (shiftDown) {
                        ip.selectRange(mv.getColumns().indexOf(mv.getColumns().getFocus()), true);
                        ip.selectRange(mv.getColumns().indexOf(mv.getRows().getFocus()), false);
                    } else {
                        switchLeadColSelection(e);
                        switchLeadRowSelection(e);
                    }
                    break;

                case KeyEvent.VK_U:
                    ip.savePressedState(e);
                    break;

                case KeyEvent.VK_M:
                    ip.savePressedState(e);
                    break;

                case KeyEvent.VK_A:
                    ip.savePressedState(e);
                    if (ip.isKeyPressed(KeyEvent.VK_U)) {
                        unselectColumns();
                        unselectRows();
                    }
                    break;
            }
        }
    }

    private void unselectRows() {
        mv.getRows().getSelected().clear();
    }

    private void unselectColumns() {
        mv.getColumns().getSelected().clear();
    }

    private void moveLead(KeyEvent e) {

        int modifiers = e.getModifiers();
        boolean shiftDown = ((modifiers & shiftMask) != 0);

        IMatrixView mv = this.mv;
        int row = mv.getRows().indexOf(mv.getRows().getFocus());
        int col = mv.getColumns().indexOf(mv.getColumns().getFocus());

        final int rowPageSize = 10; //TODO should depend on screen size
        final int colPageSize = 10; //TODO should depend on screen size

        boolean selectingRows = ip.isKeyPressed(KeyEvent.VK_R) | ip.isKeyPressed(KeyEvent.VK_B);
        boolean selectingColumns = ip.isKeyPressed(KeyEvent.VK_C) | ip.isKeyPressed(KeyEvent.VK_B);

        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                ip.setLead(++row, mv.getRows());
                if (selectingRows) {
                    ip.addToSelected(row, mv.getRows());
                    ip.setLastSelectedRow(row);
                }
                break;
            case KeyEvent.VK_UP:
                ip.setLead(--row, mv.getRows());
                if (selectingRows) {
                    ip.addToSelected(row, mv.getRows());
                    ip.setLastSelectedRow(row);
                }
                break;
            case KeyEvent.VK_RIGHT:
                ip.setLead(++col, mv.getColumns());
                if (selectingColumns) {
                    ip.addToSelected(col, mv.getColumns());
                    ip.setLastSelectedCol(col);
                }
                break;
            case KeyEvent.VK_LEFT:
                ip.setLead(--col, mv.getColumns());
                if (selectingColumns) {
                    ip.addToSelected(col, mv.getColumns());
                    ip.setLastSelectedCol(col);
                }
                break;
            case KeyEvent.VK_PAGE_UP:
                if (shiftDown) {
                    col -= colPageSize;
                    ip.setLead(++col, mv.getColumns());
                    if (selectingColumns) {
                        ip.addToSelected(col, mv.getColumns());
                        ip.setLastSelectedCol(col);
                    }
                } else {
                    row -= rowPageSize;
                    ip.setLead(row, mv.getRows());
                    if (selectingRows) {
                        ip.addToSelected(row, mv.getRows());
                        ip.setLastSelectedRow(row);
                    }
                }
                break;
            case KeyEvent.VK_PAGE_DOWN:
                if (shiftDown) {
                    col += colPageSize;
                    ip.setLead(++col, mv.getColumns());
                    if (selectingColumns) {
                        ip.addToSelected(col, mv.getColumns());
                        ip.setLastSelectedCol(col);
                    }
                } else {
                    row += rowPageSize;
                    ip.setLead(row, mv.getRows());
                    if (selectingRows) {
                        ip.addToSelected(row, mv.getRows());
                        ip.setLastSelectedRow(row);
                    }
                }
                break;
            case KeyEvent.VK_HOME:
                if (shiftDown) {
                    col = 0;
                    ip.setLead(col, mv.getColumns());
                    if (selectingColumns) {
                        ip.addToSelected(col, mv.getColumns());
                        ip.setLastSelectedCol(col);
                    }
                } else {
                    row = 0;
                    ip.setLead(row, mv.getRows());
                    if (selectingRows) {
                        ip.addToSelected(row, mv.getRows());
                        ip.setLastSelectedRow(row);
                    }
                }
                break;
            case KeyEvent.VK_END:
                if (shiftDown) {
                    col = ip.getColumnMax();
                    ip.setLead(++col, mv.getColumns());
                    if (selectingColumns) {
                        ip.addToSelected(col, mv.getColumns());
                        ip.setLastSelectedCol(col);
                    }
                } else {
                    row = ip.getRowMax();
                    ip.setLead(row, mv.getRows());
                    if (selectingRows) {
                        ip.addToSelected(row, mv.getRows());
                        ip.setLastSelectedRow(row);
                    }
                }
        }
    }

    private void switchLeadRowSelection(KeyEvent e) {

        int modifiers = e.getModifiers();
        boolean ctrlDown = ((modifiers & ctrlMask) != 0);
        boolean altDown = ((modifiers & altMask) != 0);
        if (ctrlDown) {
            return;
        }

        IMatrixView mv = this.mv;
        IMatrixViewDimension dim = mv.getRows();
        String lead = dim.getFocus();

        if (lead == null) {
            return;
        }

        ip.switchSelection(dim, dim.indexOf(lead), altDown);
    }

    private void switchLeadColSelection(KeyEvent e) {

        int modifiers = e.getModifiers();
        boolean ctrlDown = ((modifiers & ctrlMask) != 0);
        boolean altDown = ((modifiers & altMask) != 0);
        if (ctrlDown) {
            return;
        }

        IMatrixView mv = this.mv;
        IMatrixViewDimension dim = mv.getColumns();
        String lead = dim.getFocus();
        if (lead == null) {
            return;
        }

        ip.switchSelection(dim, dim.indexOf(lead), altDown);
    }


}
