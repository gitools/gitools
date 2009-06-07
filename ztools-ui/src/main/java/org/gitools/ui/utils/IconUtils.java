package org.gitools.ui.utils;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.gitools.ui.IconNames;


public class IconUtils {

	public static Icon getIconResource(String name) {
		URL url = IconUtils.class.getResource(name);
		if (url == null)
			url = IconUtils.class.getResource(IconNames.nullResource);
		
		return new ImageIcon(url);
	}
	
	public static ImageIcon getImageIconResource(String name) {
		URL url = IconUtils.class.getResource(name);
		if (url == null)
			url = IconUtils.class.getResource(IconNames.nullResource);
		
		return new ImageIcon(url);
	}
}
