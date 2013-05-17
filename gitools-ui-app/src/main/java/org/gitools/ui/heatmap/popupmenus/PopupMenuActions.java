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

import org.gitools.ui.actions.DataActions;
import org.gitools.ui.actions.EditActions;
import org.gitools.ui.actions.HeatmapActions;
import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.BaseAction;

public class PopupMenuActions {

    public static final ActionSet ROWS = new ActionSet(new BaseAction[]{
            EditActions.selectAllAction,
            EditActions.unselectAllAction,
            EditActions.invertSelectionAction,
            DataActions.copyToClipboardSelectedLabelHeader,
            BaseAction.separator,
            DataActions.showAllRowsAction,
            DataActions.showOnlyHeaderAction,
            DataActions.hideSelectedRowsAction,
            DataActions.hideEmptyLabelHeaderAction,
            DataActions.hideThisLabelHeaderAction,
            DataActions.hideGreaterThanHeaderAction,
            DataActions.hideSmallerThanHeaderAction,
            BaseAction.separator,
            DataActions.fastSortRowsAction,
            DataActions.sortByHeader,
            BaseAction.separator,
            HeatmapActions.searchRowsAction,
            BaseAction.separator,
            EditActions.editRowHeader,
            EditActions.addRowHeader
    });

    public static final ActionSet COLUMNS = new ActionSet(new BaseAction[]{
            EditActions.selectAllAction,
            EditActions.unselectAllAction,
            EditActions.invertSelectionAction,
            DataActions.copyToClipboardSelectedLabelHeader,
            BaseAction.separator,
            DataActions.showAllColumnsAction,
            DataActions.showOnlyHeaderAction,
            DataActions.hideSelectedColumnsAction,
            DataActions.hideEmptyLabelHeaderAction,
            DataActions.hideThisLabelHeaderAction,
            DataActions.hideGreaterThanHeaderAction,
            DataActions.hideSmallerThanHeaderAction,
            BaseAction.separator,
            DataActions.sortByHeader,
            BaseAction.separator,
            HeatmapActions.searchColumnsAction,
            BaseAction.separator,
            EditActions.editColumnHeader,
            EditActions.addColumnHeader
    });
}
