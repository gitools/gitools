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
package org.gitools.ui.app.heatmap.popupmenus;

import org.apache.commons.lang.StringUtils;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.ui.app.actions.Actions;
import org.gitools.ui.app.actions.data.FastSortValueAction;
import org.gitools.ui.app.actions.data.GroupSelectionAction;
import org.gitools.ui.app.actions.data.HideSelectionAction;
import org.gitools.ui.app.actions.data.ShowAllAction;
import org.gitools.ui.app.actions.data.analysis.SortByHierarchicalClusteringAction;
import org.gitools.ui.app.actions.data.analysis.ViewDendrogramAction;
import org.gitools.ui.app.actions.data.analysis.ViewGroupComparisonResultDataAction;
import org.gitools.ui.app.actions.edit.*;
import org.gitools.ui.core.actions.ActionSet;
import org.gitools.ui.core.actions.BaseAction;
import org.gitools.ui.core.actions.PopupSectionTitleAction;
import org.gitools.ui.platform.icons.IconNames;

public class PopupMenuActions {

    private static ActionSet getHeatmapMenu(MatrixDimensionKey dimensionKey) {

        String dimensionLabel = StringUtils.capitalize(dimensionKey.getLabel());

        return new ActionSet(new BaseAction[]{

                // Analysis specific
                Actions.viewEnrichmentModuleData,
                new ViewGroupComparisonResultDataAction(),
                new ViewDendrogramAction(),

                // Data selection
                new PopupSectionTitleAction("Selection"),
                BaseAction.separator,
                Actions.selectAllAction,
                Actions.selectLabelHeaderAction,
                Actions.unselectLabelHeaderAction,
                new GroupSelectionAction(dimensionKey),
                Actions.unselectAllAction,
                new InvertSelectionAction(dimensionKey),
                Actions.copyToClipboardSelectedLabelHeader,

                new PopupSectionTitleAction("Visibility"),
                BaseAction.separator,
                new ShowAllAction(dimensionKey),
                Actions.showOnlyHeaderAction,
                new HideSelectionAction(dimensionKey),
                Actions.hideThisLabelHeaderAction,
                Actions.hideGreaterThanHeaderAction,
                Actions.hideSmallerThanHeaderAction,

                //Sorting
                new PopupSectionTitleAction("Sorting"),
                BaseAction.separator,
                new SortByHierarchicalClusteringAction(),
                new FastSortValueAction(dimensionKey == MatrixDimensionKey.ROWS ? MatrixDimensionKey.ROWS : MatrixDimensionKey.COLUMNS),
                Actions.sortByHeader,
                Actions.invertOrder,

                //BaseAction.separator,
                //new HeatmapSearchAction(dimensionKey),
                BaseAction.separator,
                new PopupSectionTitleAction(dimensionLabel + " headers"),
                new AddHeaderAction(dimensionKey),
                new EditHeaderAction(dimensionKey, "<html><i>Edit</i> header</html>"),
                new EditAnnotationValueAction(dimensionKey, "<html><i>Edit</i> annotation value</html>"),
                new RemoveHeaderAction(dimensionKey, "Remove " + dimensionLabel + " header")


        });
    }

    public static final ActionSet ROWS = getHeatmapMenu(MatrixDimensionKey.ROWS);
    public static final ActionSet COLUMNS = getHeatmapMenu(MatrixDimensionKey.COLUMNS);

    // Details popup menus
    public static final ActionSet DETAILS_ROWS = new ActionSet(new BaseAction[]{
            new EditHeaderAction(MatrixDimensionKey.ROWS, "Edit..."),
            BaseAction.separator,
            new MoveUpHeaderAction("Move up (left)", MatrixDimensionKey.ROWS, IconNames.moveUp16),
            new MoveDownHeaderAction("Move down (right)", MatrixDimensionKey.ROWS, IconNames.moveDown16),
            BaseAction.separator,
            new RemoveHeaderAction(MatrixDimensionKey.ROWS, "Remove from heatmap")
    });

    public static final ActionSet DETAILS_COLUMNS = new ActionSet(new BaseAction[]{
            new EditHeaderAction(MatrixDimensionKey.COLUMNS, "Edit..."),
            BaseAction.separator,
            new MoveDownHeaderAction("Move up", MatrixDimensionKey.COLUMNS, IconNames.moveUp16),
            new MoveUpHeaderAction("Move down", MatrixDimensionKey.COLUMNS, IconNames.moveDown16),
            BaseAction.separator,
            new RemoveHeaderAction(MatrixDimensionKey.COLUMNS, "Remove from heatmap")
    });

    public static final ActionSet DETAILS_LAYERS = new ActionSet(new BaseAction[]{
            new EditLayerAction("Edit..."),
            BaseAction.separator,
            new MoveUpLayerAction(),
            new MoveDownLayerAction()
    });


}
