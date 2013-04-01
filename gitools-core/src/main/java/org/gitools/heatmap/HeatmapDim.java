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

import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.idtype.IdType;
import org.gitools.idtype.IdTypeManager;
import org.gitools.idtype.IdTypeXmlAdapter;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.model.AbstractModel;
import org.gitools.persistence.formats.analysis.adapter.PersistenceReferenceXmlAdapter;
import org.gitools.utils.xml.adapter.ColorXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

/**
 * Represents either row or column properties of a heatmap
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapDim extends AbstractModel implements PropertyChangeListener
{

    public static final String IDTYPE_CHANGED = "idType";
    public static final String HEADERS_CHANGED = "headers";
    public static final String HEADER_SIZE_CHANGED = "headerSize";
    public static final String CLUSTER_SETS_CHANGED = "clusterSets";
    public static final String GRID_PROPERTY_CHANGED = "gridProperty";
    public static final String ANNOTATIONS_CHANGED = "annotations";
    public static final String HIGHLIGHTING_CHANGED = "highlighting";

    @XmlJavaTypeAdapter(IdTypeXmlAdapter.class)
    protected IdType idType;

    @XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
    protected AnnotationMatrix annotations;

    @XmlTransient
    protected List<HeatmapHeader> headers;

    private boolean gridEnabled;

    private int gridSize;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color gridColor;

    @XmlTransient
    private Set<String> highlightedLabels;

    public HeatmapDim()
    {

        idType = IdTypeManager.getDefault().getDefaultIdType();

        headers = new ArrayList<HeatmapHeader>();

        gridEnabled = true;
        gridSize = 1;
        gridColor = Color.WHITE;

        highlightedLabels = new HashSet<String>();
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        Object src = evt.getSource();
        String pname = evt.getPropertyName();

        //System.out.println(getClass().getSimpleName() + " " + src + " " + pname);

        if (src instanceof HeatmapHeader
                && (HeatmapHeader.SIZE_CHANGED.equals(pname)
                || HeatmapHeader.VISIBLE_CHANGED.equals(pname)))
        {
            firePropertyChange(HEADER_SIZE_CHANGED);
        }

        firePropertyChange(evt);
    }

    public IdType getIdType()
    {
        return idType;
    }

    public void setIdType(IdType idType)
    {
        IdType old = this.idType;
        this.idType = idType;
        firePropertyChange(IDTYPE_CHANGED, old, idType);
    }

    public List<HeatmapHeader> getHeaders()
    {
        return Collections.unmodifiableList(headers);
    }

    public void addHeader(HeatmapHeader header)
    {
        if (header.getHeatmapDim() == null)
        {
            header.setHeatmapDim(this);
        }
        headers.add(header);
        header.addPropertyChangeListener(this);
        firePropertyChange(HEADERS_CHANGED);
    }

    public void removeHeader(int index)
    {
        if (index >= headers.size())
        {
            return;
        }
        HeatmapHeader header = headers.get(index);
        header.removePropertyChangeListener(this);
        headers.remove(header);
        firePropertyChange(HEADERS_CHANGED);
    }

    public void upHeader(int index)
    {
        if (index == 0 || index >= headers.size())
        {
            return;
        }
        HeatmapHeader header = headers.get(index);
        headers.set(index, headers.get(index - 1));
        headers.set(index - 1, header);
        firePropertyChange(HEADERS_CHANGED);
    }

    public void downHeader(int index)
    {
        if (index >= headers.size() - 1)
        {
            return;
        }
        HeatmapHeader header = headers.get(index);
        headers.set(index, headers.get(index + 1));
        headers.set(index + 1, header);
        firePropertyChange(HEADERS_CHANGED);
    }

    public int getHeaderSize()
    {
        int size = 0;
        for (HeatmapHeader h : headers)
            size += h.getSize();
        return size;
    }

    public boolean isGridEnabled()
    {
        return gridEnabled;
    }

    public void setGridEnabled(boolean enabled)
    {
        boolean old = this.gridEnabled;
        this.gridEnabled = enabled;
        firePropertyChange(GRID_PROPERTY_CHANGED, old, enabled);
    }

    public int getGridSize()
    {
        return gridSize;
    }

    public void setGridSize(int gridSize)
    {
        int old = this.gridSize;
        this.gridSize = gridSize;
        firePropertyChange(GRID_PROPERTY_CHANGED, old, gridSize);
    }

    public Color getGridColor()
    {
        return gridColor;
    }

    public void setGridColor(Color gridColor)
    {
        Color old = this.gridColor;
        this.gridColor = gridColor;
        firePropertyChange(GRID_PROPERTY_CHANGED, old, gridColor);
    }

    public AnnotationMatrix getAnnotations()
    {
        return annotations;
    }

    public void setAnnotations(AnnotationMatrix annotations)
    {
        AnnotationMatrix old = this.annotations;
        this.annotations = annotations;
        firePropertyChange(ANNOTATIONS_CHANGED, old, annotations);
    }

    public boolean isHighlighted(String label)
    {
        return highlightedLabels.contains(label);
    }

    public void setHighlightedLabels(Set<String> highlightedLabels)
    {
        this.highlightedLabels = highlightedLabels;
        firePropertyChange(HIGHLIGHTING_CHANGED);
    }

    public void clearHighlightedLabels()
    {
        highlightedLabels.clear();
        firePropertyChange(HIGHLIGHTING_CHANGED);
    }

}
