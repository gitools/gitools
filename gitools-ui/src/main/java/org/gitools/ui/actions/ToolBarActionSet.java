package org.gitools.ui.actions;

import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.BaseAction;
import javax.swing.JToolBar;
import org.gitools.ui.platform.actions.ActionSetUtils;

public final class ToolBarActionSet extends ActionSet {

	private static final long serialVersionUID = 6924230823891805344L;

	public ToolBarActionSet() {
		super(new BaseAction[] {
				FileActionSet.openAnalysisAction,
				FileActionSet.openMatrixAction,
				FileActionSet.closeAction
		});
	}

	public JToolBar createToolBar() {
		return ActionSetUtils.createToolBar(this);
	}
}
