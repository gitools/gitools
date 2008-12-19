package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;
import java.util.List;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.dialogs.NameListDialog;

public class ShowRowsByNamesAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public ShowRowsByNamesAction() {
		super("Show rows by name...");	
		setDesc("Show rows by name");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		NameListDialog d = new NameListDialog(AppFrame.instance());
		boolean regEx = d.isRegexChecked();
		List<String> names = d.getNameList();
		if(names != null) {
			//TODO: Filter!!
		}
	}
}
