package org.gitools.heatmap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.gitools.model.figure.MatrixFigure;

public class HeatmapDrawer {

	private MatrixFigure heatmap;
	
	public HeatmapDrawer() {
	}

	public HeatmapDrawer(MatrixFigure heatmap) {
		this.heatmap = heatmap;
	}
	
	public MatrixFigure getHeatmap() {
		return heatmap;
	}
	
	public void setHeatmap(MatrixFigure heatmap) {
		this.heatmap = heatmap;
	}
	
	public void draw(Graphics2D g) {
		Rectangle clipRect = g.getClipBounds();
		
		Rectangle colHeaderRect = new Rectangle(
				clipRect.x,
				clipRect.y,
				clipRect.width - heatmap.getRowHeaderSize(),
				heatmap.getColumnHeaderSize());
		
		Rectangle rowHeaderRect = new Rectangle(
				clipRect.x + clipRect.width - heatmap.getRowHeaderSize(),
				clipRect.y,
				heatmap.getRowHeaderSize(),
				clipRect.height - heatmap.getColumnHeaderSize());
		
		Rectangle bodyRect = new Rectangle(
				clipRect.x,
				clipRect.y + heatmap.getColumnHeaderSize(),
				clipRect.width - heatmap.getRowHeaderSize(),
				clipRect.height - heatmap.getColumnHeaderSize());
		
		g.setClip(clipRect.x, clipRect.y, clipRect.width, clipRect.height);
		drawBody(g, heatmap);
	}

	public void drawBody(Graphics2D g, MatrixFigure heatmap) {
		Rectangle clip = g.getClipBounds();
		int xMax = clip.x + clip.width - 1;
		int yMax = clip.y + clip.height - 1;
		
		// Draw borders and grid background
		g.setColor(Color.BLACK);
		g.setBackground(heatmap.getGridColor());
		g.fillRect(clip.x, clip.y, clip.width, clip.height);
		clip.grow(-1, -1);
		
		int firstRow = 0;
		int lastRow = heatmap.getMatrixView().getRowCount();
		for (int row = firstRow; row <= lastRow; row++) {
			
		}
	}
	
}
