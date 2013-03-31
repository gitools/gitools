/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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

package org.gitools.heatmap;

import org.gitools.utils.xml.adapter.ColorXmlAdapter;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.idtype.IdType;
import org.gitools.idtype.IdTypeManager;
import org.gitools.idtype.IdTypeXmlAdapter;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.model.AbstractModel;
import org.gitools.persistence.formats.analysis.adapter.PersistenceReferenceXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

/**
 * Represents either row or column properties of a heatmap
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HeatmapDim extends AbstractModel implements PropertyChangeListener {

	public static final String IDTYPE_CHANGED = "idType";
	public static final String HEADERS_CHANGED = "headers";
	public static final String HEADER_SIZE_CHANGED = "headerSize";
	public static final String CLUSTER_SETS_CHANGED = "clusterSets";
	public static final String GRID_PROPERTY_CHANGED = "gridProperty";
	public static final String ANNOTATIONS_CHANGED = "annotations";
	public static final String HIGHLIGHTING_CHANGED = "highlighting";

	@XmlJavaTypeAdapter(IdTypeXmlAdapter.class)
	protected IdType idType;

	@XmlJavaTypeAdapter(PersistenceReferenceXmlAdapter.class)
	protected AnnotationMatrix annotations;
	
	@XmlTransient
	protected List<HeatmapHeader> headers;

	private boolean gridEnabled;

	private int gridSize;

	@XmlJavaTypeAdapter(ColorXmlAdapter.class)
	private Color gridColor;

	@XmlTransient
	private Set<String> highlightedLabels;

	public HeatmapDim() {

		idType = IdTypeManager.getDefault().getDefaultIdType();

		headers = new ArrayList<HeatmapHeader>();
		
		gridEnabled = true;
		gridSize = 1;
		gridColor = Color.WHITE;

		highlightedLabels = new HashSet<String>();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		Object src = evt.getSource();
		String pname = evt.getPropertyName();

		//System.out.println(getClass().getSimpleName() + " " + src + " " + pname);

		if (src instanceof HeatmapHeader
				&& (HeatmapHeader.SIZE_CHANGED.equals(pname)
						|| HeatmapHeader.VISIBLE_CHANGED.equals(pname)))
			firePropertyChange(HEADER_SIZE_CHANGED);
		
		firePropertyChange(evt);
	}

	public IdType getIdType() {
		return idType;
	}

	public void setIdType(IdType idType) {
		IdType old = this.idType;
		this.idType = idType;
		firePropertyChange(IDTYPE_CHANGED, old, idType);
	}
	
	public List<HeatmapHeader> getHeaders() {
		return Collections.unmodifiableList(headers);
	}

	public void addHeader(HeatmapHeader header) {
        if (header.getHeatmapDim() == null)
            header.setHeatmapDim(this);
		headers.add(header);
		header.addPropertyChangeListener(this);
		firePropertyChange(HEADERS_CHANGED);
	}

	public void removeHeader(int index) {
		if (index >= headers.size())
			return;
		HeatmapHeader header = headers.get(index);
		header.removePropertyChangeListener(this);
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

	public boolean isHighlighted(String label) {
		return highlightedLabels.contains(label);
	}
	
	public void setHighlightedLabels(Set<String> highlightedLabels) {
		this.highlightedLabels = highlightedLabels;
		firePropertyChange(HIGHLIGHTING_CHANGED);
	}

	public void clearHighlightedLabels() {
		highlightedLabels.clear();
		firePropertyChange(HIGHLIGHTING_CHANGED);
	}

}
