package org.gitools.ui.actions;

import org.gitools.ui.actions.table.FastSortRowsAction;
import org.gitools.ui.actions.table.FilterColumnsByNamesAction;
import org.gitools.ui.actions.table.FilterRowsByNamesAction;
import org.gitools.ui.actions.table.FilterRowsByValuesAction;
import org.gitools.ui.actions.table.HideSelectionAction;
import org.gitools.ui.actions.table.MoveSelectionAction;
import org.gitools.ui.actions.table.ShowAllAction;
import org.gitools.ui.actions.table.SortAction;
import org.gitools.ui.actions.table.HideSelectionAction.ElementType;
import org.gitools.ui.actions.table.MoveSelectionAction.MoveDirection;
import org.gitools.ui.actions.table.SortAction.SortSubject;

public final class TableActionSet extends ActionSet {

	private static final long serialVersionUID = 4844715504798938035L;
	
	public static final BaseAction filterRowsByNames = 
		new FilterRowsByNamesAction();
	
	public static final BaseAction filterRowsByValues = 
		new FilterRowsByValuesAction();
	
	public static final BaseAction showAllRows =
		new ShowAllAction(ShowAllAction.ElementType.ROWS);
	
	public static final BaseAction filterColumnsByNames = 
		new FilterColumnsByNamesAction();
	
	public static final BaseAction showAllColumns =
		new ShowAllAction(ShowAllAction.ElementType.COLUMNS);
	
	public static final BaseAction hideSelectedColumns = 
		new HideSelectionAction(ElementType.COLUMNS);
	
	public static final BaseAction fastSortRowsAction = 
		new FastSortRowsAction();

	public static final BaseAction sortRows = 
		new SortAction(SortSubject.ROW);
	
	public static final BaseAction sortColumns = 
		new SortAction(SortSubject.COLUMN);
	
	public static final BaseAction sortRowsAndColumns = 
		new SortAction(SortSubject.BOTH);
	
	public static final BaseAction hideSelectedRows = 
		new HideSelectionAction(ElementType.ROWS);
	
	public static final BaseAction moveRowsUpAction = 
		new MoveSelectionAction(MoveDirection.ROW_UP);
	
	public static final BaseAction moveRowsDownAction = 
		new MoveSelectionAction(MoveDirection.ROW_DOWN);
	
	public static final BaseAction moveColsLeftAction = 
		new MoveSelectionAction(MoveDirection.COL_LEFT);
	
	public static final BaseAction moveColsRightAction = 
		new MoveSelectionAction(MoveDirection.COL_RIGHT);


	
	/*public static final ActionSet rowsActionSet = new ActionSet("Rows", new BaseAction[] {

			//fastSortRowsAction,
	});
	
	public static final ActionSet columnsActionSet = new ActionSet("Columns", new BaseAction[] {
			
	});*/
	
	public static final ActionSet filterActionSet = new ActionSet("Filter", new BaseAction[] {
			filterRowsByNames,
			filterRowsByValues,
			filterColumnsByNames
	});
	
	public static final ActionSet moveActionSet = new ActionSet("Move", new BaseAction[] {
			moveRowsUpAction,
			moveRowsDownAction,
			moveColsLeftAction,
			moveColsRightAction
	});
	
	public static final ActionSet sortActionSet = new ActionSet("Sort", new BaseAction[] {
			sortRows,
			sortColumns,
			sortRowsAndColumns
	});
	
	public static final ActionSet visibilityActionSet = new ActionSet("Visibility", new BaseAction[] {
			showAllRows,
			hideSelectedRows,
			showAllColumns,
			hideSelectedColumns
	});

	
	
	public TableActionSet() {
		super("Table", new BaseAction[] {
			filterActionSet,
			moveActionSet,
			sortActionSet,
			visibilityActionSet
		});
	}
}
