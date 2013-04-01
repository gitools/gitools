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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiagonalMatrixView implements IMatrixView
{

    private IMatrixView mv;

    private PropertyChangeListener listener;

    private List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();

    private IResourceLocator locator;

    public DiagonalMatrixView(IMatrix m)
    {
        listener = new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                DiagonalMatrixView.this.propertyChange(evt);
            }
        };

        setMatrix(m);
    }

    public DiagonalMatrixView(IMatrixView mv)
    {
        listener = new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                DiagonalMatrixView.this.propertyChange(evt);
            }
        };

        setMatrixView(mv);
    }

    public IResourceLocator getLocator()
    {
        return locator;
    }

    public void setLocator(IResourceLocator locator)
    {
        this.locator = locator;
    }

    public final void setMatrix(IMatrix matrix)
    {
        IMatrixView mview = matrix instanceof IMatrixView ?
                (IMatrixView) matrix : new MatrixView(matrix);
        setMatrixView(mview);
    }

    private void setMatrixView(IMatrixView mv)
    {
        if (this.mv != null)
        {
            this.mv.removePropertyChangeListener(listener);
        }

        IMatrix m = mv.getContents();
        if (!(m instanceof DiagonalMatrix))
        {
            m = new DiagonalMatrix(m);
        }

        this.mv = new MatrixView(m);

        this.mv.addPropertyChangeListener(listener);
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
        int[] selection = mv.getSelectedRows();
        mv.setVisibleColumns(indices);
        mv.setSelectedRows(selection);
        indices = Arrays.copyOf(indices, indices.length);
        mv.setVisibleRows(indices);
    }

    @Override
    public int[] getVisibleColumns()
    {
        return mv.getVisibleRows();
    }

    @Override
    public void setVisibleColumns(int[] indices)
    {
        int[] selection = mv.getSelectedColumns();
        mv.setVisibleRows(indices);
        mv.setSelectedColumns(selection);
        indices = Arrays.copyOf(indices, indices.length);
        mv.setVisibleColumns(indices);
    }

    @Override
    public void moveRowsUp(int[] indices)
    {
        int[] selection = mv.getSelectedRows();
        mv.moveColumnsLeft(indices);
        mv.setSelectedRows(selection);
        mv.moveRowsUp(indices);
    }

    @Override
    public void moveRowsDown(int[] indices)
    {
        int[] selection = mv.getSelectedRows();
        mv.moveColumnsRight(indices);
        mv.setSelectedRows(selection);
        mv.moveRowsDown(indices);
    }

    @Override
    public void moveColumnsLeft(int[] indices)
    {
        int[] selection = mv.getSelectedColumns();
        mv.moveRowsUp(indices);
        mv.setSelectedColumns(selection);
        mv.moveColumnsLeft(indices);
    }

    @Override
    public void moveColumnsRight(int[] indices)
    {
        int[] selection = mv.getSelectedColumns();
        mv.moveRowsDown(indices);
        mv.setSelectedColumns(selection);
        mv.moveColumnsRight(indices);
    }

    @Override
    public void hideRows(int[] indices)
    {
        mv.hideRows(indices);
        mv.hideColumns(indices);
    }

    @Override
    public void hideColumns(int[] indices)
    {
        hideRows(indices);
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
    public int getRowCount()
    {
        return mv.getRowCount();
    }

    @Override
    public String getRowLabel(int index)
    {
        return mv.getRowLabel(index);
    }

    @Override
    public int getColumnCount()
    {
        return mv.getColumnCount();
    }

    @Override
    public String getColumnLabel(int index)
    {
        return mv.getColumnLabel(index);
    }

    @Override
    public Object getCell(int row, int column)
    {
        return column >= row ? mv.getCell(row, column) : null;
    }

    @Override
    public Object getCellValue(int row, int column, int index)
    {
        return column >= row ? mv.getCellValue(row, column, index) : null;
    }

    @Override
    public Object getCellValue(int row, int column, String id)
    {
        return column >= row ? mv.getCellValue(row, column, id) : null;
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

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        if (listener != null)
        {
            listeners.add(listener);
        }
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        if (listener != null)
        {
            listeners.remove(listener);
        }
    }

    private void propertyChange(PropertyChangeEvent evt)
    {
        PropertyChangeEvent evt2 = new PropertyChangeEvent(this,
                evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());

        for (PropertyChangeListener l : listeners)
            l.propertyChange(evt2);
    }

}
