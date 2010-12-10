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

package org.gitools.heatmap.model;

import edu.upf.bg.xml.adapter.ColorXmlAdapter;
import edu.upf.bg.xml.adapter.FontXmlAdapter;
import java.awt.Color;
import java.awt.Font;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.model.AbstractModel;

public class HeatmapClusterSet extends AbstractModel {

	public static final String TITLE_CHANGED = "title";
	public static final String CLUSTERS_CHANGED = "clusters";
	public static final String INDICES_CHANGED = "indices";
	public static final String SIZE_CHANGED = "size";
	public static final String LABEL_VISIBLE_CHANGED = "labelVisible";
	public static final String LABEL_ROTATED_CHANGED = "labelRotated";
	public static final String FOREGROUND_COLOR_CHANGED = "fgColor";
	public static final String BACKGROUND_COLOR_CHANGED = "bgColor";
	public static final String FONT_CHANGED = "font";

	/** The title of the cluster set */
	protected String title;

	/** The list of clusters in this set */
	protected HeatmapCluster[] clusters;

	/** The row/column in IMatrix.getContent() at index i
	 * is assigned to the HeatmapCluster clusters.get(clusterIndices[i]) */
	protected int[] clusterIndices;

	/** The height/width of the color band */
	protected int size;

	/** Wether the cluster set is visible */
	protected boolean visible;

	/** Whether to show labels of each cluster */
	protected boolean labelVisible;

	/** If false the label is painted along the color band,
	 * otherwise the label is perpendicular to the color band */
	protected boolean labelRotated;

	/** Label foreground color */
	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color foregroundColor;

	/** Label background color */
	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	protected Color backgroundColor;

	/** The font to use for labels */
	@XmlJavaTypeAdapter(FontXmlAdapter.class)
	protected Font font;

	public HeatmapClusterSet() {
		size = 20;
		labelVisible = false;
		labelRotated = false;
		foregroundColor = Color.BLACK;
		backgroundColor = Color.WHITE;
		font = new Font(Font.MONOSPACED, Font.PLAIN, 9);
	}

	/** The title of the cluster set */
	public String getTitle() {
		return title;
	}

	/** The title of the cluster set */
	public void setTitle(String title) {
		String old = this.title;
		this.title = title;
		firePropertyChange(TITLE_CHANGED, old, title);
	}

	/** The list of clusters in this set */
	public HeatmapCluster[] getClusters() {
		return clusters;
	}

	/** The list of clusters in this set */
	public void setClusters(HeatmapCluster[] clusters) {
		HeatmapCluster[] old = this.clusters;
		this.clusters = clusters;
		firePropertyChange(CLUSTERS_CHANGED, old, clusters);
	}

	/** The row/column in IMatrix.getContent() at index i
	 * is assigned to the HeatmapCluster clusters.get(clusterIndices[i]) */
	public int[] getClusterIndices() {
		return clusterIndices;
	}

	/** The row/column in IMatrix.getContent() at index i
	 * is assigned to the HeatmapCluster clusters.get(clusterIndices[i]) */
	public void setClusterIndices(int[] clusterIndices) {
		int[] old = this.clusterIndices;
		this.clusterIndices = clusterIndices;
		firePropertyChange(INDICES_CHANGED, old, clusterIndices);
	}

	/** The height/width of the color band */
	public int getSize() {
		return size;
	}

	/** The height/width of the color band */
	public void setSize(int size) {
		int old = this.size;
		this.size = size;
		firePropertyChange(SIZE_CHANGED, old, size);
	}

	/** Wether the cluster set is visible */
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
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

	/** Label foreground color */
	public Color getForegroundColor() {
		return foregroundColor;
	}

	/** Label foreground color */
	public void setForegroundColor(Color foregroundColor) {
		Color old = this.foregroundColor;
		this.foregroundColor = foregroundColor;
		firePropertyChange(FOREGROUND_COLOR_CHANGED, old, foregroundColor);
	}

	/** Label background color */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/** Label background color */
	public void setBackgroundColor(Color backgroundColor) {
		Color old = this.backgroundColor;
		this.backgroundColor = backgroundColor;
		firePropertyChange(BACKGROUND_COLOR_CHANGED, old, backgroundColor);
	}

	/** The font to use for labels */
	public Font getFont() {
		return font;
	}

	/** The font to use for labels */
	public void setFont(Font font) {
		Font old = this.font;
		this.font = font;
		firePropertyChange(FONT_CHANGED, old, font);
	}

}
