package org.gitools.ui.platform.navigator;

import javax.swing.Icon;

public interface INavigatorNode {

	void expand();
	
	void refresh();
	
	void collapse();
	
	Icon getIcon();
	
	String getLabel();
}
