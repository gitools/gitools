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

import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.matrix.model.*;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.Resource;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.model.decorator.impl.BinaryElementDecorator;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.formats.analysis.adapter.ResourceReferenceXmlAdapter;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement()
@XmlType(propOrder = {"rows", "columns", "data", "layers"})
public class Heatmap extends Resource implements IMatrixView
{

    public static final String CELL_DECORATOR_CHANGED = "cellDecorator";
    public static final String VALUE_DIMENSION_SWITCHED = "valueDimensionSwitched";
    public static final String CELL_SIZE_CHANGED = "cellSize";
    private static final String ROW_DIMENSION_CHANGED = "rowDim";
    private static final String COLUMN_DIMENSION_CHANGED = "columnDim";
    private static final long serialVersionUID = 325437934312047512L;

    @XmlTransient
    private PropertyChangeListener propertyListener;

    private HeatmapDimension rows;

    private HeatmapDimension columns;

    private HeatmapLayers layers;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IMatrix> data;


    public Heatmap()
    {
        this.rows = new HeatmapDimension();
        this.columns = new HeatmapDimension();
        this.layers = new HeatmapLayers();
    }

    public Heatmap(IMatrix data)
    {

        this.rows = new HeatmapDimension(data.getRows());
        this.columns = new HeatmapDimension(data.getColumns());
        this.layers = new HeatmapLayers(data);

        this.data = new ResourceReference<IMatrix>("data", data);
    }

    public HeatmapDimension getRows()
    {
        return rows;
    }

    public void setRows(@NotNull HeatmapDimension rows)
    {
        this.rows.removePropertyChangeListener(propertyListener);
        rows.addPropertyChangeListener(propertyListener);
        HeatmapDimension old = this.rows;
        this.rows = rows;
        firePropertyChange(ROW_DIMENSION_CHANGED, old, rows);
    }

    public HeatmapDimension getColumns()
    {
        return columns;
    }

    @Override
    public boolean isEmpty(int row, int column)
    {
        return getContents().isEmpty(row, column);
    }

    public void setColumns(@NotNull HeatmapDimension columns)
    {
        this.columns.removePropertyChangeListener(propertyListener);
        columns.addPropertyChangeListener(propertyListener);
        HeatmapDimension old = this.columns;
        this.columns = columns;
        firePropertyChange(COLUMN_DIMENSION_CHANGED, old, columns);
    }

    public void detach()
    {
        if (data != null && data.isLoaded())
        {
            data.get().detach();
        }
    }

