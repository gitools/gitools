package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.model.ITableModel;

public class UnselectAllAction extends BaseAction {

	private static final long serialVersionUID = 1581417292789818975L;

	public UnselectAllAction() {
		super("Unselect all");
		
		setDesc("Unselect all");
		setSmallIconFromResource(IconNames.unselectAll16);
		setLargeIconFromResource(IconNames.unselectAll24);
		setMnemonic(KeyEvent.VK_U);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ITableModel tableModel = getTableModel();
		
		if (tableModel != null)		
			tableModel.resetSelection();
	}

}
