/*
 *  Copyright 2009 cperez.
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

package org.gitools.ui.view.properties;

import javax.swing.JComponent;
import org.gitools.model.figure.heatmap.Heatmap;
import org.gitools.ui.view.entity.EntityController;

class HeatmapPropertiesController implements EntityController {

	private HeatmapPropertiesPanel panel;

	@Override
	public JComponent getComponent(Object context) {
		Heatmap heatmap = (Heatmap) context;

		HeatmapPropertiesPanel component = getPanel();
		if (component.getHeatmap() != heatmap)
			component.setHeatmap(heatmap);
		
		return component;
	}

	protected HeatmapPropertiesPanel getPanel() {
		if (panel == null)
			panel = new HeatmapPropertiesPanel();
		
		return panel;
	}

}
