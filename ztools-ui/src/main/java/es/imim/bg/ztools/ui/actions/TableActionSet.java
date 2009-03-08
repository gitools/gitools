package es.imim.bg.ztools.ui.actions;

import es.imim.bg.ztools.ui.actions.table.HideSelectionAction;
import es.imim.bg.ztools.ui.actions.table.MoveSelectionAction;
import es.imim.bg.ztools.ui.actions.table.ShowAllAction;
import es.imim.bg.ztools.ui.actions.table.FilterColumnsByNamesAction;
import es.imim.bg.ztools.ui.actions.table.FilterRowsByNamesAction;
import es.imim.bg.ztools.ui.actions.table.FilterRowsByValuesAction;
import es.imim.bg.ztools.ui.actions.table.SortRowsAction;
import es.imim.bg.ztools.ui.actions.table.FastSortRowsAction;
import es.imim.bg.ztools.ui.actions.table.HideSelectionAction.ElementType;
import es.imim.bg.ztools.ui.actions.table.MoveSelectionAction.MoveDirection;

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
	
	public static final BaseAction hideSelectedColumnsAction = 
		new HideSelectionAction(ElementType.COLUMNS);
	
	public static final BaseAction fastSortRowsAction = 
		new FastSortRowsAction();

	public static final BaseAction sortRowsAction = 
		new SortRowsAction();
	
	public static final BaseAction hideSelectedRowsAction = 
		new HideSelectionAction(ElementType.ROWS);
	
	public static final BaseAction moveRowsUpAction = 
		new MoveSelectionAction(MoveDirection.ROW_UP);
	
	public static final BaseAction moveRowsDownAction = 
		new MoveSelectionAction(MoveDirection.ROW_DOWN);
	
	public static final BaseAction moveColsLeftAction = 
		new MoveSelectionAction(MoveDirection.COL_LEFT);
	
	public static final BaseAction moveColsRightAction = 
		new MoveSelectionAction(MoveDirection.COL_RIGHT);

	public static final ActionSet moveActionSet = new ActionSet("Move", new BaseAction[] {
			moveRowsUpAction,
			moveRowsDownAction,
			moveColsLeftAction,
			moveColsRightAction
	});
	
	public static final ActionSet rowsActionSet = new ActionSet("Rows", new BaseAction[] {
			filterRowsByNames,
			filterRowsByValues,
			fastSortRowsAction,
			showAllRows,
			hideSelectedRowsAction
	});
	
	public static final ActionSet columnsActionSet = new ActionSet("Columns", new BaseAction[] {
			filterColumnsByNames,
			showAllColumns,
			hideSelectedColumnsAction
	});
	
	public TableActionSet() {
		super("Table", new BaseAction[] {
			rowsActionSet,
			BaseAction.separator,
			columnsActionSet,
			BaseAction.separator,
			moveActionSet//,
			//BaseAction.separator,
			//fastSortRowsAction,
			//sortRowsAction
		});
	}
}
