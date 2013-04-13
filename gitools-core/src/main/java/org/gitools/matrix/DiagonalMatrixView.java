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

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.IMatrixViewDimension;
import org.gitools.matrix.model.IMatrixViewLayers;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.persistence.IResourceLocator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiagonalMatrixView implements IMatrixView
{

    private IMatrixView mv;

    private final PropertyChangeListener listener;

    @NotNull
    private final List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();

    private IResourceLocator locator;

    public DiagonalMatrixView(@NotNull IMatrix m)
    {
        listener = new PropertyChangeListener()
        {
            @Override
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                DiagonalMatrixView.this.propertyChange(evt);
            }
        };

        setMatrix(m);
    }

    public DiagonalMatrixView(@NotNull IMatrixView mv)
    {
        listener = new PropertyChangeListener()
        {
            @Override
            public void propertyChange(@NotNull PropertyChangeEvent evt)
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

    final void setMatrix(@NotNull IMatrix matrix)
    {
        IMatrixView mview = matrix instanceof IMatrixView ? (IMatrixView) matrix : new Heatmap(matrix);
        setMatrixView(mview);
    }

    private void setMatrixView(@NotNull IMatrixView mv)
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

        this.mv = new Heatmap(m);

        this.mv.addPropertyChangeListener(listener);
    }

    @Override
    public IMatrix getContents()
    {
        return mv.getContents();
    }

    //TODO
    public void internalRowsSetVisible(int[] indices)
    {
        int[] selection = mv.getRows().getSelected(  );
        mv.getColumns().setVisible(indices);
        mv.getRows().setSelected(  selection);
        indices = Arrays.copyOf(indices, indices.length);
        mv.getRows().setVisible(indices);
    }

    //TODO
    public void internalColumnsSetVisible(int[] indices)
    {
        int[] selection = mv.getColumns().getSelected(  );
        mv.getRows().setVisible(indices);
        mv.getColumns().setSelected(  selection);
        indices = Arrays.copyOf(indices, indices.length);
        mv.getColumns().setVisible(indices);
    }

    /*
    @Override
    public void getRows().move(org.gitools.matrix.model.Direction.UP , int[] indices)
    {
        int[] selection = mv.getRows().getSelected(  );
        mv.getColumns().move(org.gitools.matrix.model.Direction.LEFT,  indices);
        mv.getRows().setSelected(  selection);
        mv.getRows().move(org.gitools.matrix.model.Direction.UP , indices);
    }   */

    /*
    @Override
    public void getRows().move(org.gitools.matrix.model.Direction.DOWN,  int[] indices)
    {
        int[] selection = mv.getRows().getSelected(  );
        mv.getColumns().move(org.gitools.matrix.model.Direction.RIGHT,  indices);
        mv.getRows().setSelected(  selection);
        mv.getRows().move(org.gitools.matrix.model.Direction.DOWN,  indices);
    }  */

    /*
    public void getColumns().move(org.gitools.matrix.model.Direction.LEFT,  int[] indices)
    {
        int[] selection = mv.getColumns().getSelected(  );
        //mv.getRows().move(org.gitools.matrix.model.Direction.UP , indices);
        mv.getColumns().setSelected(  selection);
        mv.getColumns().move(org.gitools.matrix.model.Direction.LEFT,  indices);
    }  */

    /*
    @Override
    public void getColumns().move(org.gitools.matrix.model.Direction.RIGHT,  int[] indices)
    {
        int[] selection = mv.getColumns().getSelected(  );
        mv.getRows().move(org.gitools.matrix.model.Direction.DOWN,  indices);
        mv.getColumns().setSelected(  selection);
        mv.getColumns().move(org.gitools.matrix.model.Direction.RIGHT,  indices);
    }   */

    /*
    @Override
    public void getRows().hide(  int[] indices)
    {
        mv.getRows().hide(  indices);
        mv.getColumns().hide(  indices);
    }       */

    /*
    @Override
    public void getColumns().hide(  int[] indices)
    {
        getRows().hide(  indices);
    } */

    /*
    @Override
    public int[] getRows().getSelected(  )
    {
        return mv.getRows().getSelected(  );
    }*/

    /*
    @Override
    public void getRows().setSelected(  int[] indices)
    {
        mv.getRows().setSelected(  indices);
    }
      */

    /*
    @Override
    public boolean getRows().isSelected(  int index)
    {
        return mv.getRows().isSelected(  index);
    } */

    @Override
    public IMatrixViewDimension getRows()
    {
        return mv.getRows();
    }

    @Override
    public IMatrixViewDimension getColumns()
    {
        return mv.getColumns();
    }

    @Override
    public boolean isEmpty(int row, int column)
    {
        return mv.isEmpty(row, column);
    }


    @Nullable
    @Override
    public Object getCellValue(int row, int column, int layer)
    {
        return column >= row ? mv.getCellValue(row, column, layer) : null;
    }

    @Override
    public void setCellValue(int row, int column, int layer, Object value)
    {
        mv.setCellValue(row, column, layer, value);
    }

    @Override
    public IElementAdapter getCellAdapter()
    {
        return mv.getCellAdapter();
    }

    @Override
    public IMatrixViewLayers getLayers()
    {
        return mv.getLayers();
    }

    @Override
    public void detach()
    {
        mv.detach();
    }

    @Override
    public void addPropertyChangeListener(@Nullable PropertyChangeListener listener)
    {
        if (listener != null)
        {
            listeners.add(listener);
        }
    }

    @Override
    public void removePropertyChangeListener(@Nullable PropertyChangeListener listener)
    {
        if (listener != null)
        {
            listeners.remove(listener);
        }
    }

    private void propertyChange(@NotNull PropertyChangeEvent evt)
    {
        PropertyChangeEvent evt2 = new PropertyChangeEvent(this, evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());

        for (PropertyChangeListener l : listeners)
            l.propertyChange(evt2);
    }

}
