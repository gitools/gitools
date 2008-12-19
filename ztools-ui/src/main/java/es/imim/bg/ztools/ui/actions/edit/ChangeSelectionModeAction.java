package es.imim.bg.ztools.ui.actions.edit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.model.deprecated.SelectionMode;
import es.imim.bg.ztools.ui.model.table.ITable;

public class ChangeSelectionModeAction extends BaseAction {

	private static final long serialVersionUID = -70164005532296778L;

	protected SelectionMode mode;
	
	public ChangeSelectionModeAction(SelectionMode mode) {
		super(null);
		switch (mode) {
		case columns:
			setName("Column selection mode");
			setDesc("Change selection mode to columns");
			setSmallIconFromResource(IconNames.columnSelection16);
			setLargeIconFromResource(IconNames.columnSelection24);
			setMnemonic(KeyEvent.VK_C);
			break;
		case rows:
			setName("Row selection mode");
			setDesc("Change selection mode to rows");
			setSmallIconFromResource(IconNames.rowSelection16);
			setLargeIconFromResource(IconNames.rowSelection24);
			setMnemonic(KeyEvent.VK_R);
			break;
		case cells:
			setName("Cell selection mode");
			setDesc("Change selection mode to cell");
			setSmallIconFromResource(IconNames.cellSelection16);
			setLargeIconFromResource(IconNames.cellSelection24);
			setMnemonic(KeyEvent.VK_E);
			break;
		}
		this.mode = mode;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		ITable table = getTable();
		
		if (table == null)
			return;
		
		//tableModel.setSelectionMode(mode);
		
		/*AppFrame.instance()
			.setStatusText("Selection mode changed to " + mode.toString() + ".");*/
	}

}
