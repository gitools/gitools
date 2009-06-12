package org.gitools.ui.panels.matrix;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.gitools.model.decorator.HeaderDecorator;

public class ColumnsMatrixRenderer extends HeaderMatrixRenderer {

	private static final long serialVersionUID = 8878769979396041532L;

	protected static final double radianAngle = (-90.0 / 180.0) * Math.PI;

	public ColumnsMatrixRenderer(
			HeaderDecorator decorator,
			boolean highlightSelected) {
	
		super(decorator, highlightSelected);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		final int w = this.getWidth();
		final int h = this.getHeight();

		//g2.setClip(0, 0, w, h);
		
		if (highlightSelected && isSelected)
			g2.setBackground(getBackground().darker());
		else
			g2.setBackground(getBackground());

		g2.clearRect(0, 0, w, h);

		if (showGrid) {
			g2.setColor(gridColor);
			g2.drawRect(0, 0, w, h);
		}

		//int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
	    //int fontSize = (int)Math.round(w * screenRes / 72.0);
	    
		//Font font = this.getFont().deriveFont(fontSize);
		//g2.setFont(font);
		
		Rectangle2D r = g2.getFontMetrics().getStringBounds(this.getText(), g2);
		float textHeight = (float) r.getHeight();
		
		AffineTransform at = new AffineTransform();
		at.setToTranslation(w, h);
		g2.transform(at);
		at.setToScale(1.0, w / textHeight);
		g2.transform(at);
		at.setToRotation(radianAngle);
		g2.transform(at);
		
		g2.setColor(getForeground());
		g2.drawString(this.getText(), 4.0f, -(w + 8 - textHeight) / 2);
	}
}