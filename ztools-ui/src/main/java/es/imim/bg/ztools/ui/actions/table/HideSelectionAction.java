package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import es.imim.bg.ztools.table.ITable;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.actions.BaseAction;

public class HideSelectionAction extends BaseAction {

	private static final long serialVersionUID = 1453040322414160605L;

	public enum ElementType {
		ROWS, COLUMNS
	}
	
	private ElementType type;
	
	public HideSelectionAction(ElementType type) {
		super(null);
		
		this.type = type;
		switch (type) {
		case ROWS:
			setName("Hide selected rows");
			setDesc("Hide selected rows");
			setSmallIconFromResource(IconNames.rowHide16);
			setLargeIconFromResource(IconNames.rowHide24);
			setMnemonic(KeyEvent.VK_W);
			break;
		case COLUMNS:
			setName("Hide selected columns");
			setDesc("Hide selected columns");
			setSmallIconFromResource(IconNames.columnHide16);
			setLargeIconFromResource(IconNames.columnHide24);
			setMnemonic(KeyEvent.VK_O);
			break;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ITable table = getTable();
		
		if (table == null)
			return;		
		
		String msg = "";
		
		switch (type) {
		case ROWS:
			msg = "Selected rows hided.";
			table.setVisibleRows(arrayRemove(
					table.getVisibleRows(), 
					table.getSelectedRows()));
			break;
		case COLUMNS:
			msg = "Selected columns hided.";
			table.setVisibleColumns(arrayRemove(
					table.getVisibleColumns(), 
					table.getSelectedColumns()));
			break;
		}
		
		table.clearSelection();
		
		AppFrame.instance()
			.setStatusText(msg);
	}
	
	private int[] arrayRemove(int[] array, int[] indices) {
		int j = 0;
		int lastIndex = 0;
		int[] newIndices = new int[array.length - indices.length];
		
		Arrays.sort(indices);
		for (int i = 0; i < indices.length; i++) {
			int len = indices[i] - lastIndex;
			System.arraycopy(array, lastIndex, newIndices, j, len);
			lastIndex = indices[i] + 1;
			j += len;
		}
		System.arraycopy(array, lastIndex, newIndices, j, array.length - lastIndex);
		
		return newIndices;
	}
}
