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
package org.gitools.ui.actions.data;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.actions.BaseAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @noinspection ALL
 */
public class MoveSelectionAction extends BaseAction {

    private static final long serialVersionUID = 2499014276737037571L;

    public enum MoveDirection {
        ROW_UP, ROW_DOWN, COL_LEFT, COL_RIGHT
    }

    private final MoveDirection dir;

    public MoveSelectionAction(@NotNull MoveDirection dir) {
        super(null);

        this.dir = dir;

        switch (dir) {
            case ROW_UP:
                setName("Move row up");
                setDesc("Move row up");
                setSmallIconFromResource(IconNames.moveRowsUp16);
                setLargeIconFromResource(IconNames.moveRowsUp24);
                setMnemonic(KeyEvent.VK_U);
                break;
            case ROW_DOWN:
                setName("Move row down");
                setDesc("Move row down");
                setSmallIconFromResource(IconNames.moveRowsDown16);
                setLargeIconFromResource(IconNames.moveRowsDown24);
                setMnemonic(KeyEvent.VK_D);
                break;
            case COL_LEFT:
                setName("Move column left");
                setDesc("Move column left");
                setSmallIconFromResource(IconNames.moveColsLeft16);
                setLargeIconFromResource(IconNames.moveColsLeft24);
                setMnemonic(KeyEvent.VK_L);
                break;
            case COL_RIGHT:
                setName("Move column right");
                setDesc("Move column right");
                setSmallIconFromResource(IconNames.moveColsRight16);
                setLargeIconFromResource(IconNames.moveColsRight24);
                setMnemonic(KeyEvent.VK_R);
                break;
        }
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IMatrixView matrixView = ActionUtils.getMatrixView();

        if (matrixView == null) {
            return;
        }

        switch (dir) {
            case ROW_UP:
                matrixView.getRows().move(org.gitools.matrix.model.Direction.UP, matrixView.getRows().getSelected());
                break;
            case ROW_DOWN:
                matrixView.getRows().move(org.gitools.matrix.model.Direction.DOWN, matrixView.getRows().getSelected());
                break;
            case COL_LEFT:
                matrixView.getColumns().move(org.gitools.matrix.model.Direction.LEFT, matrixView.getColumns().getSelected());
                break;
            case COL_RIGHT:
                matrixView.getColumns().move(org.gitools.matrix.model.Direction.RIGHT, matrixView.getColumns().getSelected());
                break;
        }
    }
}
