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

import javax.xml.bind.annotation.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class HierarchicalClusterHeatmapHeader extends HeatmapHeader {

    @XmlElementWrapper(name="clusterLevels")
    @XmlElement(name="levels")
    private List<HeatmapColoredLabelsHeader> clusterLevels;

    @XmlTransient
    private int interactionLevel = -1;

    @XmlTransient
    private boolean reportAllLevels;

    @XmlElement
    private HierarchicalCluster hierarchicalCluster;

    public HierarchicalClusterHeatmapHeader() {
        super();
        reportAllLevels = false;
    }

    public HierarchicalClusterHeatmapHeader(HeatmapDimension hdim) {
        super(hdim);
        clusterLevels = new ArrayList<>();
        reportAllLevels = false;
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

        for (HeatmapColoredLabelsHeader level : Lists.reverse(clusterLevels)) {
            if (isReportAllLevels() || clusterLevels.indexOf(level) == interactionLevel) {
                level.populateDetails(details, identifier, selected);
            }
        }
    }

    @Override
    public Function<String, String> getIdentifierTransform() {
        return null;
    }

    public void setInteractionLevel(int interactionLevel) {
        this.interactionLevel = interactionLevel;
    }

    public HeatmapColoredLabelsHeader getInteractionLevelHeader() {
        if (interactionLevel < 0) {
            return null;
        }
        return clusterLevels.get(interactionLevel);
    }

    public boolean isReportAllLevels() {
        return reportAllLevels;
    }

    public void setReportAllLevels(boolean reportAllLevels) {
        this.reportAllLevels = reportAllLevels;
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
}
