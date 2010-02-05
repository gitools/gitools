package org.gitools.ui.actions;

import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.actions.SeparatorAction;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import org.gitools.ui.actions.DataActionSet;
import org.gitools.ui.actions.EditActionSet;
import org.gitools.ui.actions.FileActionSet;
import org.gitools.ui.actions.HelpActionSet;
import org.gitools.ui.actions.MatrixActionSet;
import org.gitools.ui.platform.actions.ActionSetUtils;

public class MenuActionSet extends ActionSet {

	private static final long serialVersionUID = -7702905459240675073L;

	public static final ActionSet fileActionSet = new FileActionSet();
	public static final ActionSet editActionSet = new EditActionSet();
	public static final ActionSet dataActionSet = new DataActionSet();
	public static final ActionSet matrixActionSet = new MatrixActionSet();
	public static final ActionSet helpActionSet = new HelpActionSet();
	
	public MenuActionSet() {
		super(new BaseAction[] {
			fileActionSet,
			editActionSet,
			dataActionSet,
			//matrixActionSet,
			helpActionSet
		});
	}
	
	public JMenuBar createMenuBar() {
		return ActionSetUtils.createMenuBar(this);
	}
}
