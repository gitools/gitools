package org.gitools.ui.actions;

import org.gitools.ui.actions.help.AboutAction;
import org.gitools.ui.actions.help.WelcomeAction;

public final class HelpActionSet extends ActionSet {

	private static final long serialVersionUID = 2106803056919741368L;
	
	public static final BaseAction aboutAction = new AboutAction();
	
	public static final BaseAction welcomeAction = new WelcomeAction();

	public HelpActionSet() {
		super("Help", new BaseAction[] {
				welcomeAction,
				BaseAction.separator,
				aboutAction
		});
	}
}
