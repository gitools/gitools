package org.gitools.model.matrix;

import java.io.Serializable;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.AbstractModel;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.matrix.element.IElementProperty;
import org.gitools.model.xml.IndexArrayAdapter;
import org.gitools.model.xml.MatrixXmlAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType( propOrder={"contents","visibleRows", "visibleColumns","selectedRows", 
		"selectedColumns", "selectionLeadRow", "selectionLeadColumn",
		"selectedPropertyIndex"} )

public class MatrixView
		extends AbstractModel
		implements Serializable, IMatrixView {

	private static final long serialVersionUID = -8602409555044803568L;

	
	@XmlJavaTypeAdapter(MatrixXmlAdapter.class)
	protected IMatrix contents;
	
	@XmlJavaTypeAdapter(IndexArrayAdapter.class)
	protected int[] visibleRows;
	@XmlJavaTypeAdapter(IndexArrayAdapter.class)
	protected int[] visibleColumns;
	@XmlJavaTypeAdapter(IndexArrayAdapter.class)
	protected int[] selectedRows;
	@XmlJavaTypeAdapter(IndexArrayAdapter.class)
	protected int[] selectedColumns;
	
	protected int selectionLeadRow;
	protected int selectionLeadColumn;
	
	protected int selectedPropertyIndex;
	
	public MatrixView(){
		// init

		visibleRows = new int[0];
		visibleColumns = new int[0];
		selectedRows = new int[0];
		selectedColumns = new int[0];
		
		selectionLeadRow = selectionLeadColumn = -1;
	
	}
	
	
	public MatrixView(IMatrix contents) {
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
		
		// selected property
		
		IElementAdapter cellAdapter = contents.getCellAdapter();
		for (int i = 0; i < cellAdapter.getPropertyCount(); i++) {
			IElementProperty prop = cellAdapter.getProperty(i);
			if ("right-p-value".equals(prop.getId())
					|| "p-value".equals(prop.getId()))
					selectedPropertyIndex = i;
		}
	}
	
	/* visibility */
	
	@Override
	public IMatrix getContents() {
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
			lastIndex = array[i] + 1;
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

	@Override
	public int getSelectedPropertyIndex() {
		return selectedPropertyIndex;
	}

	@Override
	public void setSelectedPropertyIndex(int index) {
		this.selectedPropertyIndex = index;		
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
	public IElementAdapter getRowAdapter() {
		return contents.getRowAdapter();
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
	public IElementAdapter getColumnAdapter() {
		return contents.getColumnAdapter();
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
		contents.setCellValue(
				visibleRows[row], 
				visibleColumns[column], 
				index, value);
	}

	@Override
	public void setCellValue(int row, int column, String id, Object value) {
		contents.setCellValue(
				visibleRows[row], 
				visibleColumns[column], 
				id, value);
	}
	
	@Override
	public IElementAdapter getCellAdapter() {
		return contents.getCellAdapter();
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
