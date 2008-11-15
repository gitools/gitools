package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.model.ISectionModel;
import es.imim.bg.ztools.ui.model.ITableModel;
import es.imim.bg.ztools.ui.views.AbstractView;

public class SelectAllAction extends BaseAction {

	private static final long serialVersionUID = 3088237733885396229L;

	public SelectAllAction() {
		super("Select all");
		
		setDesc("Select all");
		setSmallIconFromResource(IconNames.selectAll16);
		setLargeIconFromResource(IconNames.selectAll24);
		setMnemonic(KeyEvent.VK_A);
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
			tableModel.selectAll();
	}

}
