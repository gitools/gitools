package es.imim.bg.ztools.ui.actions;

import es.imim.bg.ztools.ui.actions.edit.ChangeSelectionModeAction;
import es.imim.bg.ztools.ui.actions.edit.InvertSelectionAction;
import es.imim.bg.ztools.ui.actions.edit.SelectAllAction;
import es.imim.bg.ztools.ui.actions.edit.UnselectAllAction;
import es.imim.bg.ztools.ui.model.SelectionMode;

public final class EditActionSet extends ActionSet {

	private static final long serialVersionUID = 3549170839730150579L;
	
	public static final BaseAction selectAllAction = new SelectAllAction();
	public static final BaseAction invertSelectionAction = new InvertSelectionAction();
	public static final BaseAction unselectAllAction = new UnselectAllAction();
	
	public static final BaseAction columnSelectionModeAction = 
		new ChangeSelectionModeAction(SelectionMode.columns);
	
	public static final BaseAction rowSelectionModeAction = 
		new ChangeSelectionModeAction(SelectionMode.rows);
	
	public static final BaseAction cellSelectionModeAction = 
		new ChangeSelectionModeAction(SelectionMode.cells);

	public static final ActionSet selectionModeActionSet = new ActionSet("Selection mode", new BaseAction[] {
			columnSelectionModeAction,
			rowSelectionModeAction,
			cellSelectionModeAction
		});
	
	public EditActionSet() {
		super("Edit", new BaseAction[] {
				selectAllAction,
				unselectAllAction,
				invertSelectionAction,
				selectionModeActionSet
		});
	}
}
