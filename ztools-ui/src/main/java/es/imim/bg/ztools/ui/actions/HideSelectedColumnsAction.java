package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.model.ITableModel;

public class HideSelectedColumnsAction extends BaseAction {

	private static final long serialVersionUID = 1453040322414160605L;

	public HideSelectedColumnsAction() {
		super("Hide selected columns");
		
		setDesc("Hide selected columns");
		setSmallIconFromResource(IconNames.columnHide16);
		setLargeIconFromResource(IconNames.columnHide24);
		setMnemonic(KeyEvent.VK_O);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ITableModel tableModel = getTableModel();
		
		if (tableModel == null)
			return;
		
		tableModel.hideColumns(
				tableModel.getSelectedColumns());
		
		tableModel.resetSelection();
		
		AppFrame.instance()
			.setStatusText("Selected columns hided.");
	}

}
