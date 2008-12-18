package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.table.TableModel;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.dialogs.NameListDialog;
import es.imim.bg.ztools.ui.dialogs.ValueListDialog;
import es.imim.bg.ztools.ui.dialogs.DefineCriteriaDialog.Criteria;
import es.imim.bg.ztools.ui.model.ITableModel;

public class ShowRowsByValuesAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public ShowRowsByValuesAction() {
		super("Show rows by value");	
		setDesc("Show rows by value");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ITableModel tableModel = getTableModel();
		if (tableModel == null)
			return;

		Object[] params = {"p1", "p2", "p3", "p4"};
		
		ValueListDialog d = new ValueListDialog(AppFrame.instance(), params);
		List<Criteria> valueList = d.getValueList();
		if(valueList != null) {
			//TODO: Filter!!
		}
	}
}
