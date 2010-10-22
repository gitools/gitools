/*
 *  Copyright 2009 chris.
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

package org.gitools.ui.heatmap.panel.properties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.gitools.heatmap.model.Heatmap;

public abstract class HeatmapPropertiesAbstractPanel extends javax.swing.JPanel {

	protected Heatmap hm;
	private PropertyChangeListener heatmapListener;

	protected boolean updatingControls = false;

	public HeatmapPropertiesAbstractPanel() {
		heatmapListener = new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				heatmapPropertyChange(evt);
			}
		};
	}

	public Heatmap getHeatmap() {
		return hm;
	}

	public void setHeatmap(Heatmap heatmap) {
		if (this.hm == heatmap)
			return;
		
		if (heatmap != null) {
			heatmap.removePropertyChangeListener(heatmapListener);
			heatmap.getColumnHeader().removePropertyChangeListener(heatmapListener);
			heatmap.getRowHeader().removePropertyChangeListener(heatmapListener);
			heatmap.getMatrixView().removePropertyChangeListener(heatmapListener);
		}

		this.hm = heatmap;

		updateControls();
		initControls();

		heatmap.addPropertyChangeListener(heatmapListener);
		heatmap.getColumnHeader().addPropertyChangeListener(heatmapListener);
		heatmap.getRowHeader().addPropertyChangeListener(heatmapListener);
		heatmap.getMatrixView().addPropertyChangeListener(heatmapListener);
	}

	protected void heatmapPropertyChange(PropertyChangeEvent evt) {}

	protected void initControls() {}

	protected void updateControls() {}
}
