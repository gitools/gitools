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
package org.gitools.ui.heatmap.popupmenus;

import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.ui.actions.Actions;
import org.gitools.ui.actions.data.FastSortValueAction;
import org.gitools.ui.actions.data.GroupSelectionAction;
import org.gitools.ui.actions.data.ShowAllAction;
import org.gitools.ui.actions.edit.AddHeaderAction;
import org.gitools.ui.actions.edit.EditAllHeaderAction;
import org.gitools.ui.actions.edit.InvertSelectionAction;
import org.gitools.ui.heatmap.editor.HeatmapSearchAction;
import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.BaseAction;

public class PopupMenuActions {

    private static ActionSet getMenu(MatrixDimensionKey dimensionKey) {

        return new ActionSet(new BaseAction[] {
                Actions.selectAllAction,
                Actions.selectLabelHeaderAction,
                new GroupSelectionAction(dimensionKey),
                Actions.unselectAllAction,
                new InvertSelectionAction(dimensionKey),
                Actions.copyToClipboardSelectedLabelHeader,
                BaseAction.separator,
                new ShowAllAction(dimensionKey),
                Actions.showOnlyHeaderAction,
                Actions.hideSelectedRowsAction,
                Actions.hideEmptyLabelHeaderAction,
                Actions.hideThisLabelHeaderAction,
                Actions.hideGreaterThanHeaderAction,
                Actions.hideSmallerThanHeaderAction,
                BaseAction.separator,
                new FastSortValueAction(dimensionKey),
                Actions.sortByHeader,
                BaseAction.separator,
                new HeatmapSearchAction(dimensionKey),
                BaseAction.separator,
                new EditAllHeaderAction(dimensionKey),
                new AddHeaderAction(dimensionKey)
        });


    }

    public static final ActionSet ROWS = getMenu(MatrixDimensionKey.ROWS);
    public static final ActionSet COLUMNS = getMenu(MatrixDimensionKey.COLUMNS);
}
