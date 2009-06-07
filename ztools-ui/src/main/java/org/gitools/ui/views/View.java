package org.gitools.ui.views;

import javax.swing.Icon;
import javax.swing.JPanel;

public interface View {

	String getName();
	void setName(String name);
	
	Icon getIcon();
	
	JPanel getPanel();
	
	Object getModel();
	
	void refresh();
	
	void refreshActions();	
}
