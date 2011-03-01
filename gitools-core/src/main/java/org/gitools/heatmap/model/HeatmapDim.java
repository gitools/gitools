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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.model.AbstractModel;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;

/**
 * Represents either row or column properties of a heatmap
 */
public class HeatmapDim extends AbstractModel {

	public static final String LABELS_HEADER_CHANGED = "labelsHeader";
	public static final String HEADERS_CHANGED = "headers";
	public static final String HEADER_SIZE_CHANGED = "headerSize";
	public static final String CLUSTER_SETS_CHANGED = "clusterSets";
	public static final String GRID_PROPERTY_CHANGED = "gridProperty";
	public static final String ANNOTATIONS_CHANGED = "annotations";

	@Deprecated protected HeatmapLabelsHeader labelsHeader;

	protected List<HeatmapHeader> headers;

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

		headers = new ArrayList<HeatmapHeader>();
		
		/*labelsHeader = new HeatmapLabelsHeader(this);
		labelsHeader.addPropertyChangeListener(propertyListener);
		headers.add(labelsHeader);*/

		gridEnabled = true;
		gridSize = 1;
		gridColor = Color.WHITE;
	}

	private void propertyChange(PropertyChangeEvent evt) {
		Object src = evt.getSource();
		String pname = evt.getPropertyName();

		//System.out.println(getClass().getSimpleName() + " " + src + " " + pname);

		if (src instanceof HeatmapHeader
				&& (HeatmapHeader.SIZE_CHANGED.equals(pname)
						|| HeatmapHeader.VISIBLE_CHANGED.equals(pname)))
			firePropertyChange(HEADER_SIZE_CHANGED);
		
		firePropertyChange(evt);
	}

	public List<HeatmapHeader> getHeaders() {
		return Collections.unmodifiableList(headers);
	}

	/*public void setHeaders(List<HeatmapHeader> headers) {
		List<HeatmapHeader> old = this.headers;
		this.headers = headers;
		firePropertyChange(HEADERS_CHANGED, old, headers);
	}*/

	public void addHeader(HeatmapHeader header) {
		headers.add(header);
		header.addPropertyChangeListener(propertyListener);
		firePropertyChange(HEADERS_CHANGED);
	}

	public void removeHeader(int index) {
		if (index >= headers.size())
			return;
		HeatmapHeader header = headers.get(index);
		header.removePropertyChangeListener(propertyListener);
		headers.remove(header);
		firePropertyChange(HEADERS_CHANGED);
	}

	public void upHeader(int index) {
		if (index == 0 || index >= headers.size())
			return;
		HeatmapHeader header = headers.get(index);
		headers.set(index, headers.get(index - 1));
		headers.set(index - 1, header);
		firePropertyChange(HEADERS_CHANGED);
	}

	public void downHeader(int index) {
		if (index >= headers.size() - 1)
			return;
		HeatmapHeader header = headers.get(index);
		headers.set(index, headers.get(index + 1));
		headers.set(index + 1, header);
		firePropertyChange(HEADERS_CHANGED);
	}

	public int getHeaderSize() {
		int size = 0;
		for (HeatmapHeader h : headers)
			size += h.getSize();
		return size;
	}

	/*@Deprecated // Remove it
	public void setHeaderSize(int headerSize) {
		int old = this.headerSize;
		this.headerSize = headerSize;
		firePropertyChange(HEADER_SIZE_CHANGED, old, headerSize);
	}*/

	@Deprecated
	public HeatmapLabelsHeader getLabelsHeader() {
		return labelsHeader;
	}

	/*@Deprecated
	public void setLabelsHeader(HeatmapLabelsHeader labelsHeader) {
		this.labelsHeader.removePropertyChangeListener(propertyListener);
		labelsHeader.addPropertyChangeListener(propertyListener);
		final HeatmapLabelsHeader old = this.labelsHeader;
		this.labelsHeader = labelsHeader;
		labelsHeader.setHeatmapDim(this);
		firePropertyChange(LABELS_HEADER_CHANGED, old, labelsHeader);
	}*/

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
