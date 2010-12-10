/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

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
		//System.out.println("getImageIconResourceScaledByHeight(" + name + ", " + height + ")");

		ImageIcon icon = getImageIconResource(name);
		int w = icon.getIconWidth();
		int h = icon.getIconHeight();
		double ratio = (double) height / (double) h;
		int width = (int) Math.floor(w * ratio);
		ImageIcon image = new ImageIcon(iconToImage(icon, width, height));

		//System.out.println("Done.");
		
		return image;
	}

	public static Image iconToImage(Icon icon) {
		return iconToImage(icon, icon.getIconWidth(), icon.getIconHeight());
	}

	public static Image iconToImage(Icon icon, int width, int height) {
		//System.out.println("\ticonToImage(" + width + ", " + height + ")");
		
		int w = icon.getIconWidth();
		int h = icon.getIconHeight();
		boolean sameSize = w == width && h == height;

		if (icon instanceof ImageIcon && sameSize)
			return ((ImageIcon) icon).getImage();
		else {	
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gd = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gd.getDefaultConfiguration();
			BufferedImage image = gc.createCompatibleImage(w, h, Transparency.BITMASK /*TRANSLUCENT*/);
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
