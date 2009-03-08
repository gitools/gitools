package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ListIterator;

import es.imim.bg.ztools.table.element.IElementProperty;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.dialogs.SortRowsDialog;
import es.imim.bg.ztools.ui.dialogs.SortRowsDialog.SortCriteria;
import es.imim.bg.ztools.ui.model.table.ITable;

public class SortRowsAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public SortRowsAction() {
		super("Sort rows by ...");	
		setDesc("Sort rows by ...");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ITable table = getTable();
		if (table == null)
			return;

		
		//select parameters
		List<IElementProperty> cellProps = table.getCellAdapter().getProperties();
		ListIterator<IElementProperty> i = cellProps.listIterator();
		Object[] params = new Object[cellProps.size()];
		int counter = 0;
		while (i.hasNext()) {
			IElementProperty ep = i.next();
			params[counter] = ep.getId();
			counter++;
		}
		
		SortRowsDialog d = new SortRowsDialog(AppFrame.instance(), params);
		List<SortCriteria> valueList = d.getValueList();
		boolean onlySelectedColumns = d.isOnlySelectedColumnsChecked();
		if(valueList != null) {
			int[] rows;
			if(onlySelectedColumns) 
				rows = table.getSelectedRows();
			else
				rows = table.getVisibleRows();
			int[] sortedRows = sortRows(rows, valueList);
		}
	}

	private int[] sortRows(int[] rows, List<SortCriteria> valueList) {
		int[] sortedRows = null;
				
		
		
		return sortedRows;
	}
}
