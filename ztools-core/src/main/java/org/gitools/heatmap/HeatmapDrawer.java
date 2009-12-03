package org.gitools.heatmap;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.gitools.model.figure.MatrixFigure;

public class HeatmapDrawer extends AbstractDrawer {

	public HeatmapDrawer(MatrixFigure heatmap) {
		super(heatmap);
	}

	@Override
	public void draw(Graphics2D g, Rectangle box, Rectangle clip) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Dimension getSize() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
