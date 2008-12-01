package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.model.ITableModel;

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
		/*List<SortCriteria> criteriaList = 
			new ArrayList<SortCriteria>(indices.length);
		
		for (int i = 0; i <  indices.length; i++)
			criteriaList.add(new SortCriteria(
					indices[i], selParamIndex, true));*/
		
		ITableModel tableModel = getTableModel();
		
		if (tableModel == null)
			return;

		tableModel.sortByFunc(
				tableModel.getSelectedColumns());
		
		AppFrame.instance()
			.setStatusText("Selected rows hided.");
	}

}
