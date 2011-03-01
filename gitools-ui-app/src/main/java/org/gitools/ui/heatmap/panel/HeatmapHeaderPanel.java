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

package org.gitools.ui.heatmap.panel;

import java.beans.PropertyChangeEvent;
import org.gitools.heatmap.drawer.HeatmapHeaderDrawer;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapDim;
import org.gitools.heatmap.model.HeatmapHeader;

public class HeatmapHeaderPanel extends AbstractHeatmapPanel {

	private static final long serialVersionUID = 930370133535101914L;

	private HeatmapDim hdim;

	public HeatmapHeaderPanel(Heatmap heatmap, boolean horizontal) {
		super(heatmap, new HeatmapHeaderDrawer(heatmap, horizontal));

		this.hdim = horizontal ? heatmap.getColumnDim() : heatmap.getRowDim();
	}

	private HeatmapHeaderDrawer getHeaderDrawer() {
		return (HeatmapHeaderDrawer) getDrawer();
	}

	@Override
	protected void heatmapPropertyChanged(PropertyChangeEvent evt) {
		super.heatmapPropertyChanged(evt);

		String pname = evt.getPropertyName();
		Object src = evt.getSource();

		//System.out.println(getClass().getSimpleName() + " " + src + " " + pname);

		boolean repaintFlag = false;

		if (src.equals(hdim)) {
			if (HeatmapDim.HEADERS_CHANGED.equals(pname)) {
				getHeaderDrawer().updateDrawers();
			}
			
			/*else if (HeatmapDim.HEADER_SIZE_CHANGED.equals(pname)
					|| HeatmapDim.GRID_PROPERTY_CHANGED.equals(pname))
				updateSize();*/
		}
		else if (src instanceof HeatmapHeader) {
			if (HeatmapHeader.SIZE_CHANGED.equals(pname)
					|| HeatmapHeader.VISIBLE_CHANGED.equals(pname)
					|| HeatmapHeader.BG_COLOR_CHANGED.equals(pname))
				
				repaintFlag = true;
		}

		if (repaintFlag) {
			revalidate();
			repaint();
		}
	}
}