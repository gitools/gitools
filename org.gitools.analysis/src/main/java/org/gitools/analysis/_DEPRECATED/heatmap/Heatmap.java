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
package org.gitools.analysis._DEPRECATED.heatmap;

import org.gitools.analysis._DEPRECATED.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.MatrixDimensionKey;
import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;
import org.gitools.api.matrix.position.IMatrixPosition;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.api.resource.ResourceReference;
import org.gitools.api.resource.adapter.ResourceReferenceXmlAdapter;
import org.gitools.analysis._DEPRECATED.model.Resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlType(propOrder = {"diagonal", "rows", "columns", "data", "layers"})
public class Heatmap extends Resource implements IMatrixView {

    public static final String PROPERTY_ROWS = "rows";
    public static final String PROPERTY_COLUMNS = "columns";
    public static final String PROPERTY_LAYERS = "layers";

    @XmlTransient
    private PropertyChangeListener propertyListener;

    private HeatmapDimension rows;
    private HeatmapDimension columns;

    private HeatmapLayers layers;

    private transient HeatmapDimension diagonalRows;

    private boolean diagonal;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IMatrix> data;

    public Heatmap() {
        super();
        this.rows = new HeatmapDimension();
        this.columns = new HeatmapDimension();
        this.layers = new HeatmapLayers();
        this.diagonal = false;
    }

    public Heatmap(IMatrix data) {
        this(data, false);
    }

    public Heatmap(IMatrix data, boolean diagonal) {
        super();
        this.rows = new HeatmapDimension(data.getDimension(ROWS));
        this.columns = new HeatmapDimension(data.getDimension(COLUMNS));
        this.data = new ResourceReference<>("data", data);
        this.layers = new HeatmapLayers(data);
        this.diagonal = diagonal;
    }

    public HeatmapDimension getRows() {

        if (diagonal) {
            return diagonalRows;
        }

        return rows;
    }

    public void setRows(HeatmapDimension rows) {
        this.rows.removePropertyChangeListener(propertyListener);
        rows.addPropertyChangeListener(propertyListener);
        HeatmapDimension old = this.rows;
        this.rows = rows;
        firePropertyChange(PROPERTY_ROWS, old, rows);
    }

    public HeatmapDimension getColumns() {
        return columns;
    }

    public void setColumns(HeatmapDimension columns) {
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
        this.rows.init(matrix.getDimension(ROWS));
        this.columns.init(matrix.getDimension(COLUMNS));

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
    public HeatmapDimension getDimension(MatrixDimensionKey dimension) {

        if (dimension == ROWS) {
            return getRows();
        }

        if (dimension == COLUMNS) {
            return getColumns();
        }

        return null;
    }

    @Override
    public HeatmapLayers getLayers() {
        return layers;
    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, IMatrixPosition position) {
        return get(layer, position.toVector());
    }

    @Override
    public <T> void set(IMatrixLayer<T> layer, T value, IMatrixPosition position) {
        set(layer, value, position.toVector());
    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, String... identifiers) {
        return getContents().get(layer, identifiers);
    }

    @Override
    public <T> void set(IMatrixLayer<T> layer, T value, String... identifiers) {
        getContents().set(layer, value, identifiers);
    }

    @Override
    public IMatrixPosition newPosition() {
        return getContents().newPosition();
    }

    private static MatrixDimensionKey[] dimensions = new MatrixDimensionKey[]{ROWS, COLUMNS};

    @Override
    public MatrixDimensionKey[] getDimensionKeys() {
        return dimensions;
    }

    @Deprecated
    private void particularInitialization(IMatrix matrix) {
        /*if (matrix instanceof DoubleBinaryMatrix) {
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
        } */
    }

}
