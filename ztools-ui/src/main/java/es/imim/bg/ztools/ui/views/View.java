package es.imim.bg.ztools.ui.views;

import javax.swing.Icon;
import javax.swing.JPanel;

public interface View {

	String getName();
	void setName(String name);
	
	Icon getIcon();
	
	JPanel getPanel();
}
