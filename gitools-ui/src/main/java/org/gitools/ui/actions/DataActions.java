package org.gitools.ui.actions;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.actions.data.FastSortRowsAction;
import org.gitools.ui.actions.data.FilterByLabelAction;
import org.gitools.ui.actions.data.FilterByValueAction;
import org.gitools.ui.actions.data.HideSelectionAction;
import org.gitools.ui.actions.data.MoveSelectionAction;
import org.gitools.ui.actions.data.ShowAllAction;
import org.gitools.ui.actions.data.SortByValueAction;
import org.gitools.ui.actions.data.HideSelectionAction.ElementType;
import org.gitools.ui.actions.data.MoveSelectionAction.MoveDirection;
import org.gitools.ui.actions.data.SortByLabelAction;

public final class DataActions {
	
	public static final BaseAction filterByLabelAction = new FilterByLabelAction();
	
	public static final BaseAction filterByValueAction = new FilterByValueAction();
	
	public static final BaseAction showAllRowsAction = new ShowAllAction(ShowAllAction.ElementType.ROWS);
	
	public static final BaseAction showAllColumnsAction = new ShowAllAction(ShowAllAction.ElementType.COLUMNS);
	
	public static final BaseAction hideSelectedColumnsAction = new HideSelectionAction(ElementType.COLUMNS);
	
	public static final BaseAction fastSortRowsAction = new FastSortRowsAction();

	public static final BaseAction sortByLabelAction = new SortByLabelAction();

	public static final BaseAction sortByValueAction = new SortByValueAction();
	
	public static final BaseAction hideSelectedRowsAction = new HideSelectionAction(ElementType.ROWS);
	
	public static final BaseAction moveRowsUpAction = new MoveSelectionAction(MoveDirection.ROW_UP);
	
	public static final BaseAction moveRowsDownAction = new MoveSelectionAction(MoveDirection.ROW_DOWN);
	
	public static final BaseAction moveColsLeftAction = new MoveSelectionAction(MoveDirection.COL_LEFT);
	
	public static final BaseAction moveColsRightAction = new MoveSelectionAction(MoveDirection.COL_RIGHT);
}
