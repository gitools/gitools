package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.model.ISectionModel;
import es.imim.bg.ztools.ui.model.ITableModel;
import es.imim.bg.ztools.ui.views.AbstractView;

public class SortSelectedColumnsAction extends BaseAction {

	private static final long serialVersionUID = -582380114189586206L;

	public SortSelectedColumnsAction() {
		super("Sort selected columns");
		
		setDesc("Sort selected columns");
		setSmallIconFromResource(IconNames.sortSelectedColumns16);
		setLargeIconFromResource(IconNames.sortSelectedColumns24);
		setMnemonic(KeyEvent.VK_S);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractView view = getSelectedView();
		if (view == null)
			return;
		
		/*List<SortCriteria> criteriaList = 
			new ArrayList<SortCriteria>(indices.length);
		
		for (int i = 0; i <  indices.length; i++)
			criteriaList.add(new SortCriteria(
					indices[i], selParamIndex, true));*/
		
		ITableModel tableModel = null;
		
		Object model = view.getModel();
		if (model instanceof ISectionModel) {
			ISectionModel sectionModel = (ISectionModel) model;
			tableModel = sectionModel.getTableModel();
		}
		else if (model instanceof ITableModel)
			tableModel = (ITableModel) model;
		
		if (tableModel == null)
			return;

		tableModel.sortByFunc(
				tableModel.getSelectedColumns());
		
		AppFrame.instance()
			.setStatusText("Selected rows hided.");
	}

}
