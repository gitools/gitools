package org.gitools.ui.actions;

import org.gitools.ui.platform.actions.ActionManager;

public class Actions {
	
	public static final MenuActionSet menuActionSet = new MenuActionSet();
	
	public static final ToolBarActionSet toolBarActionSet = new ToolBarActionSet();
	
	private Actions() {
	}

	public static final void init() {
		ActionManager am = ActionManager.getDefault();
		am.addRootAction(menuActionSet);
		am.addRootAction(toolBarActionSet);
		
		/*am.addActionsFromClass(FileActions.class);
		am.addActionsFromClass(EditActions.class);
		am.addActionsFromClass(DataActions.class);
		am.addActionsFromClass(MtcActions.class);
		am.addActionsFromClass(HeatmapActions.class);*/
	}
}
