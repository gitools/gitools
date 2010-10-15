package org.gitools.ui.heatmap.panel;

import org.gitools.heatmap.drawer.HeatmapHeaderDrawer;
import org.gitools.heatmap.model.Heatmap;

public class HeatmapHeaderPanel extends AbstractHeatmapPanel {

	private static final long serialVersionUID = 930370133535101914L;
	
	public HeatmapHeaderPanel(Heatmap heatmap, boolean horizontal) {
		super(heatmap, new HeatmapHeaderDrawer(heatmap, horizontal));
	}
}
