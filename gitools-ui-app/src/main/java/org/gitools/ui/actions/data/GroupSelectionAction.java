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

import org.apache.commons.lang.ArrayUtils;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.drawer.HeatmapPosition;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.matrix.model.IMatrixViewDimension;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.heatmap.popupmenus.dynamicactions.IHeatmapDimensionAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.utils.HeaderEnum;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

public class GroupSelectionAction extends BaseAction implements IHeatmapDimensionAction {

    private static final long serialVersionUID = 1453040322414160605L;

    private final HeaderEnum.Dimension type;
    private HeatmapPosition position;

    public GroupSelectionAction(@NotNull HeaderEnum.Dimension type) {
        super(null);

        this.type = type;
        switch (type) {
            case ROW:
                setName("Group selected rows here");
                setDesc("Group selected rows here");
                break;
            case COLUMN:
                setName("Group selected columns here");
                setDesc("Group selected columns here");
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

        String msg = "";

        switch (type) {
            case ROW:
                groupSelected(matrixView.getRows(), position.getRow());
                msg = "Selected rows grouped.";
                break;
            case COLUMN:
                groupSelected(matrixView.getColumns(), position.getColumn());
                msg = "Selected columns grouped.";
                break;
        }

        AppFrame.get().setStatusText(msg);
    }

    private void groupSelected(IMatrixViewDimension dim, int position) {
        int[] visibleIndices = dim.getVisibleIndices();
        int[] selectedIndices = new int[dim.getSelected().length];
        int[] unselectedIndices = new int[visibleIndices.length - selectedIndices.length];

        int progress = 0;
        int positionOffset = 0;
        for (int i : dim.getSelected()) {
            selectedIndices[progress++] = visibleIndices[i];
            if (i <= position) {
                positionOffset++;
            }
        }


        progress = 0;
        for (int i = 0; i < visibleIndices.length; i++) {
            if (!ArrayUtils.contains(dim.getSelected(), i)) {
                unselectedIndices[progress++] = visibleIndices[i];
            }
        }

        int[] newOrder = new int[visibleIndices.length];

        position -= positionOffset;
        int selectedProgress = 0;
        int unselectedProgress = 0;
        boolean drawFromSelected;
        for (int i = 0; i < newOrder.length; i++) {
            drawFromSelected = i >= position && i < position + selectedIndices.length;

            newOrder[i] = drawFromSelected ?
                    selectedIndices[selectedProgress++] :
                    unselectedIndices[unselectedProgress++];
        }

        dim.setVisibleIndices(newOrder);

    }


    @Override
    public void onConfigure(HeatmapDimension dimension, HeatmapPosition position) {

        // Enable only if there is at least one item selected
        setEnabled(dimension.getSelected().length > 0);
        this.position = position;
    }
}
