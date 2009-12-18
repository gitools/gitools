package org.gitools.ui._DEPRECATED.panels.matrix;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.gitools.heatmap.model.HeatmapHeader;

public class RowsMatrixRenderer extends HeaderMatrixRenderer {

	private static final long serialVersionUID = -257470002216448607L;

	public RowsMatrixRenderer(
			HeatmapHeader decorator,
			boolean highlightSelected) {
		
		super(decorator, highlightSelected);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		final int w = this.getWidth();
		final int h = this.getHeight();

		//g2.setClip(0, 0, w, h);
		
		/*if (highlightSelected && isSelected)
			g2.setBackground(Color.ORANGE);
		else*/
			g2.setBackground(getBackground());

		g2.clearRect(0, 0, w, h);

		if (showGrid) {
			g2.setColor(gridColor);
			g2.drawRect(0, 0, w, h);
		}

		if (h < minimumRequiredTextSize)
			return;
		
		Rectangle2D r = g2.getFontMetrics().getStringBounds(getText(), g2);
		float textHeight = (float) r.getHeight();
		
		AffineTransform at = new AffineTransform();
		float scale = h / textHeight;
		at.setToScale(1.0, scale);
		g2.transform(at);
		
		g2.setColor(getForeground());
		g2.drawString(getText(), 4.0f, (scale + 8.0f + textHeight) / 2.0f);
	}
}