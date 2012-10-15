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

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.drawer.header.HeatmapHeaderDrawer;
import org.gitools.heatmap.drawer.header.HeatmapHeaderIntersectionDrawer;
import org.gitools.heatmap.header.HeatmapHeader;

import java.beans.PropertyChangeEvent;

public class HeatmapHeaderIntersectionPanel extends AbstractHeatmapPanel {

	private static final long serialVersionUID = 930370133535101914L;

	public HeatmapHeaderIntersectionPanel(Heatmap heatmap, HeatmapHeaderDrawer columnHeaderDrawer,HeatmapHeaderDrawer rowHeaderDrawer) {
		super(heatmap, new HeatmapHeaderIntersectionDrawer(heatmap, columnHeaderDrawer, rowHeaderDrawer));

	}



    private HeatmapHeaderIntersectionDrawer getHeaderDrawer() {
		return (HeatmapHeaderIntersectionDrawer) getDrawer();
	}

	@Override
	protected void heatmapPropertyChanged(PropertyChangeEvent evt) {
		super.heatmapPropertyChanged(evt);

		String pname = evt.getPropertyName();
		Object src = evt.getSource();


        if (src instanceof HeatmapDim && HeatmapDim.HEADERS_CHANGED.equals(pname))
            getHeaderDrawer().updateDrawers((HeatmapDim) src);
        else if (src instanceof HeatmapHeader && HeatmapHeader.VISIBLE_CHANGED.equals(pname))
            getHeaderDrawer().updateDrawers(((HeatmapHeader) src).getHeatmapDim());
	}
}

