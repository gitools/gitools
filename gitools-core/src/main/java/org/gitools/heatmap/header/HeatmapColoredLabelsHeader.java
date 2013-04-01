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

import org.gitools.clustering.ClusteringResults;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.utils.color.generator.ColorGenerator;
import org.gitools.utils.color.generator.ColorGeneratorFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HeatmapColoredLabelsHeader extends HeatmapHeader
{

    public static final String THICKNESS_CHANGED = "thickness";
    public static final String SEPARATION_GRID_CHANGED = "separationGrid";
    public static final String FORCE_LABEL_COLOR = "forceLabelColor";
    public static final String CLUSTERS_CHANGED = "clusters";
    public static final String INDICES_CHANGED = "indices";

    /* The thickness of the color band */
    protected int thickness;

    /* Separate different clusters with a grid */
    protected boolean separationGrid;

    protected boolean forceLabelColor;

    /**
     * The list of clusters in this set
     */
    protected ColoredLabel[] coloredLabels;

    /**
     * Maps matrix row/column id to cluster index
     */
    protected Map<String, Integer> dataColoredLabelIndices;

    public HeatmapColoredLabelsHeader(HeatmapDim hdim)
    {
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
        dataColoredLabelIndices = new HashMap<String, Integer>();
    }

    @Override
    protected void updateLargestLabelLength(Component component)
    {
        this.largestLabelLength = 0;
    }

    /* The thickness of the color band */
    public int getThickness()
    {
        return thickness;
    }

    /* The thickness of the color band */
    public void setThickness(int thickness)
    {
        int old = this.thickness;
        this.thickness = thickness;
        firePropertyChange(THICKNESS_CHANGED, old, thickness);
    }

    /* Separate different clusters with a grid */
    public boolean isSeparationGrid()
    {
        return separationGrid;
    }

    /* Separate different clusters with a grid */
    public void setSeparationGrid(boolean separationGrid)
    {
        boolean old = this.separationGrid;
        this.separationGrid = separationGrid;
        firePropertyChange(SEPARATION_GRID_CHANGED, old, separationGrid);
    }

    /**
     * The list of clusters in this set
     */
    public ColoredLabel[] getClusters()
    {
        return coloredLabels;
    }

    /**
     * The list of clusters in this set
     */
    public void setClusters(ColoredLabel[] clusters)
    {
        ColoredLabel[] old = this.coloredLabels;
        this.coloredLabels = clusters;
        firePropertyChange(CLUSTERS_CHANGED, old, clusters);
    }

    /**
     * Return the corresponding matrix row/column cluster. Null if there is not cluster assigned.
     */
    public ColoredLabel getAssignedColoredLabel(String id)
    {
        Integer index = dataColoredLabelIndices.get(id);
        if (index == null)
        {
            return null;
        }
        return coloredLabels[index];
    }

    public void setAssignedColoredLabels(Map<String, Integer> assigned)
    {
        this.dataColoredLabelIndices = new HashMap<String, Integer>(assigned);
        firePropertyChange(INDICES_CHANGED);
    }

    public Map<String, Integer> getAssignedColoredLabels()
    {
        return this.dataColoredLabelIndices;
    }

    /**
     * Set the corresponding matrix row/column cluster. -1 if there is not cluster assigned.
     */
    public void setAssignedColoredLabel(String id, int clusterIndex)
    {
        ColoredLabel old = getAssignedColoredLabel(id);
        if (old != null && clusterIndex == -1)
        {
            this.dataColoredLabelIndices.remove(id);
        }
        else
        {
            this.dataColoredLabelIndices.put(id, clusterIndex);
        }

        ColoredLabel newCluster = clusterIndex != -1 ? coloredLabels[clusterIndex] : null;
        firePropertyChange(INDICES_CHANGED, old, newCluster);
    }

    /**
     * Clear all assigned clusters
     */
    public void clearAssignedColoredLabels()
    {
        this.dataColoredLabelIndices.clear();
        firePropertyChange(INDICES_CHANGED);
    }

    public boolean isForceLabelColor()
    {
        return forceLabelColor;
    }

    /*Use the same color for all labels instead of inverted*/
    public void setForceLabelColor(boolean forceLabelColor)
    {
        boolean old = this.forceLabelColor;
        this.forceLabelColor = forceLabelColor;
        firePropertyChange(FORCE_LABEL_COLOR, old, forceLabelColor);
    }

    public void updateFromClusterResults(ClusteringResults results)
    {
        ColorGenerator cg = ColorGeneratorFactory.getDefault().create();

        String[] clusterTitles = results.getClusterTitles();
        coloredLabels = new ColoredLabel[results.getNumClusters()];
        for (int i = 0; i < results.getNumClusters(); i++)
        {
            ColoredLabel cluster = coloredLabels[i]
                    = new ColoredLabel(clusterTitles[i],
                    cg.next());
        }

        dataColoredLabelIndices = new HashMap<String, Integer>();
        for (String label : results.getDataLabels())
            dataColoredLabelIndices.put(label, results.getClusterIndex(label));
    }
}
