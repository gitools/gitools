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
package org.gitools.ui.app.actions.data;

import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.view.Direction;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.ui.app.DimensionIcons;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.actions.HeatmapDimensionAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;
import static org.gitools.api.matrix.view.Direction.*;

public class MoveSelectionAction extends HeatmapDimensionAction {

    public enum MoveDirection {

        ROW_UP(ROWS, UP, KeyEvent.VK_U),
        ROW_DOWN(ROWS, DOWN, KeyEvent.VK_D),
        COL_LEFT(COLUMNS, LEFT, KeyEvent.VK_L),
        COL_RIGHT(COLUMNS, RIGHT, KeyEvent.VK_R);

        public Direction direction;
        public MatrixDimensionKey dimension;
        public int mnemonic;

        private MoveDirection(MatrixDimensionKey dimension, Direction direction, int mnemonic) {
            this.dimension = dimension;
            this.direction = direction;
            this.mnemonic = mnemonic;
        }
    }

    private final MoveDirection dir;

    public MoveSelectionAction(MoveDirection dir) {
        super(dir.dimension, "Move " + dir.dimension.getLabel() + " " + dir.direction);

        this.dir = dir;
        DimensionIcons icons = IconNames.get(dir.dimension);

        setSmallIconFromResource(dir.direction.getShift() == 1 ? icons.getMoveForward16() : icons.getMoveBackward16());
        setLargeIconFromResource(dir.direction.getShift() == 1 ? icons.getMoveForward24() : icons.getMoveBackward24());

        setMnemonic(dir.mnemonic);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        HeatmapDimension dimension = getDimension();
        dimension.move(dir.direction, dimension.getSelected());
    }
}
