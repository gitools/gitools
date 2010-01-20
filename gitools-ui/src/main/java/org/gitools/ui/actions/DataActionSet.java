package org.gitools.ui.actions;

import org.gitools.ui.actions.data.FastSortRowsAction;
import org.gitools.ui.actions.data.FilterByLabelAction;
import org.gitools.ui.actions.data.FilterByValueAction;
import org.gitools.ui.actions.data.HideSelectionAction;
import org.gitools.ui.actions.data.MoveSelectionAction;
import org.gitools.ui.actions.data.ShowAllAction;
import org.gitools.ui.actions.data.SortByValueAction;
import org.gitools.ui.actions.data.HideSelectionAction.ElementType;
import org.gitools.ui.actions.data.MoveSelectionAction.MoveDirection;
import org.gitools.ui.actions.data.SortByValueAction.SortSubject;

public final class DataActionSet extends ActionSet {

	private static final long serialVersionUID = 4844715504798938035L;
	
	public static final BaseAction filterByLabel = new FilterByLabelAction();
	
	public static final BaseAction filterByValue = new FilterByValueAction();
	
	public static final BaseAction showAllRows = new ShowAllAction(ShowAllAction.ElementType.ROWS);
	
	public static final BaseAction showAllColumns = new ShowAllAction(ShowAllAction.ElementType.COLUMNS);
	
	public static final BaseAction hideSelectedColumns = new HideSelectionAction(ElementType.COLUMNS);
	
	public static final BaseAction fastSortRowsAction = new FastSortRowsAction();

	public static final BaseAction sortRows = new SortByValueAction(SortSubject.ROW);
	
	public static final BaseAction sortColumns = new SortByValueAction(SortSubject.COLUMN);
	
	public static final BaseAction sortRowsAndColumns = new SortByValueAction(SortSubject.BOTH);
	
	public static final BaseAction hideSelectedRows = new HideSelectionAction(ElementType.ROWS);
	
	public static final BaseAction moveRowsUpAction = new MoveSelectionAction(MoveDirection.ROW_UP);
	
	public static final BaseAction moveRowsDownAction = new MoveSelectionAction(MoveDirection.ROW_DOWN);
	
	public static final BaseAction moveColsLeftAction = new MoveSelectionAction(MoveDirection.COL_LEFT);
	
	public static final BaseAction moveColsRightAction = new MoveSelectionAction(MoveDirection.COL_RIGHT);

	public static final ActionSet mtcActionSet = new MtcActionSet();
	
	/*public static final ActionSet rowsActionSet = new ActionSet("Rows", new BaseAction[] {

			//fastSortRowsAction,
	});
	
	public static final ActionSet columnsActionSet = new ActionSet("Columns", new BaseAction[] {
			
	});*/
	
	public static final ActionSet filterActionSet = new ActionSet("Filter", new BaseAction[] {
			filterByLabel,
			filterByValue
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
	
	public DataActionSet() {
		super("Data", new BaseAction[] {
			filterActionSet,
			sortActionSet,
			moveActionSet,
			visibilityActionSet,
			mtcActionSet
		});
	}
}
