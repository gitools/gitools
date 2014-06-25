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

import org.gitools.api.matrix.*;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.api.resource.ResourceReference;
import org.gitools.api.resource.adapter.ResourceReferenceXmlAdapter;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.heatmap.plugins.Plugins;
import org.gitools.matrix.model.MatrixPosition;
import org.gitools.resource.Resource;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlType(propOrder = {"diagonal", "lastSaved", "authorName", "authorEmail", "rows", "columns", "data", "layers", "bookmarks", "pluggedBoxes"})
public class Heatmap extends Resource implements IMatrixView {

    public static final String PROPERTY_ROWS = "rows";
    public static final String PROPERTY_COLUMNS = "columns";
    public static final String PROPERTY_LAYERS = "layers";
    public static final String PROPERTY_AUTHOR_NAME = "authorName";
    public static final String PROPERTY_AUTHOR_EMAIL = "authorEmail";


    @XmlTransient
    private PropertyChangeListener propertyListener;

    private Date lastSaved;

    private String authorName;
    private String authorEmail;

    private HeatmapDimension rows;
    private HeatmapDimension columns;

    private HeatmapLayers layers;

    private transient HeatmapDimension diagonalRows;

    private Bookmarks bookmarks;

    @XmlElement(name = "plugged-boxes")
    private Plugins pluggedBoxes;

    private boolean diagonal;

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<IMatrix> data;

    public Heatmap() {
        super();
        this.rows = new HeatmapDimension();
        this.columns = new HeatmapDimension();
        this.layers = new HeatmapLayers();
        this.diagonal = false;
        this.bookmarks = new Bookmarks();
        this.pluggedBoxes = new Plugins();
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
        this.bookmarks = new Bookmarks();
        this.pluggedBoxes = new Plugins();
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

        // Detach heatmap cache
        //this.cache = null;

        // Detach layers cache
        for (HeatmapLayer layer : layers) {
            layer.detach();
        }

        // Force garbage collection
        System.gc();
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

    }

    public boolean isDiagonal() {
        return diagonal;
    }

    public void setDiagonal(boolean diagonal) {
        this.diagonal = diagonal;
    }

    public void applyBookmark(Bookmark b) {

        if (b.getRows() != null) {
            getRows().show(b.getRows());
        }

        if (b.getColumns() != null) {
            getColumns().show(b.getColumns());
        }

        if (b.getLayerId() != null) {
            getLayers().setTopLayer(getLayers().get(b.getLayerId()));
        }
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
        return new MatrixPosition(this);
    }

    @Override
    public IMatrix subset(IMatrixDimension... dimensionSubsets) {
        return getContents().subset(dimensionSubsets);
    }

    private static MatrixDimensionKey[] dimensions = new MatrixDimensionKey[]{ROWS, COLUMNS};

    @Override
    public MatrixDimensionKey[] getDimensionKeys() {
        return dimensions;
    }

    public Bookmarks getBookmarks() {
        return bookmarks;
    }

    public Plugins getPluggedBoxes() {
        return pluggedBoxes;
    }


    transient Map<ICacheKey, Object> cache;

    public <T> void setCache(ICacheKey<T> key, T value) {
        this.getCacheMap().put(key, value);
    }

    public <T> T getCache(ICacheKey<T> key) {
        return (T) this.getCacheMap().get(key);
    }

    private Map<ICacheKey, Object> getCacheMap() {
        if (cache == null) {
            cache = new HashMap<>();
        }
        return cache;
    }

    public Date getLastSaved() {
        return lastSaved;
    }

    public void setLastSaved(Date lastSaved) {
        this.lastSaved = lastSaved;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


}
