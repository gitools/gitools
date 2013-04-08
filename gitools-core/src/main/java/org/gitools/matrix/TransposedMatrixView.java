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
package org.gitools.matrix;

import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.IResourceLocator;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeListener;
import java.util.List;

public class TransposedMatrixView implements IMatrixView
{

    private IMatrixView mv;

    private IResourceLocator locator;

    public TransposedMatrixView()
    {
    }

    public TransposedMatrixView(IMatrixView mv)
    {
        this.mv = mv;
    }

    public TransposedMatrixView(@NotNull IMatrix mv)
    {
        setMatrix(mv);
    }

    public final void setMatrix(@NotNull IMatrix matrix)
    {
        this.mv = matrix instanceof IMatrixView ? (IMatrixView) matrix : new MatrixView(matrix);
    }

    public IResourceLocator getLocator()
    {
        return locator;
    }

    public void setLocator(IResourceLocator locator)
    {
        this.locator = locator;
    }

    @Override
    public IMatrix getContents()
    {
        return mv.getContents(); //FIXME return TransposedMatrix(mv.getContents)
    }

    @Override
    public int[] getVisibleRows()
    {
        return mv.getVisibleColumns();
    }

    @Override
    public void setVisibleRows(int[] indices)
    {
        mv.setVisibleColumns(indices);
    }

    @Override
    public int[] getVisibleColumns()
    {
        return mv.getVisibleRows();
    }

    @Override
    public void setVisibleColumns(int[] indices)
    {
        mv.setVisibleRows(indices);
    }

    @Override
    public void moveRowsUp(int[] indices)
    {
        mv.moveColumnsLeft(indices);
    }

    @Override
    public void moveRowsDown(int[] indices)
    {
        mv.moveColumnsRight(indices);
    }

    @Override
    public void moveColumnsLeft(int[] indices)
    {
        mv.moveRowsUp(indices);
    }

    @Override
    public void moveColumnsRight(int[] indices)
    {
        mv.moveRowsDown(indices);
    }

    @Override
    public void hideRows(int[] indices)
    {
        mv.hideColumns(indices);
    }

    @Override
    public void hideColumns(int[] indices)
    {
        mv.hideRows(indices);
    }

    @Override
    public int[] getSelectedRows()
    {
        return mv.getSelectedColumns();
    }

    @Override
    public void setSelectedRows(int[] indices)
    {
        mv.setSelectedColumns(indices);
    }

    @Override
    public boolean isRowSelected(int index)
    {
        return mv.isColumnSelected(index);
    }

    @Override
    public int[] getSelectedColumns()
    {
        return mv.getSelectedRows();
    }

    @Override
    public void setSelectedColumns(int[] indices)
    {
        mv.setSelectedRows(indices);
    }

    @Override
    public boolean isColumnSelected(int index)
    {
        return mv.isRowSelected(index);
    }

    @Override
    public void selectAll()
    {
        mv.selectAll(); //FIXME by columns
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
        return mv.getLeadSelectionColumn();
    }

    @Override
    public int getLeadSelectionColumn()
    {
        return mv.getLeadSelectionRow();
    }

    @Override
    public void setLeadSelection(int row, int column)
    {
        mv.setLeadSelection(column, row);
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
    public void init()
    {
        mv.init();
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
        return mv.getColumnCount();
    }

    @Override
    public int getColumnCount()
    {
        return mv.getRowCount();
    }

    @Override
    public String getRowLabel(int index)
    {
        return mv.getColumnLabel(index);
    }

    @Override
    public int getRowIndex(String label)
    {
        return mv.getRowIndex(label);
    }

    @Override
    public String getColumnLabel(int index)
    {
        return mv.getRowLabel(index);
    }

    @Override
    public int getColumnIndex(String label)
    {
        return mv.getColumnIndex(label);
    }

    @Override
    public Object getCell(int row, int column)
    {
        return mv.getCell(column, row);
    }

    @Override
    public Object getCellValue(int row, int column, int index)
    {
        return mv.getCellValue(column, row, index);
    }

    @Override
    public Object getCellValue(int row, int column, String id)
    {
        return mv.getCellValue(column, row, id);
    }

    @Override
    public void setCellValue(int row, int column, int index, Object value)
    {
        mv.setCellValue(column, row, index, value);
    }

    @Override
    public void setCellValue(int row, int column, String id, Object value)
    {
        mv.setCellValue(column, row, id, value);
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
