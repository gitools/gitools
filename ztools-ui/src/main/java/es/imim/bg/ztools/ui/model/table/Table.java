package es.imim.bg.ztools.ui.model.table;

import java.util.Arrays;

import es.imim.bg.ztools.model.elements.ElementFacade;
import es.imim.bg.ztools.model.elements.ElementProperty;
import es.imim.bg.ztools.ui.model.AbstractModel;

public class Table
		extends AbstractModel
		implements ITable {

	protected ITableContents contents;
	
	protected int[] visibleRows;
	protected int[] visibleColumns;
	
	protected int[] selectedRows;
	protected int[] selectedColumns;
	
	protected int selectionLeadRow;
	protected int selectionLeadColumn;
	
	protected int currentProperty;
	
	protected ITableDecoratorContext cellDecorationContext;
	
	public Table(ITableContents contents) {
		this.contents = contents;
		
		// initialize visible rows and columns
		
		visibleRows = new int[contents.getRowCount()];
		for (int i = 0; i < contents.getRowCount(); i++)
			visibleRows[i] = i;
		
		visibleColumns = new int[contents.getColumnCount()];
		for (int i = 0; i < contents.getColumnCount(); i++)
			visibleColumns[i] = i;
		
		// initialize selection
		
		selectedRows = new int[0];
		selectedColumns = new int[0];
		
		selectionLeadRow = selectionLeadColumn = -1;
		
		// current property
		
		ElementFacade cellsFacade = contents.getCellsFacade();
		for (int i = 0; i < cellsFacade.getPropertyCount(); i++) {
			ElementProperty prop = cellsFacade.getProperty(i);
			if ("right-p-value".equals(prop.getId()))
					currentProperty = i;
		}
		
		// cells decoration context
		
		cellDecorationContext = null;
	}
	
	/* visibility */
	
	@Override
	public ITableContents getContents() {
		return contents;
	}

	@Override
	public int[] getVisibleRows() {
		return visibleRows;
	}

	@Override
	public void setVisibleRows(int[] indices) {
		this.visibleRows = indices;
		firePropertyChange(VISIBLE_ROWS_CHANGED);
	}

	@Override
	public int[] getVisibleColumns() {
		return visibleColumns;
	}

	@Override
	public void setVisibleColumns(int[] indices) {
		this.visibleColumns = indices;
		firePropertyChange(VISIBLE_COLUMNS_CHANGED);
	}
	
	@Override
	public void moveRowsUp(int[] indices) {
		arrayMoveLeft(visibleRows, indices, selectedRows);
		firePropertyChange(VISIBLE_ROWS_CHANGED);
		firePropertyChange(SELECTION_CHANGED);
	}
	
	@Override
	public void moveRowsDown(int[] indices) {
		arrayMoveRight(visibleRows, indices, selectedRows);
		firePropertyChange(VISIBLE_ROWS_CHANGED);
		firePropertyChange(SELECTION_CHANGED);
	}
	
	@Override
	public void moveColumnsLeft(int[] indices) {
		arrayMoveLeft(visibleColumns, indices, selectedColumns);
		firePropertyChange(VISIBLE_COLUMNS_CHANGED);
		firePropertyChange(SELECTION_CHANGED);
	}

	@Override
	public void moveColumnsRight(int[] indices) {
		arrayMoveRight(visibleColumns, indices, selectedColumns);
		firePropertyChange(VISIBLE_COLUMNS_CHANGED);
		firePropertyChange(SELECTION_CHANGED);
	}
	
	/* selection */
	
	@Override
	public int[] getSelectedRows() {
		return selectedRows;
	}
	
	@Override
	public void setSelectedRows(int[] indices) {
		this.selectedRows = indices;
		firePropertyChange(SELECTION_CHANGED);
	}
	
	@Override
	public int[] getSelectedColumns() {
		return selectedColumns;
	}
	
	@Override
	public void setSelectedColumns(int[] indices) {
		this.selectedColumns = indices;
		firePropertyChange(SELECTION_CHANGED);
	}
	
	@Override
	public void selectAll() {
		selectedRows = new int[getRowCount()];
		for (int i = 0; i < getRowCount(); i++)
			selectedRows[i] = i;
		selectedColumns = new int[0];
		firePropertyChange(SELECTION_CHANGED);
	}
	
	@Override
	public void invertSelection() {
		if (selectedRows.length == 0 && selectedColumns.length == 0)
			selectAll();
		else if (selectedRows.length == 0)
			setSelectedColumns(invertSelectionArray(
					selectedColumns, getColumnCount()));
		else if (selectedColumns.length == 0)
			setSelectedRows(invertSelectionArray(
					selectedRows, getRowCount()));
	}
	
	@Override
	public void clearSelection() {
		selectedColumns = new int[0];
		selectedRows = new int[0];
		firePropertyChange(SELECTION_CHANGED);
	}
	
	private int[] invertSelectionArray(int[] array, int count) {
		int size = count - array.length;
		int[] invArray = new int[size];
		
		int j = 0;
		int lastIndex = 0;
		for (int i = 0; i < array.length; i++) {
			while (lastIndex < array[i])
				invArray[j++] = lastIndex++;
			lastIndex = array[i];
		}
		while (lastIndex < count)
			invArray[j++] = lastIndex++;
		
		return invArray;
	}

	@Override
	public int getSelectionLeadRow() {
		return selectionLeadRow;
	}
	
	@Override
	public int getSelectionLeadColumn() {
		return selectionLeadColumn;
	}
	
	@Override
	public void setLeadSelection(int row, int column) {
		this.selectionLeadRow = row;
		this.selectionLeadColumn = column;
		firePropertyChange(SELECTED_LEAD_CHANGED);
	}

	/*@Override
	public int getCurrentProperty() {
		return currentProperty;
	}

	@Override
	public void setCurrentProperty(int index) {
		this.currentProperty = index;
	}*/
	
	@Override
	public ITableDecoratorContext getCellDecoratorContext() {
		return cellDecorationContext;
	}
	
	@Override
	public void setCellDecoratorContext(ITableDecoratorContext decoratorContext) {
		this.cellDecorationContext = decoratorContext;
	}

	/* ITableContents */
	
	@Override
	public int getRowCount() {
		return visibleRows.length;
	}
	
	@Override
	public Object getRow(int index) {
		return contents.getRow(visibleRows[index]);
	}

	@Override
	public ElementFacade getRowsFacade() {
		return contents.getRowsFacade();
	}
	
	@Override
	public int getColumnCount() {
		return visibleColumns.length;
	}
	
	@Override
	public Object getColumn(int index) {
		return contents.getColumn(visibleColumns[index]);
	}

	@Override
	public ElementFacade getColumnsFacade() {
		return contents.getColumnsFacade();
	}
	
	@Override
	public Object getCell(int row, int column) {
		return contents.getCell(
				visibleRows[row], 
				visibleColumns[column]);
	}

	@Override
	public Object getCellValue(int row, int column, int index) {
		return contents.getCellValue(
				visibleRows[row], 
				visibleColumns[column],
				index);
	}

	@Override
	public Object getCellValue(int row, int column, String id) {
		return contents.getCellValue(
				visibleRows[row], 
				visibleColumns[column],
				id);
	}

	@Override
	public void setCellValue(int row, int column, int index, Object value) {
		contents.setCellValue(row, column, index, value);
	}

	@Override
	public void setCellValue(int row, int column, String id, Object value) {
		contents.setCellValue(row, column, id, value);
	}
	
	@Override
	public ElementFacade getCellsFacade() {
		return contents.getCellsFacade();
	}
	
	// internal operations
	
	private void arrayMoveLeft(
			int[] array, int[] indices, int[] selection) {
		
		if (indices.length == 0 || indices[0] == 0)
			return;
		
		Arrays.sort(indices);
		
		for (int idx : indices) {
			int tmp = array[idx - 1];
			array[idx - 1] = array[idx];
			array[idx] = tmp;
		}
		
		for (int i = 0; i < selection.length; i++)
			selection[i]--;
	}
	
	private void arrayMoveRight(
			int[] array, int[] indices, int[] selection) {
		
		if (indices.length == 0 
				|| indices[indices.length - 1] == array.length - 1)
				return;
			
		Arrays.sort(indices);
		
		for (int i = indices.length - 1; i >= 0; i--) {
			int idx = indices[i];
			int tmp = array[idx + 1];
			array[idx + 1] = array[idx];
			array[idx] = tmp;
		}
		
		for (int i = 0; i < selection.length; i++)
			selection[i]++;
	}
}
