package org.gitools.ui.editor;

import javax.swing.Icon;
import javax.swing.JPanel;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public interface IEditor {

	String getName();
	void setName(String name);
	
	Icon getIcon();
	
	JPanel getPanel();
	
	Object getModel();
	
	boolean isDirty();
	
	void doSave(IProgressMonitor monitor);
	
	boolean isSaveAsAllowed();
	
	void doSaveAs(IProgressMonitor monitor);
	
	void refresh();
	
	//void refreshActions();	
}
