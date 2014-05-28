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

import com.google.common.collect.Lists;
import org.gitools.api.matrix.IAnnotations;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.resource.ResourceReference;
import org.gitools.api.resource.adapter.ResourceReferenceXmlAdapter;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapDimension extends AbstractMatrixViewDimension {

    public static final String PROPERTY_GRID_COLOR = "gridColor";
    public static final String PROPERTY_GRID_SIZE = "gridSize";
    public static final String PROPERTY_CELL_SIZE = "cellSize";
    public static final String PROPERTY_HEADERS = "headers";
    public static final String PROPERTY_HIGHLIGHTED = "highlighted";
    public static final String PROPERTY_SELECTED_HEADER = "selectedHeader";

    @XmlJavaTypeAdapter(ResourceReferenceXmlAdapter.class)
    private ResourceReference<AnnotationMatrix> annotations;

    @XmlElementWrapper(name = "headers")
    @XmlElement(name = "header")
    private LinkedList<HeatmapHeader> headers;

    @XmlElement(name = "grid-size")
    private int gridSize;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    @XmlElement(name = "grid-color")
    private Color gridColor;

    @XmlElement(name = "cell-size")
    private int cellSize;

    @XmlTransient
    private Set<String> highlightedLabels;

    @XmlTransient
    private Set<String> highlightedHeaders;

    @XmlTransient
    private String selectedHeader;

    @XmlTransient
    private static Color highlightingColor;

    public HeatmapDimension() {
        this(null);
    }

    public HeatmapDimension(IMatrixDimension matrixDimension) {
        super();

        this.headers = new LinkedList<>();
        this.gridSize = 1;
        this.gridColor = Color.WHITE;
        this.cellSize = 14;
        this.highlightedLabels = new HashSet<>();
        this.highlightedHeaders = new HashSet<>();
        this.selectedHeader = "";

        init(matrixDimension);
    }

    public void init(IMatrixDimension matrixDimension) {
        super.init(matrixDimension);

        if (matrixDimension == null) {
            return;
        }

        if (this.annotations == null) {
            this.annotations = new ResourceReference<>(matrixDimension.getId() + "-annotations", new AnnotationMatrix());
        }

        for (HeatmapHeader header : headers) {
            header.init(this);
        }
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int newValue) {
        int oldValue = this.cellSize;
        this.cellSize = newValue;
        firePropertyChange(PROPERTY_CELL_SIZE, oldValue, newValue);
    }

    public List<HeatmapHeader> getHeaders() {
        return headers;
    }

    public void updateHeaders() {
        firePropertyChange(PROPERTY_HEADERS, null, null);
    }

    public void addHeader(HeatmapHeader header) {
        if (header.getHeatmapDimension() == null) {
            header.setHeatmapDimension(this);
        }
        headers.addFirst(header);
        firePropertyChange(PROPERTY_HEADERS, null, headers);
    }

    public int getHeaderSize() {
        int size = 0;
        for (HeatmapHeader h : headers)
            size += h.getSize();
        return size;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        int old = this.gridSize;
        this.gridSize = gridSize;
        firePropertyChange(PROPERTY_GRID_SIZE, old, gridSize);
    }

    public Color getGridColor() {
        return gridColor;
    }

    public boolean showGrid() {
        return getCellSize() > 1;
    }

    public int getFullSize() {
        if (showGrid()) {
            return getCellSize() + getGridSize();
        }

        return getCellSize();
    }

    public void setGridColor(Color gridColor) {
        Color old = this.gridColor;
        this.gridColor = gridColor;
        firePropertyChange(PROPERTY_GRID_COLOR, old, gridColor);
    }

    public ResourceReference<AnnotationMatrix> getAnnotationsReference() {
        return annotations;
    }

    public void setAnnotationsReference(ResourceReference<AnnotationMatrix> annotationsReference) {
        this.annotations = annotationsReference;
    }

    public AnnotationMatrix getAnnotations() {
        if (annotations == null) {
            return null;
        }

        return annotations.get();
    }

    public void addAnnotations(IAnnotations annotations) {
        this.annotations.get().addAnnotations(annotations);
    }

    public boolean isHighlighted(String label) {
        return highlightedLabels.contains(label);
    }

    public void setHighlightedLabels(Set<String> highlightedLabels) {
        this.highlightedLabels = highlightedLabels;
        firePropertyChange(PROPERTY_HIGHLIGHTED, null, highlightedLabels);
    }

    public void clearHighlightedLabels() {
        highlightedLabels.clear();
        firePropertyChange(PROPERTY_HIGHLIGHTED, null, highlightedLabels);
    }


    public Set<String> getHighlightedHeaders() {
        return highlightedHeaders;
    }

    public void setHighlightedHeaders(Set<String> highlightedHeaders) {
        this.highlightedHeaders = highlightedHeaders;
        firePropertyChange(PROPERTY_HIGHLIGHTED, null, highlightedLabels);
    }

    public void clearHighlightedHeaders() {
        this.highlightedHeaders.clear();
        firePropertyChange(PROPERTY_HIGHLIGHTED, null, highlightedLabels);
    }

    public void populateDetails(List<DetailsDecoration> details) {
        Iterable<HeatmapHeader> itHeaders = headers;

        if (getId() == MatrixDimensionKey.COLUMNS) {
            itHeaders = Lists.reverse(headers);
        }

        for (HeatmapHeader header : itHeaders) {
            if (header.isVisible()) {
                header.populateDetails(details, getFocus(), selectedHeader.contains(header.getTitle()));
            }
        }
    }

    public void setHighlightingColor(Color highlightingColor) {
        this.highlightingColor = highlightingColor;
        firePropertyChange(PROPERTY_HIGHLIGHTED, null, highlightedLabels);
        updateHeaders();
    }

    public Color getHighlightingColor() {
        if (highlightingColor == null) {
            return getDefaultHighlightColor();
        }
        return highlightingColor;
    }

    private static Color getDefaultHighlightColor() {
        return new Color(254, 254, 0, 127);
    }

    public String getSelectedHeader() {
        return selectedHeader;
    }

    public void setSelectedHeader(HeatmapHeader header) {
        setSelectedHeader(header.getTitle());
    }

    public void setSelectedHeader(String title) {
        String old = getSelectedHeader();
        this.selectedHeader = title;
        firePropertyChange(PROPERTY_SELECTED_HEADER, old, title);
    }

    public void resetHighlightColor() {
        setHighlightingColor(getDefaultHighlightColor());
    }
}
