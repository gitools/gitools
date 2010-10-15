package org.gitools.ui.heatmap.panel;

import org.gitools.heatmap.drawer.HeatmapColorAnnDrawer;
import org.gitools.heatmap.model.Heatmap;

public class HeatmapColorAnnPanel extends AbstractHeatmapPanel {


	public HeatmapColorAnnPanel(Heatmap heatmap, boolean horizontal) {
		super(heatmap, new HeatmapColorAnnDrawer(heatmap, horizontal));
	}
}
