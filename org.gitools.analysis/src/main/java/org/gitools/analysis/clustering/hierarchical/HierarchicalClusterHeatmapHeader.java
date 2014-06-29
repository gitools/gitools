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
package org.gitools.analysis.clustering.hierarchical;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapHeader;

import javax.xml.bind.annotation.*;
import java.util.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class HierarchicalClusterHeatmapHeader extends HeatmapHeader {

    private List<HeatmapColoredLabelsHeader> levels;

    @XmlTransient
    private int interactionLevel = -1;
    private boolean reportAllLevels;

    @XmlTransient
    private HierarchicalCluster hierarchicalCluster;

    public HierarchicalClusterHeatmapHeader() {
        super();
    }

    public HierarchicalClusterHeatmapHeader(HeatmapDimension hdim) {
        super(hdim);
        levels = new ArrayList<>();
        reportAllLevels = false;
    }

    public void addLevel(HeatmapColoredLabelsHeader level) {
        levels.add(level);
    }

    public List<HeatmapColoredLabelsHeader> getLevels() {
        return levels;
    }

    /* The thickness of the color band */
    public int getThickness() {
        return levels.size() > 0 ? levels.get(0).getThickness() : 1;
    }

    /* The thickness of the color band */
    public void setThickness(int thickness) {
        for (HeatmapColoredLabelsHeader level : levels) {
            level.setThickness(thickness);
        }
    }

    /* Separate different clusters with a grid */
    public boolean isSeparationGrid() {
        return levels.size() > 0 ? levels.get(0).isSeparationGrid() : true;
    }

    /* Separate different clusters with a grid */
    public void setSeparationGrid(boolean separationGrid) {
        for (HeatmapColoredLabelsHeader level : levels) {
            level.setSeparationGrid(separationGrid);
        }
    }

    public boolean isForceLabelColor() {
        return levels.size() > 0 ? levels.get(0).isForceLabelColor() : true;
    }

    public void setForceLabelColor(boolean forceLabelColor) {
        for (HeatmapColoredLabelsHeader level : levels) {
            level.setSeparationGrid(forceLabelColor);
        }
    }

    @Override
    public int getSize() {
        int size = 0;
        for (HeatmapColoredLabelsHeader level : levels) {
            size += level.getSize();
        }
        return size;
    }

    @Override
    public void populateDetails(List<DetailsDecoration> details, String identifier, boolean selected) {

        for (HeatmapColoredLabelsHeader level : Lists.reverse(levels)) {
            if (isReportAllLevels() || levels.indexOf(level) == interactionLevel) {
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
        //Lists.reverse(levels).get(interactionLevel);
        return levels.get(interactionLevel);
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
            for (HeatmapColoredLabelsHeader level: levels) {
                if (level.getSize() < 3) {
                    continue;
                }
                level.setSize(level.getSize() + change);
            }
        }

        this.size = getSize();

    }
}
