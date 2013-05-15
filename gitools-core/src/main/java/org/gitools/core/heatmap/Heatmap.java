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
package org.gitools.core.heatmap;

import org.gitools.core.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.core.matrix.MirrorDimension;
import org.gitools.core.matrix.model.AbstractMatrix;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.matrix.model.matrix.DoubleBinaryMatrix;
import org.gitools.core.matrix.model.matrix.DoubleMatrix;
import org.gitools.core.model.decorator.impl.BinaryDecorator;
import org.gitools.core.persistence.ResourceReference;
import org.gitools.core.persistence.formats.analysis.adapter.ResourceReferenceXmlAdapter;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlType(propOrder = {"diagonal", "rows", "columns", "data", "layers"})
public class Heatmap extends AbstractMatrix implements IMatrixView {
    public static final String PROPERTY_ROWS = "rows";
    public static final String PROPERTY_COLUMNS = "columns";
    public static final String PROPERTY_LAYERS = "layers";

    public static final int ROW = 0;
    public static final int COLUMN = 1;


    @XmlTransient
    private PropertyChangeListener propertyListener;

    private HeatmapDimension rows;

    private transient HeatmapDimension diagonalRows;

    private HeatmapDimension columns;

    private HeatmapLayers layers;

    private boolean diagonal;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IMatrix> data;

    public Heatmap() {
        this.rows = new HeatmapDimension();
        this.columns = new HeatmapDimension();
        this.layers = new HeatmapLayers();
        this.diagonal = false;
    }

    public Heatmap(IMatrix data) {
        this(data, false);
    }

    public Heatmap(IMatrix data, boolean diagonal) {
        this.rows = new HeatmapDimension(this, data.getRows());
        this.columns = new HeatmapDimension(this, data.getColumns());
        this.layers = new HeatmapLayers(data);
        this.data = new ResourceReference<IMatrix>("data", data);
        this.diagonal = diagonal;
    }

    public HeatmapDimension getRows() {

        if (diagonal) {
            return diagonalRows;
        }

        return rows;
    }

    public void setRows(@NotNull HeatmapDimension rows) {
        this.rows.removePropertyChangeListener(propertyListener);
        rows.addPropertyChangeListener(propertyListener);
        HeatmapDimension old = this.rows;
        this.rows = rows;
        firePropertyChange(PROPERTY_ROWS, old, rows);
    }

    public HeatmapDimension getColumns() {
        return columns;
    }

    public void setColumns(@NotNull HeatmapDimension columns) {
        this.columns.removePropertyChangeListener(propertyListener);
        columns.addPropertyChangeListener(propertyListener);
        HeatmapDimension old = this.columns;
        this.columns = columns;
        firePropertyChange(PROPERTY_COLUMNS, old, columns);
    }

    public void detach() {
        if (data != null && data.isLoaded()) {
            data.get().detach();
        }
    }

    public void init() {
        propertyListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                firePropertyChange(evt);
            }
        };
        this.rows.addPropertyChangeListener(propertyListener);
        this.columns.addPropertyChangeListener(propertyListener);
        this.layers.addPropertyChangeListener(propertyListener);

        IMatrix matrix = getData().get();
        this.rows.init(this, matrix.getRows());
        this.columns.init(this, matrix.getColumns());

        if (this.rows.getHeaderSize() == 0) {
            this.rows.addHeader(new HeatmapTextLabelsHeader());
        }
        if (this.columns.getHeaderSize() == 0) {
            this.columns.addHeader(new HeatmapTextLabelsHeader());
        }

        this.layers.init(matrix);

        if (isDiagonal()) {
            diagonalRows = new MirrorDimension(columns, rows);
        }

        particularInitialization(matrix);

    }

    public boolean isDiagonal() {
        return diagonal;
    }

    public void setDiagonal(boolean diagonal) {
        this.diagonal = diagonal;
    }

    @Override
    public IMatrix getContents() {
        return getData().get();
    }

    public ResourceReference<IMatrix> getData() {
        return data;
    }

    public void setData(ResourceReference<IMatrix> data) {
        this.data = data;
    }

    @Override
    public Object getValue(int[] position, int layer) {

        int[] p = Arrays.copyOf(position, position.length);

        applyDiagonal(p);
        applyVisible(p);
        applyDiagonal(p);

        return getContents().getValue(p, layer);
    }

    @Override
    public void setValue(int[] position, int layer, Object value) {

        int[] p = Arrays.copyOf(position, position.length);

        applyDiagonal(p);
        applyVisible(p);
        applyDiagonal(p);

        getContents().setValue(p, layer, value);
    }

    @Override
    public HeatmapLayers getLayers() {
        return layers;
    }

    @Deprecated
    private void particularInitialization(IMatrix matrix) {
        if (matrix instanceof DoubleBinaryMatrix) {
            for (HeatmapLayer layer : getLayers()) {
                BinaryDecorator decorator = new BinaryDecorator();
                decorator.setCutoff(1.0);
                decorator.setComparator(CutoffCmp.EQ);
                layer.setDecorator(decorator);
            }

            getRows().setGridSize(0);
            getColumns().setGridSize(0);
        } else if (matrix instanceof DoubleMatrix) {
            getRows().setGridSize(0);
            getColumns().setGridSize(0);
        }
    }

    private void applyDiagonal(int[] position) {
        if (isDiagonal() && position[COLUMN] < position[ROW]) {
            int tmp = position[COLUMN];
            position[COLUMN] = position[ROW];
            position[ROW] = tmp;
        }
    }

    private void applyVisible(int[] position) {
        position[ROW] = getRows().getVisibleIndices()[position[ROW]];
        position[COLUMN] = getColumns().getVisibleIndices()[position[COLUMN]];
    }

}
