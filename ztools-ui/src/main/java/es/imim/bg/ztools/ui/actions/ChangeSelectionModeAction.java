package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.model.ISectionModel;
import es.imim.bg.ztools.ui.model.SelectionMode;
import es.imim.bg.ztools.ui.model.ITableModel;
import es.imim.bg.ztools.ui.views.AbstractView;

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
		AbstractView view = getSelectedView();
		if (view == null)
			return;
		
		ITableModel tableModel = null;
		
		Object model = view.getModel();
		if (model instanceof ISectionModel) {
			ISectionModel sectionModel = (ISectionModel) model;
			tableModel = sectionModel.getTableModel();
		}
		else if (model instanceof ITableModel)
			tableModel = (ITableModel) model;
		
		if (tableModel != null)
			tableModel.setSelectionMode(mode);
		
		AppFrame.instance()
			.setStatusText("Selection mode changed to " + mode.toString() + ".");
	}

}
