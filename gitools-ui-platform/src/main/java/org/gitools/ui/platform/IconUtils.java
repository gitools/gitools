package org.gitools.ui.platform;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconUtils {

	public static final String nullResourceImage = "/img/null.gif";
	
	public static Icon getIconResource(String name) {
		return getImageIconResource(name);
	}
	
	public static ImageIcon getImageIconResource(String name) {
		URL url = IconUtils.class.getResource(name);
		if (url == null)
			url = IconUtils.class.getResource(nullResourceImage);
		
		return new ImageIcon(url);
	}

	public static ImageIcon getImageIconResourceScaledByHeight(String name, int height) {
		ImageIcon icon = getImageIconResource(name);
		int w = icon.getIconWidth();
		int h = icon.getIconHeight();
		double ratio = (double) height / (double) h;
		int width = (int) Math.floor(w * ratio);
		return new ImageIcon(iconToImage(icon, width, height));
	}

	public static Image iconToImage(Icon icon) {
		return iconToImage(icon, icon.getIconWidth(), icon.getIconHeight());
	}

	public static Image iconToImage(Icon icon, int width, int height) {
		int w = icon.getIconWidth();
		int h = icon.getIconHeight();
		boolean sameSize = w == width && h == height;

		if (icon instanceof ImageIcon && sameSize)
			return ((ImageIcon) icon).getImage();
		else {	
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gd = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gd.getDefaultConfiguration();
			BufferedImage image = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
			Graphics2D g = image.createGraphics();
			icon.paintIcon(null, g, 0, 0);
			g.dispose();
			if (sameSize)
				return image;
			else
				return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		}
	}

	/*public static ImageIcon iconToImageIcon(Icon icon) {
		if (icon instanceof ImageIcon)
			return (ImageIcon) icon;
		else
			return new ImageIcon(iconToImage(icon));
	}*/
}
