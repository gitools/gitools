/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.heatmap.header;

import edu.upf.bg.color.generator.ColorGenerator;
import edu.upf.bg.color.generator.ColorGeneratorFactory;
import edu.upf.bg.xml.adapter.ColorXmlAdapter;
import edu.upf.bg.xml.adapter.FontXmlAdapter;
import org.gitools.clustering.ClusteringResults;
import org.gitools.heatmap.HeatmapDim;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HeatmapDataLabelsHeader extends HeatmapHeader {

	public static final String THICKNESS_CHANGED = "thickness";
	public static final String MARGIN_CHANGED = "margin";
	public static final String SEPARATION_GRID_CHANGED = "separationGrid";
	public static final String LABEL_VISIBLE_CHANGED = "labelVisible";
	public static final String LABEL_FONT_CHANGED = "labelFont";
	public static final String LABEL_ROTATED_CHANGED = "labelRotated";
	public static final String LABEL_COLOR_DEFINED_CHANGED = "labelColorDefined";
	public static final String LABEL_COLOR_CHANGED = "labelColor";
	public static final String CLUSTERS_CHANGED = "clusters";
	public static final String INDICES_CHANGED = "indices";

    /* The thickness of the color band */
	protected int thickness;

	/* Color band margin */
	protected int margin;

	/* Separate different clusters with a grid */
	protected boolean separationGrid;

	/** Whether to show labels of each cluster */
	protected boolean labelVisible;

	/** The font to use for labels */
	@XmlJavaTypeAdapter(FontXmlAdapter.class)
	protected Font font;

	/** If false the label is painted along the color band,
	 * otherwise the label is perpendicular to the color band */
	protected boolean labelRotated;

	/** Instead of use the same color as the cluster use a defined color */
	protected boolean labelColorDefined;

	/** Label foreground color */
	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color labelColor;

	/** The list of clusters in this set */
	protected ColoredLabel[] coloredLabels;

	/** Maps matrix row/column id to cluster index */
	protected Map<String, Integer> dataColoredLabelIndices;

	public HeatmapDataLabelsHeader(HeatmapDim hdim) {
		super(hdim);
		
		size = 20;
		thickness = 14;
		margin = 1;
		separationGrid = true;

		labelVisible = false;
		font = new Font(Font.MONOSPACED, Font.PLAIN, 9);
		labelRotated = false;
		labelColorDefined = false;
		labelColor = Color.BLACK;

		coloredLabels = new ColoredLabel[0];
		dataColoredLabelIndices = new HashMap<String, Integer>();
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

	/* Color band margin */
	public int getMargin() {
		return margin;
	}

	/* Color band margin */
	public void setMargin(int margin) {
		int old = this.margin;
		this.margin = margin;
		firePropertyChange(MARGIN_CHANGED, old, margin);
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

	/** Whether to show labels of each cluster */
	public boolean isLabelVisible() {
		return labelVisible;
	}

	/** Whether to show labels of each cluster */
	public void setLabelVisible(boolean labelVisible) {
		boolean old = this.labelVisible;
		this.labelVisible = labelVisible;
		firePropertyChange(LABEL_VISIBLE_CHANGED, old, labelVisible);
	}

	/** The font to use for labels */
	public Font getLabelFont() {
		return font;
	}

	/** The font to use for labels */
	public void setLabelFont(Font font) {
		Font old = this.font;
		this.font = font;
		firePropertyChange(LABEL_FONT_CHANGED, old, font);
	}

	/** If false the label is painted along the color band,
	 * otherwise the label is perpendicular to the color band */
	public boolean isLabelRotated() {
		return labelRotated;
	}

	/** If false the label is painted along the color band,
	 * otherwise the label is perpendicular to the color band */
	public void setLabelRotated(boolean labelRotated) {
		boolean old = this.labelRotated;
		this.labelRotated = labelRotated;
		firePropertyChange(LABEL_ROTATED_CHANGED, old, labelRotated);
	}

	/** Instead of use the same color as the cluster use a defined color */
	public boolean isLabelColorDefined() {
		return labelColorDefined;
	}

	/** Instead of use the same color as the cluster use a defined color */
	public void setLabelColorDefined(boolean labelColorDefined) {
		boolean old = this.labelColorDefined;
		this.labelColorDefined = labelColorDefined;
		firePropertyChange(LABEL_COLOR_DEFINED_CHANGED, old, labelColorDefined);
	}

	/** Label foreground color */
	public Color getLabelColor() {
		return labelColor;
	}

	/** Label foreground color */
	public void setLabelColor(Color labelColor) {
		Color old = this.labelColor;
		this.labelColor = labelColor;
		firePropertyChange(LABEL_COLOR_CHANGED, old, labelColor);
	}

	/** The list of clusters in this set */
	public ColoredLabel[] getClusters() {
		return coloredLabels;
	}

	/** The list of clusters in this set */
	public void setClusters(ColoredLabel[] clusters) {
		ColoredLabel[] old = this.coloredLabels;
		this.coloredLabels = clusters;
		firePropertyChange(CLUSTERS_CHANGED, old, clusters);
	}

	/** Return the corresponding matrix row/column cluster. Null if there is not cluster assigned. */
	public ColoredLabel getAssignedColoredLabel(String id) {
		Integer index = dataColoredLabelIndices.get(id);
		if (index == null)
			return null;
		return coloredLabels[index];
	}

	public void setAssignedColoredLabels(Map<String, Integer> assigned) {
		this.dataColoredLabelIndices = new HashMap<String, Integer>(assigned);
		firePropertyChange(INDICES_CHANGED);
	}

	public Map<String, Integer> getAssignedColoredLabels() {
		return this.dataColoredLabelIndices;
	}

	/** Set the corresponding matrix row/column cluster. -1 if there is not cluster assigned. */
	public void setAssignedColoredLabel(String id, int clusterIndex) {
		ColoredLabel old = getAssignedColoredLabel(id);
		if (old != null && clusterIndex == -1)
			this.dataColoredLabelIndices.remove(id);
		else
			this.dataColoredLabelIndices.put(id, clusterIndex);
		
		ColoredLabel newCluster = clusterIndex != -1 ? coloredLabels[clusterIndex] : null;
		firePropertyChange(INDICES_CHANGED, old, newCluster);
	}

	/** Clear all assigned clusters */
	public void clearAssignedColoredLabels() {
		this.dataColoredLabelIndices.clear();
		firePropertyChange(INDICES_CHANGED);
	}
}
