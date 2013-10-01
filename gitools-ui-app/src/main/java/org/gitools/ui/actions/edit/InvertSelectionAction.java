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
package org.gitools.ui.actions.edit;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.drawer.HeatmapPosition;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.heatmap.popupmenus.dynamicactions.IHeatmapDimensionAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.utils.HeaderEnum;

import java.awt.event.ActionEvent;

public class InvertSelectionAction extends BaseAction implements IHeatmapDimensionAction {

    private static final long serialVersionUID = 3124483059501436713L;
    private final HeaderEnum.Dimension dim;


    public InvertSelectionAction(HeaderEnum.Dimension dimension) {
        super("Invert selection");

        setDesc("Invert selection");
        this.dim = dimension;

        switch (dim) {
            case COLUMN:
                setName("Invert column selection");
                setDesc("Invert column selection");
                break;
            case ROW:
                setName("Invert row selection");
                setDesc("Invert row selection");
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


        if (matrixView != null) {
            if (this.dim != HeaderEnum.Dimension.COLUMN) {
                matrixView.getRows().invertSelection();
            }

            if (this.dim != HeaderEnum.Dimension.ROW) {
                matrixView.getColumns().invertSelection();
            }
        }

        AppFrame.get().setStatusText("Selection inverted");
    }

    @Override
    public void onConfigure(HeatmapDimension dimension, HeatmapPosition position) {

        // Enable only if there is at least one item selected
        setEnabled(dimension.getSelected().length > 0);
    }
}
