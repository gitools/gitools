package org.gitools.ui.actions;

import org.gitools.ui.platform.actions.ActionSet;
import org.gitools.ui.platform.actions.BaseAction;


public final class MatrixActionSet extends ActionSet {

	private static final long serialVersionUID = 4029483111783299361L;

	public static final BaseAction cloneAction = new UnimplementedAction("Clone");
	
	public MatrixActionSet() {
		super("Heatmap", new BaseAction[] {
				//cloneAction
		});
	}
}
