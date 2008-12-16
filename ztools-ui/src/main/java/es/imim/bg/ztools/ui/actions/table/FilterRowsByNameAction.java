package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.dialogs.AboutDialog;
import es.imim.bg.ztools.ui.dialogs.FilterRowsByNameDialog;

public class FilterRowsByNameAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;


	public FilterRowsByNameAction() {
		super("Filter Rows by Name");	
		setDesc("Filter Rows by Name");
		}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new FilterRowsByNameDialog(AppFrame.instance())
		.setVisible(true);
	}
}
