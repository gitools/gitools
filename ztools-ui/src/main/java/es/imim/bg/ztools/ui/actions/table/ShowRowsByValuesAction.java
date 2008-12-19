package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;
import java.util.List;


import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.dialogs.ValueListDialog;
import es.imim.bg.ztools.ui.dialogs.DefineValueCriteriaDialog.ValueCriteria;
import es.imim.bg.ztools.ui.model.table.ITable;

public class ShowRowsByValuesAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public ShowRowsByValuesAction() {
		super("Show rows by values");	
		setDesc("Show rows by values");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ITable table = getTable();
		if (table == null)
			return;

		Object[] params = {"p1", "p2", "p3", "p4"};
		
		ValueListDialog d = new ValueListDialog(AppFrame.instance(), params);
		List<ValueCriteria> valueList = d.getValueList();
		if(valueList != null) {
			//TODO: Filter!!
		}
	}
}
