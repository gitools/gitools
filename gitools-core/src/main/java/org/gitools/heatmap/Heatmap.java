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
import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.Resource;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.model.decorator.impl.BinaryElementDecorator;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement()
@XmlType(propOrder = {"rows", "columns", "body"})
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
    private HeatmapBody body;

    public Heatmap()
    {
        this.body = new HeatmapBody();
        this.rows = new HeatmapDimension();
        this.columns = new HeatmapDimension();
    }

    public Heatmap(IMatrix data)
    {
        this.body = new HeatmapBody(data);
        this.rows = new HeatmapDimension(data.getRowCount());
        this.columns = new HeatmapDimension(data.getColumnCount());

        particularInitialization();
    }

    public HeatmapBody getBody()
    {
        return body;
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
        if (body != null)
        {
            body.detach();
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
        body.addPropertyChangeListener(propertyListener);

        this.rows.addPropertyChangeListener(propertyListener);
        this.columns.addPropertyChangeListener(propertyListener);

        this.rows.init();
        this.columns.init();

        if (this.rows.getHeaderSize() == 0)
        {
            this.rows.addHeader(new HeatmapTextLabelsHeader());
        }
        if (this.columns.getHeaderSize() == 0)
        {
            this.columns.addHeader(new HeatmapTextLabelsHeader());
        }

        // Initialize decorators adapters
        IElementAdapter adapter = body.getData().get().getCellAdapter();
        for (ElementDecorator decorator : getCellDecorators())
        {
            decorator.setAdapter(adapter);
        }

    }

    // Matrix View
    public final IMatrixView getMatrixView()
    {
        return this;
    }

    /**
     * Gets cell width.
     *
     * @return the cell width
     * @deprecated use {@link #getColumns()}.getCellSize() instead
     */
    public int getCellWidth()
    {
        return getColumns().getCellSize();
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
     * Gets cell height.
     *
     * @return the cell height
     * @deprecated use {@link #getRows()}.getCellSize() instead
     */
    public int getCellHeight()
    {
        return getRows().getCellSize();
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

    public final void switchActiveCellDecorator(int newindex)
    {
        // switches from active ElementDecorator to ElementDecorator
        // of same type, but for another propertyIndex (data dimension)
        // no new ElementDecorator has been created and none will be removed

        final int oldindex = body.getSelectedView();
        body.setSelectedView(newindex);
        body.getViews()[oldindex].removePropertyChangeListener(propertyListener);
        body.getViews()[newindex].addPropertyChangeListener(propertyListener);
        firePropertyChange(VALUE_DIMENSION_SWITCHED, body.getViews()[oldindex], body.getViews()[newindex]);
    }

    public void replaceActiveDecorator(@NotNull ElementDecorator newDecorator) throws Exception
    {
        // removes the actual ElementDecorator for the propertyIndex displayed
        // and puts the new one. Needs to have proper propertyIndex set!

        int propIndex = body.getSelectedView();
        body.getViews()[propIndex].removePropertyChangeListener(propertyListener);
        newDecorator.addPropertyChangeListener(propertyListener);
        ElementDecorator old = body.getViews()[propIndex];
        if (old.getAdapter().getElementClass().equals(newDecorator.getAdapter().getElementClass()))
        {
            body.getViews()[propIndex] = newDecorator;
            firePropertyChange(CELL_DECORATOR_CHANGED, old, newDecorator);
        }
        else
        {
            throw new Exception("Substituting decorator not of same class");
        }
    }

    /**
     * Get cell decorators.
     *
     * @return the element decorator [ ]
     * @deprecated use {@link #getMatrixView()}.getViews()
     */
    public ElementDecorator[] getCellDecorators()
    {
        return body.getViews();
    }

    public final void setCellDecorators(@NotNull ElementDecorator[] decorators)
    {
        // The ElementDecorator Type has been changed and thus a new
        // set of ElementDecorators has to be put in place (for each
        // propertyIndex one.

        int propIndex = body.getSelectedView();
        ElementDecorator old = null;
        if (body.getViews() != null)
        {
            body.getViews()[propIndex].removePropertyChangeListener(propertyListener);
            old = body.getViews()[propIndex];
        }

        //a new data dimension has been added
        if (decorators.length > body.getViews().length)
        {
            propIndex = decorators.length - 1;
            body.setSelectedView(propIndex);
        }


        if (decorators[propIndex].getValueIndex() != propIndex)
        {
            // the new decorator forces a new a ValueIndex different to the
            // one that was selected
            propIndex = decorators[propIndex].getValueIndex();
            body.setSelectedView(propIndex);
        }
        decorators[propIndex].addPropertyChangeListener(propertyListener);

        body.setViews(decorators);
        firePropertyChange(CELL_DECORATOR_CHANGED, old, decorators[propIndex]);
    }

    public ElementDecorator getActiveCellDecorator()
    {
        return body.getActiveView();
    }

    @Deprecated
    private void particularInitialization()
    {
        final int propertiesNb = getMatrixView().getCellAdapter().getPropertyCount();
        final IMatrix matrix = getContents();
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
            setCellDecorators(decorators);
            getRows().setGridEnabled(false);
            getColumns().setGridEnabled(false);
        }
        else if (matrix instanceof DoubleMatrix)
        {
            getRows().setGridEnabled(false);
            getColumns().setGridEnabled(false);
        }
    }

    // IMatrixView adaptor methods

    @Override
    public IMatrix getContents()
    {
        return body.getData().get();
    }

    @Override
    public int[] getVisibleRows()
    {
        return rows.getVisible();
    }

    @Override
    public void setVisibleRows(int[] indices)
    {
        rows.setVisible(indices);
    }

    @Override
    public int[] getVisibleColumns()
    {
        return columns.getVisible();
    }

    @Override
    public void setVisibleColumns(int[] indices)
    {
        columns.setVisible(indices);
    }

    @Override
    public void moveRowsUp(int[] indices)
    {
        rows.move(Direction.UP, indices);
    }

    @Override
    public void moveRowsDown(int[] indices)
    {
        rows.move(Direction.DOWN, indices);
    }

    @Override
    public void moveColumnsLeft(int[] indices)
    {
        columns.move(Direction.LEFT, indices);
    }

    @Override
    public void moveColumnsRight(int[] indices)
    {
        columns.move(Direction.RIGHT, indices);
    }

    @Override
    public void hideRows(int[] indices)
    {
        rows.hide(indices);
    }

    @Override
    public void hideColumns(int[] indices)
    {
        columns.hide(indices);
    }

    @Override
    public int[] getSelectedRows()
    {
        return rows.getSelected();
    }

    @Override
    public void setSelectedRows(int[] indices)
    {
        rows.setSelected(indices);
    }

    @Override
    public boolean isRowSelected(int index)
    {
        return rows.isSelected(index);
    }

    @Override
    public int[] getSelectedColumns()
    {
        return columns.getSelected();
    }

    @Override
    public void setSelectedColumns(int[] indices)
    {
        columns.setSelected(indices);
    }

    @Override
    public boolean isColumnSelected(int index)
    {
        return columns.isSelected(index);
    }

    @Override
    public void selectAll()
    {
        rows.selectAll();
        columns.selectAll();
    }

    @Override
    public void invertSelection()
    {
        rows.invertSelection();
        columns.invertSelection();
    }

    @Override
    public void clearSelection()
    {
        rows.clearSelection();
        columns.clearSelection();
    }

    @Override
    public int getLeadSelectionRow()
    {
        return rows.getSelectionLead();
    }

    @Override
    public int getLeadSelectionColumn()
    {
        return columns.getSelectionLead();
    }

    @Override
    public void setLeadSelection(int row, int column)
    {
        rows.setSelectionLead(row);
        columns.setSelectionLead(column);
    }

    @Override
    public int getSelectedPropertyIndex()
    {
        return body.getSelectedView();
    }

    @Override
    public void setSelectedPropertyIndex(int index)
    {
        body.setSelectedView(index);
    }

    @Override
    public int getRowCount()
    {
        return rows.visibleSize();
    }

    @Override
    public String getRowLabel(int index)
    {
        return getContents().getRowLabel(rows.getVisible()[index]);
    }

    @Override
    public int getRowIndex(String label)
    {
        return getContents().getRowIndex(label);
    }

    @Override
    public int getColumnCount()
    {
        return columns.visibleSize();
    }

    @Override
    public String getColumnLabel(int index)
    {
        return getContents().getColumnLabel(columns.getVisible()[index]);
    }

    @Override
    public int getColumnIndex(String label)
    {
        return getContents().getColumnIndex(label);
    }

    @Override
    public Object getCell(int row, int column)
    {
        return getContents().getCell(rows.getVisible()[row], columns.getVisible()[column]);
    }

    @Override
    public Object getCellValue(int row, int column, int index)
    {
        return getContents().getCellValue(rows.getVisible()[row], columns.getVisible()[column], index);
    }

    @Override
    public Object getCellValue(int row, int column, String id)
    {
        return getContents().getCellValue(rows.getVisible()[row], columns.getVisible()[column], id);
    }

    @Override
    public void setCellValue(int row, int column, int index, Object value)
    {
        getContents().setCellValue(rows.getVisible()[row], columns.getVisible()[column], index, value);
    }

    @Override
    public void setCellValue(int row, int column, String id, Object value)
    {
        getContents().setCellValue(rows.getVisible()[row], columns.getVisible()[column], id, value);
    }

    @Override
    public IElementAdapter getCellAdapter()
    {
        return getContents().getCellAdapter();
    }

    @Override
    public List<IElementAttribute> getCellAttributes()
    {
        return getContents().getCellAttributes();
    }

    @Override
    public int getCellAttributeIndex(String id)
    {
        return getContents().getCellAttributeIndex(id);
    }

}
