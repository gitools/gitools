/*
 *  Copyright 2011 chris.
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
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.model.AbstractModel;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;

/**
 * Represents either row or column properties of a heatmap
 */
public class HeatmapDim extends AbstractModel {

	public static final String LABELS_HEADER_CHANGED = "labelsHeader";
	public static final String HEADER_SIZE_CHANGED = "headerSize";
	public static final String CLUSTER_SETS_CHANGED = "clusterSets";
	public static final String GRID_PROPERTY_CHANGED = "gridProperty";
	public static final String ANNOTATIONS_CHANGED = "annotations";

	private HeatmapLabelsHeader labelsHeader;

	private HeatmapClustersHeader clustersHeader;

	private boolean gridEnabled;

	private int gridSize;

	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	private Color gridColor;

	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected AnnotationMatrix annotations;

	PropertyChangeListener propertyListener;

	public HeatmapDim() {
		propertyListener = new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				HeatmapDim.this.propertyChange(evt); } };

		labelsHeader = new HeatmapLabelsHeader(this);
		labelsHeader.addPropertyChangeListener(propertyListener);

		clustersHeader = new HeatmapClustersHeader();
		clustersHeader.addPropertyChangeListener(propertyListener);
		
		gridEnabled = true;
		gridSize = 1;
		gridColor = Color.WHITE;
	}

	private void propertyChange(PropertyChangeEvent evt) {
		Object src = evt.getSource();
		String pname = evt.getPropertyName();

		//System.out.println(getClass().getSimpleName() + " " + src + " " + pname);

		if (src == labelsHeader || src == clustersHeader
				&& HeatmapHeader.SIZE_CHANGED.equals(pname))
			firePropertyChange(HEADER_SIZE_CHANGED);
		
		firePropertyChange(evt);
	}

	public int getHeaderSize() {
		int size = labelsHeader.getSize();
		size += clustersHeader.getSize();
		return size;
	}

	@Deprecated // Remove it
	public void setHeaderSize(int headerSize) {
		/*int old = this.headerSize;
		this.headerSize = headerSize;
		firePropertyChange(HEADER_SIZE_CHANGED, old, headerSize);*/
	}

	public HeatmapLabelsHeader getLabelsHeader() {
		return labelsHeader;
	}

	public void setLabelsHeader(HeatmapLabelsHeader labelsHeader) {
		this.labelsHeader.removePropertyChangeListener(propertyListener);
		labelsHeader.addPropertyChangeListener(propertyListener);
		final HeatmapLabelsHeader old = this.labelsHeader;
		this.labelsHeader = labelsHeader;
		labelsHeader.setHeatmapDim(this);
		firePropertyChange(LABELS_HEADER_CHANGED, old, labelsHeader);
	}

	public HeatmapClustersHeader getClustersHeader() {
		return clustersHeader;
	}

	public void setClustersHeader(HeatmapClustersHeader clustersHeader) {
		this.clustersHeader.removePropertyChangeListener(propertyListener);
		clustersHeader.removePropertyChangeListener(propertyListener);
		HeatmapClustersHeader old = this.clustersHeader;
		this.clustersHeader = clustersHeader;
		clustersHeader.setHeatmapDim(this);
		firePropertyChange(CLUSTER_SETS_CHANGED, old, clustersHeader);
	}

	public boolean isGridEnabled() {
		return gridEnabled;
	}

	public void setGridEnabled(boolean enabled) {
		boolean old = this.gridEnabled;
		this.gridEnabled = enabled;
		firePropertyChange(GRID_PROPERTY_CHANGED, old, enabled);
	}

	public int getGridSize() {
		return gridSize;
	}

	public void setGridSize(int gridSize) {
		int old = this.gridSize;
		this.gridSize = gridSize;
		firePropertyChange(GRID_PROPERTY_CHANGED, old, gridSize);
	}

	public Color getGridColor() {
		return gridColor;
	}

	public void setGridColor(Color gridColor) {
		Color old = this.gridColor;
		this.gridColor = gridColor;
		firePropertyChange(GRID_PROPERTY_CHANGED, old, gridColor);
	}

	public AnnotationMatrix getAnnotations() {
		return annotations;
	}

	public void setAnnotations(AnnotationMatrix annotations) {
		AnnotationMatrix old = this.annotations;
		this.annotations = annotations;
		firePropertyChange(ANNOTATIONS_CHANGED, old, annotations);
	}
}
