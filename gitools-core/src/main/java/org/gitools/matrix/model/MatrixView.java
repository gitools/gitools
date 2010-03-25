package org.gitools.matrix.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.AbstractModel;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.xml.IndexArrayXmlAdapter;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixView
		extends AbstractModel
		implements Serializable, IMatrixView {

	private static final long serialVersionUID = -8602409555044803568L;

	private static final int INT_BIT_SIZE = 32;

	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected IMatrix contents;
	
	@XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
	protected int[] visibleRows;
	
	@XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
	protected int[] visibleColumns;
	
	//@XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
	@XmlTransient
	protected int[] selectedRows;
	
	//@XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
	@XmlTransient
	protected int[] selectedColumns;

	@XmlTransient
	protected int selectionLeadRow;

	@XmlTransient
	protected int selectionLeadColumn;

	protected int selectedPropertyIndex;

	@XmlTransient
	private int[] selectedColumnsBitmap;

	@XmlTransient
	private int[] selectedRowsBitmap;
	
	public MatrixView() {
		visibleRows = new int[0];
		visibleColumns = new int[0];
		selectedRows = new int[0];
		selectedColumns = new int[0];
		selectedColumnsBitmap = new int[0];
		selectedRowsBitmap = new int[0];
		selectionLeadRow = selectionLeadColumn = -1;
	}
	
	public MatrixView(IMatrix contents) {
		initFromMatrix(contents);
	}

	public MatrixView(IMatrixView matrixView) {
		if (matrixView instanceof MatrixView) {
			this.contents = matrixView.getContents();
			this.visibleRows = matrixView.getVisibleRows();
			this.visibleColumns = matrixView.getVisibleColumns();
			this.selectedRows = matrixView.getSelectedRows();
			this.selectedColumns = matrixView.getSelectedColumns();
			this.selectionLeadRow = matrixView.getLeadSelectionRow();
			this.selectionLeadColumn = matrixView.getLeadSelectionColumn();
			this.selectedPropertyIndex = matrixView.getSelectedPropertyIndex();
		}
		else
			initFromMatrix(matrixView.getContents());

		selectedColumnsBitmap = newSelectionBitmap(contents.getColumnCount());
		selectedRowsBitmap = newSelectionBitmap(contents.getRowCount());
	}

	private void initFromMatrix(IMatrix contents) {
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

		selectedColumnsBitmap = newSelectionBitmap(contents.getColumnCount());
		selectedRowsBitmap = newSelectionBitmap(contents.getRowCount());

		selectionLeadRow = selectionLeadColumn = -1;

		// selected property

		int i = 0;
		for (IElementAttribute attr : contents.getCellAttributes()) {
			if ("right-p-value".equals(attr.getId())
					|| "p-value".equals(attr.getId())) {
					selectedPropertyIndex = i;
					break;
			}
			i++;
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
		// update selection according to new visibility
		int[] selection = selectionFromVisible(selectedRowsBitmap, indices);
		
		int nextLeadRow = -1;
		final int leadRow = selectionLeadRow >= 0 ? visibleRows[selectionLeadRow] : -1;

		int leadColumn = selectionLeadColumn >= 0 ? visibleColumns[selectionLeadColumn] : -1;

		for (int i = 0; i < indices.length && nextLeadRow == -1; i++)
			if (indices[i] == leadRow)
				nextLeadRow = i;

		this.visibleRows = indices;
		firePropertyChange(VISIBLE_ROWS_CHANGED);

		setSelectedRows(selection);
		setLeadSelection(nextLeadRow, selectionLeadColumn);
	}

	@Override
	public int[] getVisibleColumns() {
		return visibleColumns;
	}

	@Override
	public void setVisibleColumns(int[] indices) {
		// update selection according to new visibility
		int[] selection = selectionFromVisible(selectedColumnsBitmap, indices);

		int nextLeadColumn = -1;
		final int leadColumn = selectionLeadColumn >= 0 ? visibleColumns[selectionLeadColumn] : -1;

		for (int i = 0; i < indices.length && nextLeadColumn == -1; i++)
			if (indices[i] == leadColumn)
				nextLeadColumn = i;

		this.visibleColumns = indices;
		firePropertyChange(VISIBLE_COLUMNS_CHANGED);

		setSelectedColumns(selection);
		setLeadSelection(selectionLeadRow, nextLeadColumn);
	}
	
	@Override
	public void moveRowsUp(int[] indices) {
		if (indices != null && indices.length > 0) {
			Arrays.sort(indices);
			if (indices[0] > 0
					&& Arrays.binarySearch(indices, selectionLeadRow) >= 0)
				selectionLeadRow--;
		}

		arrayMoveLeft(visibleRows, indices, selectedRows);
		firePropertyChange(VISIBLE_ROWS_CHANGED);
		firePropertyChange(SELECTION_CHANGED);
	}
	
	@Override
	public void moveRowsDown(int[] indices) {
		if (indices != null && indices.length > 0) {
			Arrays.sort(indices);
			if (indices[indices.length - 1] < contents.getRowCount() - 1
					&& Arrays.binarySearch(indices, selectionLeadRow) >= 0)
				selectionLeadRow++;
		}

		arrayMoveRight(visibleRows, indices, selectedRows);
		firePropertyChange(VISIBLE_ROWS_CHANGED);
		firePropertyChange(SELECTION_CHANGED);
	}
	
	@Override
	public void moveColumnsLeft(int[] indices) {
		if (indices != null && indices.length > 0) {
			Arrays.sort(indices);
			if (indices[0] > 0
					&& Arrays.binarySearch(indices, selectionLeadColumn) >= 0)
				selectionLeadColumn--;
		}

		arrayMoveLeft(visibleColumns, indices, selectedColumns);
		firePropertyChange(VISIBLE_COLUMNS_CHANGED);
		firePropertyChange(SELECTION_CHANGED);
	}

	@Override
	public void moveColumnsRight(int[] indices) {
		if (indices != null && indices.length > 0) {
			Arrays.sort(indices);
			if (indices[indices.length - 1] < contents.getColumnCount() - 1
					&& Arrays.binarySearch(indices, selectionLeadColumn) >= 0)
				selectionLeadColumn++;
		}

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
		updateSelectionBitmap(selectedRowsBitmap, indices, visibleRows);
		firePropertyChange(SELECTION_CHANGED);
	}

	@Override
	public boolean isRowSelected(int index) {
		return checkSelectionBitmap(selectedRowsBitmap, visibleRows[index]);
	}

	@Override
	public int[] getSelectedColumns() {
		return selectedColumns;
	}
	
	@Override
	public void setSelectedColumns(int[] indices) {
		this.selectedColumns = indices;
		updateSelectionBitmap(selectedColumnsBitmap, indices, visibleColumns);
		firePropertyChange(SELECTION_CHANGED);
	}

	@Override
	public boolean isColumnSelected(int index) {
		return checkSelectionBitmap(selectedColumnsBitmap, visibleColumns[index]);
	}

	@Override
	public void selectAll() {
		selectedRows = new int[getRowCount()];
		for (int i = 0; i < getRowCount(); i++)
			selectedRows[i] = i;
		selectedColumns = new int[0];

		Arrays.fill(selectedRowsBitmap, -1);
		Arrays.fill(selectedColumnsBitmap, 0);

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

		Arrays.fill(selectedRowsBitmap, 0);
		Arrays.fill(selectedColumnsBitmap, 0);

		firePropertyChange(SELECTION_CHANGED);
	}
	
	@Override
	public int getLeadSelectionRow() {
		return selectionLeadRow;
	}
	
	@Override
	public int getLeadSelectionColumn() {
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

	/* IMatrix */
	
	@Override
	public int getRowCount() {
		return visibleRows.length;
	}
	
	@Override
	public String getRowLabel(int index) {
		return contents.getRowLabel(visibleRows[index]);
	}
	
	@Override
	public int getColumnCount() {
		return visibleColumns.length;
	}
	
	@Override
	public String getColumnLabel(int index) {
		return contents.getColumnLabel(visibleColumns[index]);
	}
	
	@Override
	public Object getCell(int row, int column) {
		return contents.getCell(visibleRows[row], visibleColumns[column]);
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
	
	@Override
	public List<IElementAttribute> getCellAttributes() {
		return contents.getCellAttributes();
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

	private int[] selectionFromVisible(int[] bitmap, int[] visible) {
		int selectionCount = 0;
		int[] selectionBuffer = new int[visible.length];
		for (int i = 0; i < visible.length; i++)
			if (checkSelectionBitmap(bitmap, visible[i]))
				selectionBuffer[selectionCount++] = i;

		int[] selection = new int[selectionCount];
		System.arraycopy(selectionBuffer, 0, selection, 0, selectionCount);
		return selection;
	}
	
	private int[] newSelectionBitmap(int size) {
		int[] a = new int[(size + INT_BIT_SIZE - 1) / INT_BIT_SIZE];
		Arrays.fill(a, 0);
		return a;
	}

	private void updateSelectionBitmap(int[] bitmap, int[] indices, int[] visible) {
		Arrays.fill(bitmap, 0);
		for (int visibleIndex : indices) {
			int index = visible[visibleIndex];
			int bindex = index / INT_BIT_SIZE;
			int bit = 1 << (index % INT_BIT_SIZE);
			bitmap[bindex] |= bit;
		}
	}

	private boolean checkSelectionBitmap(int[] bitmap, int index) {
		int bindex = index / INT_BIT_SIZE;
		int bit = 1 << (index % INT_BIT_SIZE);
		return (bitmap[bindex] & bit) != 0;
	}

	public void visibleColumnsFromSelection() {
		int[] sel = getSelectedColumns();
		int[] view = getVisibleColumns();
		int[] newview = new int[sel.length];

		for (int i = 0; i < newview.length; i++)
			newview[i] = view[sel[i]];

		setVisibleColumns(newview);
	}

	public void visibleRowsFromSelection() {
		int[] sel = getSelectedRows();
		int[] view = getVisibleRows();
		int[] newview = new int[sel.length];

		for (int i = 0; i < newview.length; i++)
			newview[i] = view[sel[i]];

		setVisibleRows(newview);
	}
}
