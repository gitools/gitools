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
package org.gitools.heatmap.header;

import com.google.common.base.Function;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.decorator.Decoration;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.matrix.filter.PatternFunction;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class HeatmapColoredLabelsHeader extends HeatmapHeader {

    private static final String THICKNESS_CHANGED = "thickness";
    private static final String SEPARATION_GRID_CHANGED = "separationGrid";
    private static final String FORCE_LABEL_COLOR = "forceLabelColor";
    private static final String CLUSTERS_CHANGED = "clusters";

    @XmlElement
    private int thickness;

    @XmlElement
    private boolean separationGrid;

    @XmlElement(name = "force-label-color")
    private boolean forceLabelColor;

    @XmlElement
    private List<ColoredLabel> coloredLabels;

    private transient Map<String, Integer> dataColoredLabelIndices;

    public HeatmapColoredLabelsHeader() {
        super();
    }

    public HeatmapColoredLabelsHeader(HeatmapDimension hdim) {
        super(hdim);

        size = 20;
        thickness = 14;
        margin = 1;
        separationGrid = true;

        labelVisible = true;
        font = new Font(Font.SANS_SERIF, Font.BOLD, 12);
        labelRotated = false;
        forceLabelColor = true;
        labelColor = Color.black;

        coloredLabels = new ArrayList<>();
    }

    /* The thickness of the color band */
    public int getThickness() {
        return thickness;
    }

    /* The thickness of the color band */
    public void setThickness(int thickness) {
        int old = this.thickness;
        this.thickness = thickness;
        firePropertyChange(THICKNESS_CHANGED, old, thickness);
    }

    /* Separate different clusters with a grid */
    public boolean isSeparationGrid() {
        return separationGrid;
    }

    /* Separate different clusters with a grid */
    public void setSeparationGrid(boolean separationGrid) {
        boolean old = this.separationGrid;
        this.separationGrid = separationGrid;
        firePropertyChange(SEPARATION_GRID_CHANGED, old, separationGrid);
    }

    /**
     * The list of clusters in this set
     */
    public List<ColoredLabel> getClusters() {
        return coloredLabels;
    }

    public void reset() {
        this.dataColoredLabelIndices = null;
    }

    /**
     * The list of clusters in this set
     */
    public void setClusters(List<ColoredLabel> clusters) {
        this.coloredLabels = clusters;
        firePropertyChange(CLUSTERS_CHANGED, null, clusters);
    }

    /**
     * Return the corresponding matrix row/column cluster. Null if there is not cluster assigned.
     */

    public ColoredLabel getAssignedColoredLabel(String id) {
        Integer index = getAssignedColoredLabels().get(id);
        if (index == null) {
            return null;
        }
        return coloredLabels.get(index);
    }

    private Map<String, Integer> getAssignedColoredLabels() {
        if (dataColoredLabelIndices == null) {
            dataColoredLabelIndices = new HashMap<>(coloredLabels.size());
            for (int i = 0; i < coloredLabels.size(); i++) {
                dataColoredLabelIndices.put(coloredLabels.get(i).getValue(), i);
            }
        }
        return this.dataColoredLabelIndices;
    }

    public boolean isForceLabelColor() {
        return forceLabelColor;
    }

    public void setForceLabelColor(boolean forceLabelColor) {
        boolean old = this.forceLabelColor;
        this.forceLabelColor = forceLabelColor;
        firePropertyChange(FORCE_LABEL_COLOR, old, forceLabelColor);
    }

    @Override
    public void populateDetails(List<DetailsDecoration> details, String identifier) {

        DetailsDecoration decoration = new DetailsDecoration(getTitle(), getDescription(), getDescriptionUrl(), null, getValueUrl());
        decoration.setReference(this);

        if (identifier != null) {
            reset();
            decorate(decoration, getColoredLabel(identifier), true, false);
        }

        details.add(decoration);
    }

    public ColoredLabel getColoredLabel(int index) {
        return getColoredLabel(getHeatmapDimension().getLabel(index));
    }

    public ColoredLabel getColoredLabel(String identifier) {
        ColoredLabel label = getAssignedColoredLabel(getIdentifierTransform().apply(identifier));

        if (label == null) {
            label = new ColoredLabel("", getBackgroundColor());
        }

        return label;
    }

    public void decorate(Decoration decoration, ColoredLabel cluster, boolean forceShowLabel, boolean highlighted) {


        Color clusterColor = cluster != null ? cluster.getColor() : getBackgroundColor();
        if (highlighted && cluster != null) {
            clusterColor = blend(clusterColor, decoration.getHighlightColor(), 0.5f);
        }
        decoration.setBgColor(clusterColor);
        if (isLabelVisible() || forceShowLabel) {
            if (!cluster.getDisplayedLabel().equals("")) {
                decoration.setValue(cluster.getDisplayedLabel());
            }
        }
    }

    private transient Function<String, String> identifierTransform;

    public Function<String, String> getIdentifierTransform() {
        if (identifierTransform == null) {
            identifierTransform = new PatternFunction(getAnnotationPattern(), getHeatmapDimension().getAnnotations());
        }

        return identifierTransform;
    }

    public void setAnnotationMetadata(String annotationKey) {
        setDescription(getAnnotationMetadata("description", annotationKey));
        setValueUrl(getAnnotationMetadata("value-url", annotationKey));
        setDescriptionUrl(getAnnotationMetadata("description-url", annotationKey));
    }

    public String getAnnotationMetadata(String metadataKey, String annotationKey) {
        return getHeatmapDimension().getAnnotations().getAnnotationMetadata(metadataKey, annotationKey);
    }


    Color blend(Color c1, Color c2, float ratio) {
        if (ratio > 1f) ratio = 1f;
        else if (ratio < 0f) ratio = 0f;
        float iRatio = 1.0f - ratio;

        int i1 = c1.getRGB();
        int i2 = c2.getRGB();

        int a1 = (i1 >> 24 & 0xff);
        int r1 = ((i1 & 0xff0000) >> 16);
        int g1 = ((i1 & 0xff00) >> 8);
        int b1 = (i1 & 0xff);

        int a2 = (i2 >> 24 & 0xff);
        int r2 = ((i2 & 0xff0000) >> 16);
        int g2 = ((i2 & 0xff00) >> 8);
        int b2 = (i2 & 0xff);

        int a = (int) ((a1 * iRatio) + (a2 * ratio));
        int r = (int) ((r1 * iRatio) + (r2 * ratio));
        int g = (int) ((g1 * iRatio) + (g2 * ratio));
        int b = (int) ((b1 * iRatio) + (b2 * ratio));

        return new Color(a << 24 | r << 16 | g << 8 | b);
    }
}
