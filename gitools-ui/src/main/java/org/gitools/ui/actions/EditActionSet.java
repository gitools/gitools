package org.gitools.ui.actions;

import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.actions.edit.InvertSelectionAction;
import org.gitools.ui.actions.edit.SelectAllAction;
import org.gitools.ui.actions.edit.UnselectAllAction;

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
