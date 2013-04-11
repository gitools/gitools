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
import org.gitools.heatmap.xml.HeatmapMatrixViewXmlAdapter;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.Artifact;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorDescriptor;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceLocator;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.stats.test.results.ZScoreResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.List;

/**
 * @noinspection ALL
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement()
public class Heatmap extends Artifact implements Serializable, IResource
{

    public static final String CELL_DECORATOR_CHANGED = "cellDecorator";
    public static final String VALUE_DIMENSION_SWITCHED = "valueDimensionSwitched";
    private static final String MATRIX_VIEW_CHANGED = "matrixView";
    public static final String CELL_SIZE_CHANGED = "cellSize";
    private static final String ROW_DIMENSION_CHANGED = "rowDim";
    private static final String COLUMN_DIMENSION_CHANGED = "columnDim";
    private static final long serialVersionUID = 325437934312047512L;

    @XmlTransient
    private PropertyChangeListener propertyListener;

    // Cells
    @Nullable
    @XmlJavaTypeAdapter(HeatmapMatrixViewXmlAdapter.class)
    private IMatrixView matrixView;

    private ElementDecorator[] cellDecorators;

    private int cellWidth;

    private int cellHeight;

    private HeatmapDim rowDim;

    private HeatmapDim columnDim;

    @XmlTransient
    private IResourceLocator locator;

    public Heatmap()
    {

    }

    public Heatmap(@NotNull IMatrixView matrixView)
    {
        this.matrixView = matrixView;

        this.cellDecorators = cellDecoratorFromMatrix(matrixView);
        this.cellWidth = 14;
        this.cellHeight = 14;

        this.rowDim = new HeatmapDim();
        this.columnDim = new HeatmapDim();
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
        matrixView.addPropertyChangeListener(propertyListener);
        getActiveCellDecorator().addPropertyChangeListener(propertyListener);

        this.rowDim.addPropertyChangeListener(propertyListener);
        this.columnDim.addPropertyChangeListener(propertyListener);

        if (this.rowDim.getHeaderSize() == 0)
            this.rowDim.addHeader(new HeatmapTextLabelsHeader());
        if (this.columnDim.getHeaderSize() == 0)
            this.columnDim.addHeader(new HeatmapTextLabelsHeader());
    }

    @NotNull
    private static ElementDecorator[] getCellDecoratorsFromDecorator(@NotNull ElementDecorator cellDecorator, int attributesNb)
    {
        ElementDecorator[] cellDecorators = new ElementDecorator[attributesNb];
        for (int i = 0; i < attributesNb; i++)
        {
            ElementDecoratorDescriptor descriptor = ElementDecoratorFactory.getDescriptor(cellDecorator.getClass());
            cellDecorators[i] = ElementDecoratorFactory.create(descriptor, cellDecorator.getAdapter());
        }
        return cellDecorators;
    }

    @NotNull
    private static ElementDecorator[] cellDecoratorFromMatrix(@NotNull IMatrixView matrixView)
    {

        ElementDecorator decorator = null;

        IElementAdapter adapter = matrixView.getCellAdapter();
        List<IElementAttribute> attributes = matrixView.getCellAttributes();

        int attrIndex = matrixView.getSelectedPropertyIndex();
        if (attrIndex >= 0 && attrIndex < attributes.size())
        {
            Class<?> elementClass = attributes.get(attrIndex).getValueClass();

            Class<?> c = adapter.getElementClass();

            if (CommonResult.class.isAssignableFrom(c) || ZScoreResult.class == c)
            {
                decorator = ElementDecoratorFactory.create(ElementDecoratorNames.ZSCORE, adapter);
            }
            else if (CommonResult.class.isAssignableFrom(c) || CommonResult.class == c)
            {
                decorator = ElementDecoratorFactory.create(ElementDecoratorNames.PVALUE, adapter);
            }
            else if (elementClass == double.class || double.class.isInstance(elementClass))
            {
                decorator = ElementDecoratorFactory.create(ElementDecoratorNames.LINEAR_TWO_SIDED, adapter);
            }
        }

        if (decorator == null)
        {
            decorator = ElementDecoratorFactory.create(ElementDecoratorNames.LINEAR_TWO_SIDED, adapter);
        }

        return getCellDecoratorsFromDecorator(decorator, matrixView.getCellAttributes().size());
    }

    public IResourceLocator getLocator()
    {
        return locator;
    }

    public void setLocator(IResourceLocator locator)
    {
        this.locator = locator;
    }

    // Matrix View

    @Nullable
    public final IMatrixView getMatrixView()
    {
        return matrixView;
    }

    public final void setMatrixView(@NotNull IMatrixView matrixView)
    {
        this.matrixView.removePropertyChangeListener(propertyListener);
        matrixView.addPropertyChangeListener(propertyListener);
        final IMatrixView old = this.matrixView;
        this.matrixView = matrixView;
        firePropertyChange(MATRIX_VIEW_CHANGED, old, matrixView);
    }

    // Cells

    public final ElementDecorator getActiveCellDecorator()
    {
        // returns the active ElementDecorator

        int propIndex = getMatrixView().getSelectedPropertyIndex();
        return cellDecorators[propIndex];
    }

    public final void switchActiveCellDecorator(int newindex)
    {
        // switches from active ElementDecorator to ElementDecorator
        // of same type, but for another propertyIndex (data dimension)
        // no new ElementDecorator has been created and none will be removed

        final int oldindex = getMatrixView().getSelectedPropertyIndex();
        getMatrixView().setSelectedPropertyIndex(newindex);
        this.cellDecorators[oldindex].removePropertyChangeListener(propertyListener);
        this.cellDecorators[newindex].addPropertyChangeListener(propertyListener);
        firePropertyChange(VALUE_DIMENSION_SWITCHED, this.cellDecorators[oldindex], this.cellDecorators[newindex]);
    }

    public void replaceActiveDecorator(@NotNull ElementDecorator newDecorator) throws Exception
    {
        // removes the actual ElementDecorator for the propertyIndex displayed 
        // and puts the new one. Needs to have proper propertyIndex set!

        int propIndex = getMatrixView().getSelectedPropertyIndex();
        this.cellDecorators[propIndex].removePropertyChangeListener(propertyListener);
        newDecorator.addPropertyChangeListener(propertyListener);
        ElementDecorator old = this.cellDecorators[propIndex];
        if (old.getAdapter().getElementClass().equals(newDecorator.getAdapter().getElementClass()))
        {
            this.cellDecorators[propIndex] = newDecorator;
            firePropertyChange(CELL_DECORATOR_CHANGED, old, newDecorator);
        }
        else
        {
            throw new Exception("Substituting decorator not of same class");
        }
    }

    public final ElementDecorator[] getCellDecorators()
    {
        return this.cellDecorators;
    }

    public final void setCellDecorators(@NotNull ElementDecorator[] decorators)
    {
        // The ElementDecorator Type has been changed and thus a new
        // set of ElementDecorators has to be put in place (for each
        // propertyIndex one.

        int propIndex = getMatrixView().getSelectedPropertyIndex();
        ElementDecorator old = null;
        if (this.cellDecorators != null)
        {
            this.cellDecorators[propIndex].removePropertyChangeListener(propertyListener);
            old = this.cellDecorators[propIndex];
        }

        //a new data dimension has been added
        if (decorators.length > this.cellDecorators.length)
        {
            propIndex = decorators.length - 1;
            matrixView.setSelectedPropertyIndex(propIndex);
        }


        if (decorators[propIndex].getValueIndex() != propIndex)
        {
            // the new decorator forces a new a ValueIndex different to the
            // one that was selected
            propIndex = decorators[propIndex].getValueIndex();
            matrixView.setSelectedPropertyIndex(propIndex);
        }
        decorators[propIndex].addPropertyChangeListener(propertyListener);

        this.cellDecorators = decorators;
        firePropertyChange(CELL_DECORATOR_CHANGED, old, decorators[propIndex]);
    }

    public int getCellWidth()
    {
        return cellWidth;
    }

    public void setCellWidth(int cellWidth)
    {
        int old = this.cellWidth;
        this.cellWidth = cellWidth;
        firePropertyChange(CELL_SIZE_CHANGED, old, cellWidth);
    }

    public int getCellHeight()
    {
        return cellHeight;
    }

    public void setCellHeight(int cellHeight)
    {
        int old = this.cellHeight;
        this.cellHeight = cellHeight;
        firePropertyChange(CELL_SIZE_CHANGED, old, cellHeight);
    }

    public HeatmapDim getRowDim()
    {
        return rowDim;
    }

    public void setRowDim(@NotNull HeatmapDim rowDim)
    {
        this.rowDim.removePropertyChangeListener(propertyListener);
        rowDim.addPropertyChangeListener(propertyListener);
        HeatmapDim old = this.rowDim;
        this.rowDim = rowDim;
        firePropertyChange(ROW_DIMENSION_CHANGED, old, rowDim);
    }

    public HeatmapDim getColumnDim()
    {
        return columnDim;
    }

    public void setColumnDim(@NotNull HeatmapDim columnDim)
    {
        this.columnDim.removePropertyChangeListener(propertyListener);
        columnDim.addPropertyChangeListener(propertyListener);
        HeatmapDim old = this.columnDim;
        this.columnDim = columnDim;
        firePropertyChange(COLUMN_DIMENSION_CHANGED, old, columnDim);
    }

    // Generated values
    @Deprecated
    public String getColumnLabel(int index)
    {
        String label = matrixView.getColumnLabel(index);
        return label;
    }

    @Deprecated
    public String getRowLabel(int index)
    {
        String label = matrixView.getRowLabel(index);
        return label;
    }

    public void detach()
    {
        if (matrixView!=null)
        {
            this.matrixView.detach();
        }

    }

}
