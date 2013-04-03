package org.gitools.ui.heatmap.editor;

import org.gitools.ui.actions.DataActions;
import org.gitools.ui.actions.EditActions;
import org.gitools.ui.actions.HeatmapActions;
import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.BaseAction;

public class HeatmapPopupmenus
{
    public static final ActionSet ROWS = new ActionSet(new BaseAction[]{
            EditActions.selectAllAction,
            EditActions.unselectAllAction,
            BaseAction.separator,
            DataActions.hideSelectedRowsAction,
            DataActions.showAllRowsAction,
            BaseAction.separator,
            DataActions.fastSortRowsAction,
            BaseAction.separator,
            HeatmapActions.searchRowsAction
    });

    public static final ActionSet COLUMNS = new ActionSet(new BaseAction[]{
            EditActions.selectAllAction,
            EditActions.unselectAllAction,
            BaseAction.separator,
            DataActions.hideSelectedColumnsAction,
            DataActions.showAllColumnsAction,
            BaseAction.separator,
            DataActions.fastSortRowsAction,
            BaseAction.separator,
            HeatmapActions.searchColumnsAction
    });
}
