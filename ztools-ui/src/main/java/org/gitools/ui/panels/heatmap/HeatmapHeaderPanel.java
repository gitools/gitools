package org.gitools.ui.panels.heatmap;

import org.gitools.heatmap.HeatmapHeaderDrawer;
import org.gitools.model.figure.HeatmapFigure;

public class HeatmapHeaderPanel extends AbstractHeatmapPanel {

	private static final long serialVersionUID = 930370133535101914L;
	
	public HeatmapHeaderPanel(HeatmapFigure heatmap, boolean horizontal) {
		super(heatmap, new HeatmapHeaderDrawer(heatmap, horizontal));
	}
}