    public void init()
    {
        propertyListener = new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                firePropertyChange(evt);
            }
        };
        this.rows.addPropertyChangeListener(propertyListener);
        this.columns.addPropertyChangeListener(propertyListener);

        IMatrix matrix = getData().get();
        this.rows.init(matrix.getRows());
        this.columns.init(matrix.getColumns());

        if (this.rows.getHeaderSize() == 0)
        {
            this.rows.addHeader(new HeatmapTextLabelsHeader());
        }
        if (this.columns.getHeaderSize() == 0)
        {
            this.columns.addHeader(new HeatmapTextLabelsHeader());
        }

        this.layers.init(matrix);
        particularInitialization(matrix);

    }

    public final void switchActiveCellDecorator(int newindex)
    {
        // switches from active ElementDecorator to ElementDecorator
        // of same type, but for another propertyIndex (data dimension)
        // no new ElementDecorator has been created and none will be removed

        final int oldindex = layers.getTopLayer();
        layers.setTopLayer(newindex);
        layers.getCellDecorators()[oldindex].removePropertyChangeListener(propertyListener);
        layers.getCellDecorators()[newindex].addPropertyChangeListener(propertyListener);
        firePropertyChange(VALUE_DIMENSION_SWITCHED, layers.getCellDecorators()[oldindex], layers.getCellDecorators()[newindex]);
    }

    public void replaceActiveDecorator(@NotNull ElementDecorator newDecorator) throws Exception
    {
        // removes the actual ElementDecorator for the propertyIndex displayed
        // and puts the new one. Needs to have proper propertyIndex set!

        int propIndex = layers.getTopLayer();
        layers.getCellDecorators()[propIndex].removePropertyChangeListener(propertyListener);
        newDecorator.addPropertyChangeListener(propertyListener);
        ElementDecorator old = layers.getCellDecorators()[propIndex];
        if (old.getAdapter().getElementClass().equals(newDecorator.getAdapter().getElementClass()))
        {
            layers.getCellDecorators()[propIndex] = newDecorator;
            firePropertyChange(CELL_DECORATOR_CHANGED, old, newDecorator);
        }
        else
        {
            throw new Exception("Substituting decorator not of same class");
        }
    }

    public final void setCellDecorators(@NotNull ElementDecorator[] decorators)
    {
        // The ElementDecorator Type has been changed and thus a new
        // set of ElementDecorators has to be put in place (for each
        // propertyIndex one.

        int propIndex = layers.getTopLayer();
        ElementDecorator old = null;
        if (layers.getCellDecorators() != null)
        {
            layers.getCellDecorators()[propIndex].removePropertyChangeListener(propertyListener);
            old = layers.getCellDecorators()[propIndex];
        }

        //a new data dimension has been added
        if (decorators.length > layers.getCellDecorators().length)
        {
            propIndex = decorators.length - 1;
            layers.setTopLayer(propIndex);
        }


        if (decorators[propIndex].getValueIndex() != propIndex)
        {
            // the new decorator forces a new a ValueIndex different to the
            // one that was selected
            propIndex = decorators[propIndex].getValueIndex();
            layers.setTopLayer(propIndex);
        }
        decorators[propIndex].addPropertyChangeListener(propertyListener);

        this.layers.setCellDecorators(decorators);
        firePropertyChange(CELL_DECORATOR_CHANGED, old, decorators[propIndex]);
    }

    public ElementDecorator getActiveCellDecorator()
    {
        return layers.getCellDecorators()[layers.getTopLayer()];
    }

    /**
     * Sets cell width.
     *
     * @param cellWidth the cell width
     * @deprecated use {@link #getColumns()}.setCellSize(...) instead
     */
    public void setCellWidth(int cellWidth)
    {
        int old = getColumns().getCellSize();
        getColumns().setCellSize(cellWidth);
        firePropertyChange(CELL_SIZE_CHANGED, old, cellWidth);
    }

    /**
     * Sets cell height.
     *
     * @param cellHeight the cell height
     * @deprecated use {@link #getRows()}.setCellSize(...) instead
     */
    public void setCellHeight(int cellHeight)
    {
        int old = getRows().getCellSize();
        getRows().setCellSize(cellHeight);
        firePropertyChange(CELL_SIZE_CHANGED, old, cellHeight);
    }

    // IMatrixView adaptor methods

    @Override
    public IMatrix getContents()
    {
        return getData().get();
    }

    public ResourceReference<IMatrix> getData()
    {
        return data;
    }

    public void setData(ResourceReference<IMatrix> data)
    {
        this.data = data;
    }

    @Override
    public Object getCellValue(int row, int column, int layer)
    {
        return getContents().getCellValue(rows.getVisible()[row], columns.getVisible()[column], layer);
    }

    @Override
    public void setCellValue(int row, int column, int layer, Object value)
    {
        getContents().setCellValue(rows.getVisible()[row], columns.getVisible()[column], layer, value);
    }

    @Override
    public IElementAdapter getCellAdapter()
    {
        return getContents().getCellAdapter();
    }

    @Override
    public IMatrixViewLayers getLayers()
    {
        return layers;
    }

    @Deprecated
    private void particularInitialization(IMatrix matrix)
    {
        final int propertiesNb = matrix.getLayers().size();
        if (matrix instanceof DoubleBinaryMatrix)
        {
            BinaryElementDecorator[] decorators = new BinaryElementDecorator[propertiesNb];
            for (int i = 0; i < decorators.length; i++)
            {
                BinaryElementDecorator decorator = (BinaryElementDecorator) ElementDecoratorFactory.create(ElementDecoratorNames.BINARY, matrix.getCellAdapter());
                decorator.setValueIndex(i);
                decorator.setCutoff(1.0);
                decorator.setCutoffCmp(CutoffCmp.EQ);
                decorators[i] = decorator;
            }
            layers.setCellDecorators(decorators);
            getRows().setGridEnabled(false);
            getColumns().setGridEnabled(false);
        }
        else if (matrix instanceof DoubleMatrix)
        {
            getRows().setGridEnabled(false);
            getColumns().setGridEnabled(false);
        }
    }

    public ElementDecorator[] getCellDecorators()
    {
        return layers.getCellDecorators();
    }
}
