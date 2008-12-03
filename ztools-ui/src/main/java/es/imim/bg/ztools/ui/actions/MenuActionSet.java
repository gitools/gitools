package es.imim.bg.ztools.ui.actions;

import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MenuActionSet extends ActionSet {

	private static final long serialVersionUID = -7702905459240675073L;

	public static final ActionSet fileActionSet = new FileActionSet();
	public static final ActionSet editActionSet = new EditActionSet();
	public static final ActionSet tableActionSet = new TableActionSet();
	public static final ActionSet mtcActionSet = new MtcActionSet();
	public static final ActionSet helpActionSet = new HelpActionSet();
	
	public MenuActionSet() {
		super(new BaseAction[] {
			fileActionSet,
			editActionSet,
			tableActionSet,
			mtcActionSet,
			helpActionSet
		});
	}
	
	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		for (BaseAction a : actions)
			menuBar.add(createMenu(a));
		return menuBar;
	}
	
	public JMenu createMenu(BaseAction action) {
		JMenu menu = new JMenu(action);
		
		List<BaseAction> actions = ((ActionSet)action).getActions();
		for (BaseAction a : actions)
			if (a instanceof SeparatorAction)
				menu.addSeparator();
			else if (a instanceof ActionSet)
				menu.add(createMenu(a));
			else
				menu.add(a);
		
		return menu;
	}
}
