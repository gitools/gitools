package es.imim.bg.ztools.ui.actions.edit;

import java.awt.event.ActionEvent;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.model.ITableModel;
import es.imim.bg.ztools.ui.model.SelectionMode;

public class InvertSelectionAction extends BaseAction {

	private static final long serialVersionUID = 3124483059501436713L;

	public InvertSelectionAction() {
		super("Invert selection");
		
		setDesc("Invert selection");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ITableModel tableModel = getTableModel();
		
		
		if (tableModel != null){	
			tableModel.invertSelection();
		}
		
		AppFrame.instance()
		.setStatusText("Selection inverted");
	}

}
