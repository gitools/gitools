package org.gitools.ui.actions;

import javax.swing.JToolBar;
import org.gitools.ui.actions.DataActionSet;
import org.gitools.ui.actions.EditActionSet;
import org.gitools.ui.actions.FileActionSet;

public final class ToolBarActionSet extends ActionSet {

	private static final long serialVersionUID = 6924230823891805344L;

	public ToolBarActionSet() {
		super(new BaseAction[] {
				FileActionSet.openAnalysisAction,
				FileActionSet.openMatrixAction,
				FileActionSet.closeAction,
				BaseAction.separator,
				/*EditActionSet.columnSelectionModeAction,
				EditActionSet.rowSelectionModeAction,
				EditActionSet.cellSelectionModeAction,
				BaseAction.separator,*/
				EditActionSet.selectAllAction,
				EditActionSet.unselectAllAction,
				BaseAction.separator,
				DataActionSet.hideSelectedColumns,
				DataActionSet.showAllColumns,
				DataActionSet.hideSelectedRows,
				DataActionSet.showAllRows,
				BaseAction.separator,
				DataActionSet.moveColsLeftAction,
				DataActionSet.moveColsRightAction,
				DataActionSet.moveRowsUpAction,
				DataActionSet.moveRowsDownAction,
				BaseAction.separator,
				DataActionSet.fastSortRowsAction	
		});
	}
	
	public JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		
		for (BaseAction a : actions)
			if (a instanceof SeparatorAction)
				toolBar.addSeparator();
			else
				toolBar.add(a);
		
		return toolBar;
	}
}
