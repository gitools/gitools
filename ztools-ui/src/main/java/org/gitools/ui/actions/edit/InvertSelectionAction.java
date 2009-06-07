package org.gitools.ui.actions.edit;

import java.awt.event.ActionEvent;

import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;

import es.imim.bg.ztools.table.ITable;

public class InvertSelectionAction extends BaseAction {

	private static final long serialVersionUID = 3124483059501436713L;

	public InvertSelectionAction() {
		super("Invert selection");
		
		setDesc("Invert selection");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ITable table = getTable();
		
		
		if (table != null){	
			table.invertSelection();
		}
		
		AppFrame.instance()
			.setStatusText("Selection inverted");
	}

}
