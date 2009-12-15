package org.gitools.ui.panels.heatmap;

import org.gitools.heatmap.drawer.HeatmapBodyDrawer;
import org.gitools.model.figure.heatmap.Heatmap;

public class HeatmapBodyPanel extends AbstractHeatmapPanel {

	private static final long serialVersionUID = 930370133535101914L;
		
	public HeatmapBodyPanel(Heatmap heatmap) {
		super(heatmap, new HeatmapBodyDrawer(heatmap));
	}
}
