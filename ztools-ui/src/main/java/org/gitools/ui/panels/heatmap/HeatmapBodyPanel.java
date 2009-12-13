package org.gitools.ui.panels.heatmap;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import org.gitools.heatmap.HeatmapBodyDrawer;
import org.gitools.model.figure.heatmap.Heatmap;

public class HeatmapBodyPanel extends AbstractHeatmapPanel implements Scrollable {

	private static final long serialVersionUID = 930370133535101914L;

	//private HeatmapFigure heatmap;
	
	//private HeatmapBodyDrawer drawer;
		
	public HeatmapBodyPanel(Heatmap heatmap) {
		super(heatmap, new HeatmapBodyDrawer(heatmap));
	}

	@Override
	public int getScrollableBlockIncrement(
			Rectangle visibleRect, int orientation, int direction) {
		
		int gridSize = heatmap.isShowGrid() ? 1 : 0;
		int size = orientation == SwingConstants.HORIZONTAL ?
			heatmap.getCellWidth() : heatmap.getCellHeight();
		return size + gridSize;
	}

	@Override
	public int getScrollableUnitIncrement(
			Rectangle visibleRect, int orientation, int direction) {
		
		return 1;
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		Dimension sz = getPreferredSize();
		return sz;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}
	
}
