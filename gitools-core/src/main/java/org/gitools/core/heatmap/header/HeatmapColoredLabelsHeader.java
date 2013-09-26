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
package org.gitools.core.heatmap.header;

import org.gitools.core.clustering.ClusteringResults;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.label.AnnotationsPatternProvider;
import org.gitools.core.label.LabelProvider;
import org.gitools.core.model.decorator.Decoration;
import org.gitools.core.model.decorator.DetailsDecoration;
import org.gitools.utils.color.generator.ColorGenerator;
import org.gitools.utils.color.generator.ColorGeneratorFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
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
    private ColoredLabel[] coloredLabels;

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

        labelVisible = false;
        font = new Font(Font.MONOSPACED, Font.BOLD, 12);
        labelRotated = false;
        forceLabelColor = true;
        labelColor = Color.black;

        coloredLabels = new ColoredLabel[0];
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
    public ColoredLabel[] getClusters() {
        return coloredLabels;
    }

    /**
     * The list of clusters in this set
     */
    public void setClusters(ColoredLabel[] clusters) {
        ColoredLabel[] old = this.coloredLabels;
        this.coloredLabels = clusters;
        firePropertyChange(CLUSTERS_CHANGED, old, clusters);
    }

    /**
     * Return the corresponding matrix row/column cluster. Null if there is not cluster assigned.
     */
    @Nullable
    public ColoredLabel getAssignedColoredLabel(String id) {
        Integer index = getAssignedColoredLabels().get(id);
        if (index == null) {
            return null;
        }
        return coloredLabels[index];
    }

    private Map<String, Integer> getAssignedColoredLabels() {
        if (dataColoredLabelIndices == null) {
            dataColoredLabelIndices = new HashMap<String, Integer>(coloredLabels.length);
            for (int i = 0; i < coloredLabels.length; i++) {
                dataColoredLabelIndices.put(coloredLabels[i].getValue(), i);
            }
        }
        return this.dataColoredLabelIndices;
    }

    public boolean isForceLabelColor() {
        return forceLabelColor;
    }

    /*Use the same color for all labels instead of inverted*/
    public void setForceLabelColor(boolean forceLabelColor) {
        boolean old = this.forceLabelColor;
        this.forceLabelColor = forceLabelColor;
        firePropertyChange(FORCE_LABEL_COLOR, old, forceLabelColor);
    }

    public void updateFromClusterResults(@NotNull ClusteringResults results) {
        ColorGenerator cg = ColorGeneratorFactory.getDefault().create();

        String[] clusterTitles = results.getClusterTitles();
        coloredLabels = new ColoredLabel[results.getNumClusters()];
        for (int i = 0; i < results.getNumClusters(); i++) {
            ColoredLabel cluster = coloredLabels[i] = new ColoredLabel(clusterTitles[i], cg.next(clusterTitles[i]));
        }
    }

    @Override
    public void populateDetails(List<DetailsDecoration> details, int index) {

        DetailsDecoration decoration = new DetailsDecoration(getTitle(), getDescription(), getDescriptionUrl(), null, getValueUrl());

        if (index != -1) {
            decorate(decoration, getColoredLabel(index), true);
        }

        details.add(decoration);
    }

    public ColoredLabel getColoredLabel(int index) {
        ColoredLabel label = getAssignedColoredLabel(getLabelProvider().getLabel(index));

        if (label == null) {
            label = new ColoredLabel("", getBackgroundColor());
        }

        return label;
    }

    public void decorate(Decoration decoration, ColoredLabel cluster, boolean forceShowLabel) {

        Color clusterColor = cluster != null ? cluster.getColor() : getBackgroundColor();
        decoration.setBgColor(clusterColor);
        if (isLabelVisible() || forceShowLabel) {
            if (!cluster.getDisplayedLabel().equals("")) {
                decoration.setValue(cluster.getDisplayedLabel());
            }
        }
    }

    private transient LabelProvider labelProvider;

    public LabelProvider getLabelProvider() {
        if (labelProvider == null) {
            labelProvider = new AnnotationsPatternProvider(getHeatmapDimension(), getAnnotationPattern());
        }

        return labelProvider;
    }
}
