package es.imim.bg.ztools.ui.actions;

import es.imim.bg.ztools.ui.actions.edit.InvertSelectionAction;
import es.imim.bg.ztools.ui.actions.edit.SelectAllAction;
import es.imim.bg.ztools.ui.actions.edit.UnselectAllAction;

public final class EditActionSet extends ActionSet {

	private static final long serialVersionUID = 3549170839730150579L;
	
	public static final BaseAction selectAllAction = new SelectAllAction();
	public static final BaseAction invertSelectionAction = new InvertSelectionAction();
	public static final BaseAction unselectAllAction = new UnselectAllAction();
	
	public EditActionSet() {
		super("Edit", new BaseAction[] {
				selectAllAction,
				unselectAllAction,
				invertSelectionAction
		});
	}
}
