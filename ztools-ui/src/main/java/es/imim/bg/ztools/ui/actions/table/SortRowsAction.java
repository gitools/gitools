package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;
import java.util.List;


import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.dialogs.SortListDialog;
import es.imim.bg.ztools.ui.dialogs.DefineSortCriteriaDialog.SortCriteria;
import es.imim.bg.ztools.ui.model.table.ITable;

public class SortRowsAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public SortRowsAction() {
		super("Sort rows by ...");	
		setDesc("Sort rows by ...");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ITable table = getTable();
		if (table == null)
			return;

		Object[] params = {"p1", "p2", "p3", "p4"};
		
		SortListDialog d = new SortListDialog(AppFrame.instance(), params);
		List<SortCriteria> valueList = d.getValueList();
		boolean onlySelectedColumns = d.getOnlySelectedColumnsChecked();
		if(valueList != null) {
			//TODO: Filter!!
		}
	}
}
