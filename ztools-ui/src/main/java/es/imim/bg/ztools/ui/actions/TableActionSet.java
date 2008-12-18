package es.imim.bg.ztools.ui.actions;

import es.imim.bg.ztools.ui.actions.table.ShowRowsByNamesAction;
import es.imim.bg.ztools.ui.actions.table.HideSelectedColumnsAction;
import es.imim.bg.ztools.ui.actions.table.HideSelectedRowsAction;
import es.imim.bg.ztools.ui.actions.table.MoveColRowAction;
import es.imim.bg.ztools.ui.actions.table.ShowRowsByValuesAction;
import es.imim.bg.ztools.ui.actions.table.SortRowsAction;
import es.imim.bg.ztools.ui.actions.table.SortSelectedColumnsAction;
import es.imim.bg.ztools.ui.actions.table.MoveColRowAction.MoveDirection;

public final class TableActionSet extends ActionSet {

	private static final long serialVersionUID = 4844715504798938035L;
	
	public static final BaseAction showRowsByNames = new ShowRowsByNamesAction();
	public static final BaseAction showRowsByValues = new ShowRowsByValuesAction();
	public static final BaseAction hideSelectedColumnsAction = new HideSelectedColumnsAction();
	public static final BaseAction sortSelectedColumnsAction = new SortSelectedColumnsAction();
	public static final BaseAction sortRowsAction = new SortRowsAction();
	public static final BaseAction hideSelectedRowsAction = new HideSelectedRowsAction();
	
	public static final BaseAction moveRowsUpAction = 
		new MoveColRowAction(MoveDirection.ROW_UP);
	
	public static final BaseAction moveRowsDownAction = 
		new MoveColRowAction(MoveDirection.ROW_DOWN);
	
	public static final BaseAction moveColsLeftAction = 
		new MoveColRowAction(MoveDirection.COL_LEFT);
	
	public static final BaseAction moveColsRightAction = 
		new MoveColRowAction(MoveDirection.COL_RIGHT);

	public TableActionSet() {
		super("Table", new BaseAction[] {
			showRowsByNames,
			showRowsByValues,
			BaseAction.separator,
			hideSelectedColumnsAction,
			hideSelectedRowsAction,
			BaseAction.separator,
			moveColsLeftAction,
			moveColsRightAction,
			moveRowsUpAction,
			moveRowsDownAction,
			BaseAction.separator,
			sortSelectedColumnsAction,
			sortRowsAction
		});
	}
}
