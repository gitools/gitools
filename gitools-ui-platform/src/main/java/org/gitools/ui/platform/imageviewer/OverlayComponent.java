package org.gitools.ui.platform.imageviewer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The <code>JComponent</code> implementing an overlay.
 * @author Kazó Csaba
 */
class OverlayComponent extends JComponent {
	Overlay overlay;
	ImageComponent theImage;
	public OverlayComponent(Overlay overlay, ImageComponent image) {
		this.overlay=overlay;
		this.theImage=image;
	}

	@Override
	protected void paintComponent(Graphics g) {
		BufferedImage image = theImage.getImage();
		if (image!=null) {
			Graphics2D gg=(Graphics2D)g.create();
			overlay.paint(gg, image, theImage.getImageTransform());
			gg.dispose();
		}
	}
	
	@Override
	public Dimension getMaximumSize() {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
}
