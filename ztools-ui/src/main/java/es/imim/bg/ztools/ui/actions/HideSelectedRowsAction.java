package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.model.ITableModel;

public class HideSelectedRowsAction extends BaseAction {

	private static final long serialVersionUID = 1453040322414160605L;

	public HideSelectedRowsAction() {
		super("Hide selected rows");
		
		setDesc("Hide selected rows");
		setSmallIconFromResource(IconNames.rowHide16);
		setLargeIconFromResource(IconNames.rowHide24);
		setMnemonic(KeyEvent.VK_W);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ITableModel tableModel = getTableModel();
		
		if (tableModel == null)
			return;
		
		tableModel.hideRows(
				tableModel.getSelectedRows());
		
		tableModel.resetSelection();
		
		AppFrame.instance()
			.setStatusText("Selected rows hided.");
	}

}