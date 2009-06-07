package org.gitools.ui.actions.table;

import java.awt.event.ActionEvent;

import org.gitools.ui.AppFrame;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.BaseAction;

import org.gitools.model.table.ITable;
import org.gitools.model.table.ITableContents;

public class ShowAllAction extends BaseAction {

	private static final long serialVersionUID = 7110623490709997414L;

	public enum ElementType {
		ROWS, COLUMNS
	}

	private ElementType type;
	
	public ShowAllAction(ElementType type) {
		super(null);
		
		this.type = type;
		switch (type) {
		case ROWS:
			setName("Show all rows");
			setDesc("Show all rows");
			setSmallIconFromResource(IconNames.rowShowAll16);
			setLargeIconFromResource(IconNames.rowShowAll24);
			break;
		case COLUMNS:
			setName("Show all columns");
			setDesc("Show all columns");
			setSmallIconFromResource(IconNames.columnShowAll16);
			setLargeIconFromResource(IconNames.columnShowAll24);
			break;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		final ITable table = getTable();
		if (table == null)
			return;
		
		final ITableContents contents = table.getContents();
		
		if (type == ElementType.ROWS) {
			int rowCount = contents.getRowCount();
			
			int[] visibleRows = new int[rowCount];
			
			for (int i = 0; i < rowCount; i++)
				visibleRows[i] = i;
			
			table.setVisibleRows(visibleRows);
			
			AppFrame.instance()
				.setStatusText(visibleRows.length + " rows showed.");
		}
		else if (type == ElementType.COLUMNS) {
			int columnCount = contents.getColumnCount();
			
			int[] visibleColumns = new int[columnCount];
			
			for (int i = 0; i < columnCount; i++)
				visibleColumns[i] = i;
			
			table.setVisibleColumns(visibleColumns);
			
			AppFrame.instance()
				.setStatusText(visibleColumns.length + " columns showed.");
		}
	}
}
