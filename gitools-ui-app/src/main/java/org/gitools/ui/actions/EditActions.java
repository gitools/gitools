package org.gitools.ui.actions;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.actions.edit.InvertSelectionAction;
import org.gitools.ui.actions.edit.SelectAllAction;
import org.gitools.ui.actions.edit.UnselectAllAction;

public final class EditActions {
	
	public static final BaseAction selectAllAction = new SelectAllAction();

	public static final BaseAction invertSelectionAction = new InvertSelectionAction();

	public static final BaseAction unselectAllAction = new UnselectAllAction();

}
