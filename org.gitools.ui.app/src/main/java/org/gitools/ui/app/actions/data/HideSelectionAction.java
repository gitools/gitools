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
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.ui.app.actions.HeatmapDimensionAction;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapDimensionAction;
import org.gitools.ui.platform.icons.IconNames;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class HideSelectionAction extends HeatmapDimensionAction implements IHeatmapDimensionAction {

    private static final long serialVersionUID = 1453040322414160605L;

    public HideSelectionAction(MatrixDimensionKey key) {
        super(key, "<html><i>Hide</i> selected<html>");

        setSmallIconFromResource(IconNames.get(key).getHide16());
        setLargeIconFromResource(IconNames.get(key).getHide24());
        setMnemonic(key == ROWS ? KeyEvent.VK_W : KeyEvent.VK_O);

    }

    @Override
    public boolean isEnabledByModel(Object model) {

        if (model instanceof Heatmap) {
            return !getDimension().getSelected().isEmpty();
        }

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        HeatmapDimension dimension = getDimension();
        dimension.hide(dimension.getSelected());
        dimension.getSelected().clear();

        Application.get().showNotification("Selected " + getDimensionLabel() + "s hidden");
    }

    @Override
    public void onConfigure(HeatmapDimension dimension, HeatmapPosition position) {

        // Enable only if there is at least one item selected
        setEnabled(dimension.getSelected().size() > 0);
    }
}
