package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.model.ITableModel;

public class MoveColRowAction extends BaseAction {

	private static final long serialVersionUID = 2499014276737037571L;

	public enum MoveDirection {
		ROW_UP, ROW_DOWN, COL_LEFT, COL_RIGHT
	}
	
	protected MoveDirection dir;
	
	public MoveColRowAction(MoveDirection dir) {
		super(null);
	
		this.dir = dir;
		
		switch (dir) {
		case ROW_UP:
			setName("Move row up");
			setDesc("Move row up");
			setSmallIconFromResource(IconNames.moveRowsUp16);
			setLargeIconFromResource(IconNames.moveRowsUp24);
			setMnemonic(KeyEvent.VK_U);
			break;
		case ROW_DOWN:
			setName("Move row down");
			setDesc("Move row down");
			setSmallIconFromResource(IconNames.moveRowsDown16);
			setLargeIconFromResource(IconNames.moveRowsDown24);
			setMnemonic(KeyEvent.VK_D);
			break;
		case COL_LEFT:
			setName("Move column left");
			setDesc("Move column left");
			setSmallIconFromResource(IconNames.moveColsLeft16);
			setLargeIconFromResource(IconNames.moveColsLeft24);
			setMnemonic(KeyEvent.VK_L);
			break;
		case COL_RIGHT:
			setName("Move column right");
			setDesc("Move column right");
			setSmallIconFromResource(IconNames.moveColsRight16);
			setLargeIconFromResource(IconNames.moveColsRight24);
			setMnemonic(KeyEvent.VK_R);
			break;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ITableModel tableModel = getTableModel();
		
		if (tableModel == null)
			return;
		
		switch (dir) {
		case ROW_UP:
			tableModel.moveRowsUp(
					tableModel.getSelectedRows());
			break;
		case ROW_DOWN:
			tableModel.moveRowsDown(
					tableModel.getSelectedRows());
			break;
		case COL_LEFT:
			tableModel.moveColsLeft(
					tableModel.getSelectedColumns());
			break;
		case COL_RIGHT:
			tableModel.moveColsRight(
					tableModel.getSelectedColumns());
			break;
		}
	}
}
