/*
 *  Copyright 2011 Univarsitat Pompeu Fabra.
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class HeatmapClustersHeader extends HeatmapHeader {

	public static final String BANDS_CHANGED = "bands";

	private List<HeatmapClusterBand> bands;

	private PropertyChangeListener propertyListener;

	public HeatmapClustersHeader() {
		propertyListener = new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				propertyChange(evt); } };

		bands = new ArrayList<HeatmapClusterBand>();
	}
	
	@Override
	public int getSize() {
		int sz = 0;
		for (HeatmapClusterBand h : bands)
			sz += h.getSize();
		return sz;
	}

	@Override
	public void setHeatmapDim(HeatmapDim dim) {
		super.setHeatmapDim(dim);

		for (HeatmapClusterBand b : bands)
			b.setHeatmapDim(dim);
	}

	public List<HeatmapClusterBand> getClusterBands() {
		return bands;
	}

	public void setClusterBands(List<HeatmapClusterBand> bands) {
		for (HeatmapClusterBand b : this.bands)
			b.removePropertyChangeListener(propertyListener);

		for (HeatmapClusterBand b : bands)
			b.addPropertyChangeListener(propertyListener);

		List<HeatmapClusterBand> old = this.bands;
		this.bands = bands;
		firePropertyChange(BANDS_CHANGED, old, bands);
	}
}
