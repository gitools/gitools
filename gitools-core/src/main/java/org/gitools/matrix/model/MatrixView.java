/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.matrix.model;

import org.apache.commons.lang.ArrayUtils;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.AbstractModel;
import org.gitools.model.xml.IndexArrayXmlAdapter;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.ResourceReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixView extends AbstractModel implements Serializable, IMatrixView
{

    private static final long serialVersionUID = -8602409555044803568L;

    private static final int INT_BIT_SIZE = 32;

    @XmlElement
    private ResourceReference<IMatrix> contents;

    @XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
    private int[] visibleRows;

    @XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
    private int[] visibleColumns;

    //@XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
    @XmlTransient
    private int[] selectedRows;

    //@XmlJavaTypeAdapter(IndexArrayXmlAdapter.class)
    @XmlTransient
    private int[] selectedColumns;

    @XmlTransient
    private int selectionLeadRow;

    @XmlTransient
    private int selectionLeadColumn;

    private int selectedPropertyIndex;

    @XmlTransient
    private int[] selectedColumnsBitmap;

    @XmlTransient
    private int[] selectedRowsBitmap;

    @XmlTransient
    private IResourceLocator locator;

    public MatrixView()
    {
        visibleRows = new int[0];
        visibleColumns = new int[0];
        selectedRows = new int[0];
        selectedColumns = new int[0];
        selectedColumnsBitmap = new int[0];
        selectedRowsBitmap = new int[0];
        selectionLeadRow = selectionLeadColumn = -1;
    }

    public MatrixView(@NotNull IMatrix contents)
    {
        initFromMatrix(contents, true);
    }

    public MatrixView(@NotNull IMatrixView matrixView)
    {
        if (matrixView instanceof MatrixView)
        {
            this.contents = new ResourceReference<IMatrix>("contents", matrixView.getContents());
            this.visibleRows = ArrayUtils.clone(matrixView.getVisibleRows());
            this.visibleColumns = ArrayUtils.clone(matrixView.getVisibleColumns());
            this.selectedRows = ArrayUtils.clone(matrixView.getSelectedRows());
            this.selectedColumns = ArrayUtils.clone(matrixView.getSelectedColumns());
            this.selectionLeadRow = matrixView.getLeadSelectionRow();
            this.selectionLeadColumn = matrixView.getLeadSelectionColumn();
            this.selectedPropertyIndex = matrixView.getSelectedPropertyIndex();
        }
        else
        {
            initFromMatrix(matrixView.getContents(), true);
        }

        selectedColumnsBitmap = newSelectionBitmap(contents.get().getColumnCount());
        selectedRowsBitmap = newSelectionBitmap(contents.get().getRowCount());
    }

    public IResourceLocator getLocator()
    {
        return locator;
    }

    public void setLocator(IResourceLocator locator)
    {
        this.locator = locator;
    }

    public void init()
    {
        initFromMatrix(contents.get(), false);
    }

    private void initFromMatrix(@NotNull IMatrix contents, boolean visible)
    {
        this.contents = new ResourceReference<IMatrix>("contents", contents);

        // initialize visible rows and columns
        if (visible)
        {
            visibleRows = new int[contents.getRowCount()];
            for (int i = 0; i < contents.getRowCount(); i++)
                visibleRows[i] = i;

            visibleColumns = new int[contents.getColumnCount()];
            for (int i = 0; i < contents.getColumnCount(); i++)
                visibleColumns[i] = i;
        }


        // initialize selection
        selectedRows = new int[0];
        selectedColumns = new int[0];

        selectedColumnsBitmap = newSelectionBitmap(contents.getColumnCount());
        selectedRowsBitmap = newSelectionBitmap(contents.getRowCount());

        selectionLeadRow = selectionLeadColumn = -1;

        // selected property
        /*
        int i = 0;
        for (IElementAttribute attr : contents.getCellAttributes())
        {
            if ("right-p-value".equals(attr.getId()) || "p-value".equals(attr.getId()))
            {
                selectedPropertyIndex = i;
                break;
            }
            i++;
        }  */
    }

	/* visibility */

    @Override
    public IMatrix getContents()
    {
        return contents.get();
    }

    @Override
    public int[] getVisibleRows()
    {
        return visibleRows;
    }

    @Override
    public void setVisibleRows(@NotNull int[] indices)
    {
        setVisibleRows(indices, true);
    }

    void setVisibleRows(@NotNull int[] indices, boolean updateLead)
    {
        // update selection according to new visibility
        int[] selection = selectionFromVisible(selectedRowsBitmap, indices);

        int nextLeadRow = -1;
        final int leadRow = selectionLeadRow >= 0 ? visibleRows[selectionLeadRow] : -1;

        if (updateLead)
        {
            for (int i = 0; i < indices.length && nextLeadRow == -1; i++)
                if (indices[i] == leadRow)
                {
                    nextLeadRow = i;
                }
        }

        this.visibleRows = indices;
        firePropertyChange(VISIBLE_ROWS_CHANGED);

        setSelectedRows(selection);

        if (updateLead)
        {
            setLeadSelection(nextLeadRow, selectionLeadColumn);
        }
    }

    @Override
    public int[] getVisibleColumns()
    {
        return visibleColumns;
    }

    @Override
    public void setVisibleColumns(@NotNull int[] indices)
    {
        setVisibleColumns(indices, true);
    }

    void setVisibleColumns(@NotNull int[] indices, boolean updateLead)
    {
        // update selection according to new visibility
        int[] selection = selectionFromVisible(selectedColumnsBitmap, indices);

        int nextLeadColumn = -1;
        final int leadColumn = selectionLeadColumn >= 0 ? visibleColumns[selectionLeadColumn] : -1;

        if (updateLead)
        {
            for (int i = 0; i < indices.length && nextLeadColumn == -1; i++)
                if (indices[i] == leadColumn)
                {
                    nextLeadColumn = i;
                }
        }

        this.visibleColumns = indices;
        firePropertyChange(VISIBLE_COLUMNS_CHANGED);

        setSelectedColumns(selection);

        if (updateLead)
        {
            setLeadSelection(selectionLeadRow, nextLeadColumn);
        }
    }

    @Override
    public void moveRowsUp(@Nullable int[] indices)
    {
        if (indices != null && indices.length > 0)
        {
            Arrays.sort(indices);
            if (indices[0] > 0 && Arrays.binarySearch(indices, selectionLeadRow) >= 0)
            {
                selectionLeadRow--;
            }
        }

        arrayMoveLeft(visibleRows, indices, selectedRows);
        firePropertyChange(VISIBLE_ROWS_CHANGED);
        firePropertyChange(SELECTION_CHANGED);
    }

    @Override
    public void moveRowsDown(@Nullable int[] indices)
    {
        if (indices != null && indices.length > 0)
        {
            Arrays.sort(indices);
            if (indices[indices.length - 1] < contents.get().getRowCount() - 1 && Arrays.binarySearch(indices, selectionLeadRow) >= 0)
            {
                selectionLeadRow++;
            }
        }

        arrayMoveRight(visibleRows, indices, selectedRows);
        firePropertyChange(VISIBLE_ROWS_CHANGED);
        firePropertyChange(SELECTION_CHANGED);
    }

    @Override
    public void moveColumnsLeft(@Nullable int[] indices)
    {
        if (indices != null && indices.length > 0)
        {
            Arrays.sort(indices);
            if (indices[0] > 0 && Arrays.binarySearch(indices, selectionLeadColumn) >= 0)
            {
                selectionLeadColumn--;
            }
        }

        arrayMoveLeft(visibleColumns, indices, selectedColumns);
        firePropertyChange(VISIBLE_COLUMNS_CHANGED);
        firePropertyChange(SELECTION_CHANGED);
    }

    @Override
    public void moveColumnsRight(@Nullable int[] indices)
    {
        if (indices != null && indices.length > 0)
        {
            Arrays.sort(indices);
            if (indices[indices.length - 1] < contents.get().getColumnCount() - 1 && Arrays.binarySearch(indices, selectionLeadColumn) >= 0)
            {
                selectionLeadColumn++;
            }
        }

        arrayMoveRight(visibleColumns, indices, selectedColumns);
        firePropertyChange(VISIBLE_COLUMNS_CHANGED);
        firePropertyChange(SELECTION_CHANGED);
    }

    @Override
    public void hideRows(int[] indices)
    {
        int[] rows = getVisibleRows();

        int[] sel = indices;
        if (sel == null || sel.length == 0)
        {
            if (selectionLeadRow != -1)
            {
                sel = new int[]{selectionLeadRow};
            }
            else
            {
                sel = new int[0];
            }
        }
        else
        {
            Arrays.sort(sel);
        }

        int nextLead = sel.length > 0 ? sel[0] : -1;

        int[] vrows = new int[rows.length - sel.length];

        int i = 0;
        int j = 0;
        int k = 0;
        while (i < rows.length && j < sel.length)
        {
            if (i != sel[j])
            {
                vrows[k++] = rows[i];
            }
            else
            {
                j++;
            }

            i++;
        }

        while (i < rows.length)
            vrows[k++] = rows[i++];

        int count = vrows.length - 1;
        if (nextLead > count)
        {
            nextLead = count;
        }

        this.selectionLeadRow = nextLead;

        setVisibleRows(vrows, false);

        firePropertyChange(SELECTED_LEAD_CHANGED);
    }

    @Override
    public void hideColumns(int[] indices)
    {
        int[] columns = getVisibleColumns();

        int[] sel = indices;
        if (sel == null || sel.length == 0)
        {
            if (selectionLeadColumn != -1)
            {
                sel = new int[]{selectionLeadColumn};
            }
            else
            {
                sel = new int[0];
            }
        }
        else
        {
            Arrays.sort(sel);
        }

        int nextLead = sel.length > 0 ? sel[0] : -1;

        int[] vcolumns = new int[columns.length - sel.length];

        int i = 0;
        int j = 0;
        int k = 0;
        while (i < columns.length && j < sel.length)
        {
            if (i != sel[j])
            {
                vcolumns[k++] = columns[i];
            }
            else
            {
                j++;
            }

            i++;
        }

        while (i < columns.length)
            vcolumns[k++] = columns[i++];

        int count = vcolumns.length - 1;
        if (nextLead > count)
        {
            nextLead = count;
        }

        this.selectionLeadColumn = nextLead;

        setVisibleColumns(vcolumns, false);

        firePropertyChange(SELECTED_LEAD_CHANGED);
    }

	/* selection */

    @Override
    public int[] getSelectedRows()
    {
        return selectedRows;
    }

    @Override
    public void setSelectedRows(@NotNull int[] indices)
    {
        this.selectedRows = indices;
        updateSelectionBitmap(selectedRowsBitmap, indices, visibleRows);
        firePropertyChange(SELECTION_CHANGED);
    }

    @Override
    public boolean isRowSelected(int index)
    {
        if (index >= visibleRows.length)
        {
            return false;
        }
        else
        {
            return checkSelectionBitmap(selectedRowsBitmap, visibleRows[index]);
        }
    }

    @Override
    public int[] getSelectedColumns()
    {
        return selectedColumns;
    }

    @Override
    public void setSelectedColumns(@NotNull int[] indices)
    {
        this.selectedColumns = indices;
        updateSelectionBitmap(selectedColumnsBitmap, indices, visibleColumns);
        firePropertyChange(SELECTION_CHANGED);
    }

    @Override
    public boolean isColumnSelected(int index)
    {
        if (index >= visibleColumns.length)
        {
            return false;
        }
        else
        {
            return checkSelectionBitmap(selectedColumnsBitmap, visibleColumns[index]);
        }
    }

    @Override
    public void selectAll()
    {
        if (selectionLeadRow == -1 && selectionLeadColumn != -1)
        {
            selectedColumns = new int[getColumnCount()];
            for (int i = 0; i < getColumnCount(); i++)
                selectedColumns[i] = i;
            selectedRows = new int[0];

            Arrays.fill(selectedRowsBitmap, 0);
            Arrays.fill(selectedColumnsBitmap, -1);
        }
        else
        {
            selectedRows = new int[getRowCount()];
            for (int i = 0; i < getRowCount(); i++)
                selectedRows[i] = i;
            selectedColumns = new int[0];

            Arrays.fill(selectedRowsBitmap, -1);
            Arrays.fill(selectedColumnsBitmap, 0);
        }

        firePropertyChange(SELECTION_CHANGED);
    }

    @Override
    public void invertSelection()
    {
        if (selectedRows.length == 0 && selectedColumns.length == 0)
        {
            selectAll();
        }
        else if (selectedRows.length == 0)
        {
            setSelectedColumns(invertSelectionArray(selectedColumns, getColumnCount()));
        }
        else if (selectedColumns.length == 0)
        {
            setSelectedRows(invertSelectionArray(selectedRows, getRowCount()));
        }
    }

    @Override
    public void clearSelection()
    {
        selectedColumns = new int[0];
        selectedRows = new int[0];

        Arrays.fill(selectedRowsBitmap, 0);
        Arrays.fill(selectedColumnsBitmap, 0);

        firePropertyChange(SELECTION_CHANGED);
    }

    @Override
    public int getLeadSelectionRow()
    {
        return selectionLeadRow;
    }

    @Override
    public int getLeadSelectionColumn()
    {
        return selectionLeadColumn;
    }

    @Override
    public void setLeadSelection(int row, int column)
    {
        boolean changed = this.selectionLeadRow != row || this.selectionLeadColumn != column;

        this.selectionLeadRow = row;
        this.selectionLeadColumn = column;
        if (changed)
        {
            firePropertyChange(SELECTED_LEAD_CHANGED);
        }
    }

    @Override
    public int getSelectedPropertyIndex()
    {
        return selectedPropertyIndex;
    }

    @Override
    public void setSelectedPropertyIndex(int index)
    {
        this.selectedPropertyIndex = index;
    }

	/* IMatrix */

    @Override
    public int getRowCount()
    {
        return visibleRows.length;
    }

    @Override
    public String getRowLabel(int index)
    {
        return contents.get().getRowLabel(visibleRows[index]);
    }

    @Override
    public int getRowIndex(String label)
    {
        return contents.get().getRowIndex(label);
    }

    @Override
    public int getColumnCount()
    {
        return visibleColumns.length;
    }

    @Override
    public String getColumnLabel(int index)
    {
        return contents.get().getColumnLabel(visibleColumns[index]);
    }

    @Override
    public int getColumnIndex(String label)
    {
        return contents.get().getColumnIndex(label);
    }

    @Override
    public Object getCell(int row, int column)
    {
        return contents.get().getCell(visibleRows[row], visibleColumns[column]);
    }

    @Override
    public Object getCellValue(int row, int column, int index)
    {
        return contents.get().getCellValue(visibleRows[row], visibleColumns[column], index);
    }

    @Override
    public Object getCellValue(int row, int column, String id)
    {
        return contents.get().getCellValue(visibleRows[row], visibleColumns[column], id);
    }

    @Override
    public void setCellValue(int row, int column, int index, Object value)
    {
        contents.get().setCellValue(visibleRows[row], visibleColumns[column], index, value);
    }

    @Override
    public void setCellValue(int row, int column, String id, Object value)
    {
        contents.get().setCellValue(visibleRows[row], visibleColumns[column], id, value);
    }

    @Override
    public IElementAdapter getCellAdapter()
    {
        return contents.get().getCellAdapter();
    }

    @Override
    public List<IElementAttribute> getCellAttributes()
    {
        return contents.get().getCellAttributes();
    }

    @Override
    public int getCellAttributeIndex(String id)
    {
        return contents.get().getCellAttributeIndex(id);
    }

    // internal operations

    private void arrayMoveLeft(int[] array, @NotNull int[] indices, @NotNull int[] selection)
    {

        if (indices.length == 0 || indices[0] == 0)
        {
            return;
        }

        Arrays.sort(indices);

        for (int idx : indices)
        {
            int tmp = array[idx - 1];
            array[idx - 1] = array[idx];
            array[idx] = tmp;
        }

        for (int i = 0; i < selection.length; i++)
            selection[i]--;
    }

    private void arrayMoveRight(@NotNull int[] array, @NotNull int[] indices, @NotNull int[] selection)
    {

        if (indices.length == 0 || indices[indices.length - 1] == array.length - 1)
        {
            return;
        }

        Arrays.sort(indices);

        for (int i = indices.length - 1; i >= 0; i--)
        {
            int idx = indices[i];
            int tmp = array[idx + 1];
            array[idx + 1] = array[idx];
            array[idx] = tmp;
        }

        for (int i = 0; i < selection.length; i++)
            selection[i]++;
    }

    @NotNull
    private int[] invertSelectionArray(@NotNull int[] array, int count)
    {
        int size = count - array.length;
        int[] invArray = new int[size];

        int j = 0;
        int lastIndex = 0;
        for (int i = 0; i < array.length; i++)
        {
            while (lastIndex < array[i])
                invArray[j++] = lastIndex++;
            lastIndex = array[i] + 1;
        }
        while (lastIndex < count)
            invArray[j++] = lastIndex++;

        return invArray;
    }

    @NotNull
    private int[] selectionFromVisible(int[] bitmap, @NotNull int[] visible)
    {
        int selectionCount = 0;
        int[] selectionBuffer = new int[visible.length];
        for (int i = 0; i < visible.length; i++)
            if (checkSelectionBitmap(bitmap, visible[i]))
            {
                selectionBuffer[selectionCount++] = i;
            }

        int[] selection = new int[selectionCount];
        System.arraycopy(selectionBuffer, 0, selection, 0, selectionCount);
        return selection;
    }

    @NotNull
    private int[] newSelectionBitmap(int size)
    {
        int[] a = new int[(size + INT_BIT_SIZE - 1) / INT_BIT_SIZE];
        Arrays.fill(a, 0);
        return a;
    }

    private void updateSelectionBitmap(@NotNull int[] bitmap, @NotNull int[] indices, int[] visible)
    {
        Arrays.fill(bitmap, 0);
        for (int visibleIndex : indices)
        {
            int index = visible[visibleIndex];
            int bindex = index / INT_BIT_SIZE;
            int bit = 1 << (index % INT_BIT_SIZE);
            bitmap[bindex] |= bit;
        }
    }

    private boolean checkSelectionBitmap(int[] bitmap, int index)
    {
        int bindex = index / INT_BIT_SIZE;
        int bit = 1 << (index % INT_BIT_SIZE);
        return (bitmap[bindex] & bit) != 0;
    }

    public void visibleColumnsFromSelection()
    {
        int[] sel = getSelectedColumns();
        int[] view = getVisibleColumns();
        int[] newview = new int[sel.length];

        for (int i = 0; i < newview.length; i++)
            newview[i] = view[sel[i]];

        setVisibleColumns(newview);
    }

    public void visibleRowsFromSelection()
    {
        int[] sel = getSelectedRows();
        int[] view = getVisibleRows();
        int[] newview = new int[sel.length];

        for (int i = 0; i < newview.length; i++)
            newview[i] = view[sel[i]];

        setVisibleRows(newview);
    }
}
