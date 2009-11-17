package org.gitools.heatmap;

import java.awt.Graphics2D;

import org.gitools.model.figure.MatrixFigure;

public class HeatmapColumnHeaderDrawer {

	private MatrixFigure heatmap;
	
	public HeatmapColumnHeaderDrawer(MatrixFigure heatmap) {
		this.heatmap = heatmap;
	}
	
	public MatrixFigure getHeatmap() {
		return heatmap;
	}
	
	public void setHeatmap(MatrixFigure heatmap) {
		this.heatmap = heatmap;
	}
	
	public void draw(Graphics2D g) {
		
	}
}
