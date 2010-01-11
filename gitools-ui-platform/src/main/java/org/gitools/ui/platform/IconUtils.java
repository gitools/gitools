package org.gitools.ui.platform;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconUtils {

	public static final String nullResourceImage = "/img/null.gif";
	
	public static Icon getIconResource(String name) {
		URL url = IconUtils.class.getResource(name);
		if (url == null)
			url = IconUtils.class.getResource(nullResourceImage);
		
		return new ImageIcon(url);
	}
	
	public static ImageIcon getImageIconResource(String name) {
		URL url = IconUtils.class.getResource(name);
		if (url == null)
			url = IconUtils.class.getResource(nullResourceImage);
		
		return new ImageIcon(url);
	}
}
