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
package org.gitools.heatmap;

import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.IResourceLocator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeListener;
import java.util.List;

public class HeatmapAnnotatedMatrixView implements IMatrixView
{

    @NotNull
    private final Heatmap hm;
    @Nullable
    private final IMatrixView mv;

    private IResourceLocator locator;

    public HeatmapAnnotatedMatrixView(@NotNull Heatmap hm)
    {
        this.hm = hm;
        this.mv = hm.getMatrixView();
    }

    @Override
    public IResourceLocator getLocator()
    {
        return locator;
    }

    @Override
    public void setLocator(IResourceLocator locator)
    {
        this.locator = locator;
    }

    @Override
    public IMatrix getContents()
    {
        return mv.getContents();
    }

    @Override
    public int[] getVisibleRows()
    {
        return mv.getVisibleRows();
    }

    @Override
    public void setVisibleRows(int[] indices)
    {
        mv.setVisibleRows(indices);
    }

    @Override
    public int[] getVisibleColumns()
    {
        return mv.getVisibleColumns();
    }

    @Override
    public void setVisibleColumns(int[] indices)
    {
        mv.setVisibleColumns(indices);
    }

    @Override
    public void moveRowsUp(int[] indices)
    {
        mv.moveRowsUp(indices);
    }

    @Override
    public void moveRowsDown(int[] indices)
    {
        mv.moveRowsDown(indices);
    }

    @Override
    public void moveColumnsLeft(int[] indices)
    {
        mv.moveColumnsLeft(indices);
    }

    @Override
    public void moveColumnsRight(int[] indices)
    {
        mv.moveColumnsRight(indices);
    }

    @Override
    public void hideRows(int[] indices)
    {
        mv.hideRows(indices);
    }

    @Override
    public void hideColumns(int[] indices)
    {
        mv.hideColumns(indices);
    }

    @Override
    public int[] getSelectedRows()
    {
        return mv.getSelectedRows();
    }

    @Override
    public void setSelectedRows(int[] indices)
    {
        mv.setSelectedRows(indices);
    }

    @Override
    public boolean isRowSelected(int index)
    {
        return mv.isRowSelected(index);
    }

    @Override
    public int[] getSelectedColumns()
    {
        return mv.getSelectedColumns();
    }

    @Override
    public void setSelectedColumns(int[] indices)
    {
        mv.setSelectedColumns(indices);
    }

    @Override
    public boolean isColumnSelected(int index)
    {
        return mv.isColumnSelected(index);
    }

    @Override
    public void selectAll()
    {
        mv.selectAll();
    }

    @Override
    public void invertSelection()
    {
        mv.invertSelection();
    }

    @Override
    public void clearSelection()
    {
        mv.clearSelection();
    }

    @Override
    public int getLeadSelectionRow()
    {
        return mv.getLeadSelectionRow();
    }

    @Override
    public int getLeadSelectionColumn()
    {
        return mv.getLeadSelectionColumn();
    }

    @Override
    public void setLeadSelection(int row, int column)
    {
        mv.setLeadSelection(row, column);
    }

    @Override
    public int getSelectedPropertyIndex()
    {
        return mv.getSelectedPropertyIndex();
    }

    @Override
    public void setSelectedPropertyIndex(int index)
    {
        mv.setSelectedPropertyIndex(index);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        mv.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        mv.removePropertyChangeListener(listener);
    }

    @Override
    public int getRowCount()
    {
        return mv.getRowCount();
    }

    @Override
    public String getRowLabel(int index)
    {
        return hm.getRowLabel(index);
    }

    @Override
    public int getRowIndex(String label)
    {
        return mv.getRowIndex(label);
    }

    @Override
    public int getColumnCount()
    {
        return mv.getColumnCount();
    }

    @Override
    public String getColumnLabel(int index)
    {
        return hm.getColumnLabel(index);
    }

    @Override
    public int getColumnIndex(String label)
    {
        return mv.getColumnIndex(label);
    }

    @Override
    public Object getCell(int row, int column)
    {
        return mv.getCell(row, column);
    }

    @Override
    public Object getCellValue(int row, int column, int index)
    {
        return mv.getCellValue(row, column, index);
    }

    @Override
    public Object getCellValue(int row, int column, String id)
    {
        return mv.getCellValue(row, column, id);
    }

    @Override
    public void setCellValue(int row, int column, int index, Object value)
    {
        mv.setCellValue(row, column, index, value);
    }

    @Override
    public void setCellValue(int row, int column, String id, Object value)
    {
        mv.setCellValue(row, column, id, value);
    }

    @Override
    public IElementAdapter getCellAdapter()
    {
        return mv.getCellAdapter();
    }

    @Override
    public List<IElementAttribute> getCellAttributes()
    {
        return mv.getCellAttributes();
    }

    @Override
    public int getCellAttributeIndex(String id)
    {
        return mv.getCellAttributeIndex(id);
    }
}
