package org.gitools.ui.actions.data;

import java.awt.event.ActionEvent;

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;

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
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		final IMatrixView matrixView = ActionUtils.getMatrixView();
		if (matrixView == null)
			return;
		
		final IMatrix contents = matrixView.getContents();
		
		if (type == ElementType.ROWS) {
			int rowCount = contents.getRowCount();
			
			int[] visibleRows = new int[rowCount];
			
			for (int i = 0; i < rowCount; i++)
				visibleRows[i] = i;
			
			matrixView.setVisibleRows(visibleRows);
			
			AppFrame.instance()
				.setStatusText(visibleRows.length + " rows showed.");
		}
		else if (type == ElementType.COLUMNS) {
			int columnCount = contents.getColumnCount();
			
			int[] visibleColumns = new int[columnCount];
			
			for (int i = 0; i < columnCount; i++)
				visibleColumns[i] = i;
			
			matrixView.setVisibleColumns(visibleColumns);
			
			AppFrame.instance()
				.setStatusText(visibleColumns.length + " columns showed.");
		}
	}
}
