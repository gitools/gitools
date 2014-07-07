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
import com.google.common.collect.Lists;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.utils.textpattern.TextPattern;

import javax.xml.bind.annotation.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class HierarchicalClusterHeatmapHeader extends HeatmapHeader {

    public static final String PROPERTY_INTERACTION_LEVEL = "PROPERTY_INTERACTION_LEVEL";
    @XmlElementWrapper(name="clusterLevels")
    @XmlElement(name="levels")
    private List<HeatmapColoredLabelsHeader> clusterLevels;

    @XmlTransient
    private int interactionLevel = -1;

    @XmlTransient
    private boolean reportLastInteraction;

    @XmlElement
    private HierarchicalCluster hierarchicalCluster;

    public HierarchicalClusterHeatmapHeader() {
        super();
        reportLastInteraction = false;
    }

    public HierarchicalClusterHeatmapHeader(HeatmapDimension hdim) {
        super(hdim);
        clusterLevels = new ArrayList<>();
        reportLastInteraction = false;
    }

    public void addLevel(HeatmapColoredLabelsHeader level) {
        clusterLevels.add(level);
    }

    @Override
    public void init(HeatmapDimension heatmapDimension) {
        super.init(heatmapDimension);
        for (HeatmapColoredLabelsHeader levels : clusterLevels) {
            levels.init(heatmapDimension);
        }
    }

    public List<HeatmapColoredLabelsHeader> getClusterLevels() {
        return clusterLevels;
    }

    /* The thickness of the color band */
    public int getThickness() {
        return clusterLevels.size() > 0 ? clusterLevels.get(0).getThickness() : 1;
    }

    /* The thickness of the color band */
    public void setThickness(int thickness) {
        for (HeatmapColoredLabelsHeader level : clusterLevels) {
            level.setThickness(thickness);
        }
    }

    /* Separate different clusters with a grid */
    public boolean isSeparationGrid() {
        return clusterLevels.size() > 0 ? clusterLevels.get(0).isSeparationGrid() : true;
    }

    /* Separate different clusters with a grid */
    public void setSeparationGrid(boolean separationGrid) {
        for (HeatmapColoredLabelsHeader level : clusterLevels) {
            level.setSeparationGrid(separationGrid);
        }
    }

    public boolean isForceLabelColor() {
        return clusterLevels.size() > 0 ? clusterLevels.get(0).isForceLabelColor() : true;
    }


    public void setForceLabelColor(boolean forceLabelColor) {
        for (HeatmapColoredLabelsHeader level : clusterLevels) {
            level.setSeparationGrid(forceLabelColor);
        }
    }

    @Override
    public Font getFont() {
        return clusterLevels.size() > 0 ? clusterLevels.get(0).getFont() : null;

    }

    @Override
    public void setFont(Font font) {
        for (HeatmapColoredLabelsHeader level : clusterLevels) {
            level.setFont(font);
        }
    }

    @Override
    public int getSize() {
        size = 0;
        for (HeatmapColoredLabelsHeader level : clusterLevels) {
            size += level.getSize();
        }
        return size;
    }

    @Override
    public void populateDetails(List<DetailsDecoration> details, String identifier, boolean selected) {

        DetailsDecoration desiredDecoration = null;

        for (HeatmapColoredLabelsHeader level : Lists.reverse(clusterLevels)) {

            DetailsDecoration decoration = new DetailsDecoration(this.getTitle(),
                    getDescription(),
                    null, null, null);
            decoration.setReference(this);

            if (identifier != null) {
                level.reset();
                ColoredLabel cluster = level.getColoredLabel(identifier);

                Color clusterColor = cluster != null ? cluster.getColor() : getBackgroundColor();
                decoration.setBgColor(clusterColor);
                if (!cluster.getDisplayedLabel().equals("")) {
                    int levelIndex = clusterLevels.size() - clusterLevels.indexOf(level);
                    decoration.setValue("L" + levelIndex + ": " + cluster.getDisplayedLabel());
                    desiredDecoration = decoration;
                }
            }

            if (isReportLastInteraction() && clusterLevels.indexOf(level) == interactionLevel) {
                break;
            }

        }

        if (desiredDecoration != null) {
            desiredDecoration.setSelected(selected);
            details.add(desiredDecoration);
        }
    }

    @Override
    public Function<String, String> getIdentifierTransform() {
        return null;
    }

    public void setInteractionLevel(int interactionLevel) {
        int old = this.interactionLevel;
        this.interactionLevel = interactionLevel;
        firePropertyChange(PROPERTY_INTERACTION_LEVEL, old, interactionLevel);
    }

    public HeatmapColoredLabelsHeader getInteractionLevelHeader() {
        if (interactionLevel < 0) {
            return null;
        }
        return clusterLevels.get(interactionLevel);
    }

    public boolean isReportLastInteraction() {
        return reportLastInteraction;
    }

    public void setReportLastInteraction(boolean reportLastInteraction) {
        this.reportLastInteraction = reportLastInteraction;
        //firePropertyChange();
    }


    public void setHierarchicalCluster(HierarchicalCluster hierarchicalCluster) {
        this.hierarchicalCluster = hierarchicalCluster;
    }

    public HierarchicalCluster getHierarchicalCluster() {
        return hierarchicalCluster;
    }

    public HierarchicalCluster getHierarchicalCluster(String clusterName) {
        return hierarchicalCluster.getHierarchicalSubCluster(clusterName);
    }

    @Override
    public int getZoomStepSize() {
        return clusterLevels.size();
    }

    /**
     * The height/width
     */
    public void setSize(int size) {

        int change =0;
        if (this.size < size) {
            change = 1;
        } else if (this.size > size) {
            change = -1;
        }

        if (change != 0) {
            for (HeatmapColoredLabelsHeader level: clusterLevels) {
                if (level.getSize() + change < 3) {
                    continue;
                }
                level.setSize(level.getSize() + change);
            }
        }

        int old = this.size;
        this.size = getSize();
        firePropertyChange(PROPERTY_SIZE, old, this.size);


    }

    public void removeMetadata() {
        AnnotationMatrix annotation = getHeatmapDimension().getAnnotations();
        for (HeatmapColoredLabelsHeader level : getClusterLevels()) {
            //annotation.removeAnnotations(level.getAnnotationPattern());

            for (TextPattern.Token token : new TextPattern(level.getAnnotationPattern()).getTokens()) {
                if (token instanceof TextPattern.VariableToken) {
                    annotation.removeAnnotations(((TextPattern.VariableToken) token).getVariableName());
                }
            }
        }
        clusterLevels.clear();
        annotation.getLabels();
    }
}
