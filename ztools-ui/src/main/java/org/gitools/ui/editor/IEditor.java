package org.gitools.ui.editor;

import javax.swing.Icon;
import javax.swing.JPanel;

public interface IEditor {

	String getName();
	void setName(String name);
	
	Icon getIcon();
	
	JPanel getPanel();
	
	Object getModel();
	
	void refresh();
	
	void refreshActions();	
}
