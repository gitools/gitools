package org.gitools.ui.panels.heatmap;

import org.gitools.heatmap.drawer.HeatmapHeaderDrawer;
import org.gitools.model.figure.heatmap.Heatmap;

public class HeatmapHeaderPanel extends AbstractHeatmapPanel {

	private static final long serialVersionUID = 930370133535101914L;
	
	public HeatmapHeaderPanel(Heatmap heatmap, boolean horizontal) {
		super(heatmap, new HeatmapHeaderDrawer(heatmap, horizontal));
	}
}
