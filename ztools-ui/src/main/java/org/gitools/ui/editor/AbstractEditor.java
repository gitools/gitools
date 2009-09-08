package org.gitools.ui.editor;

import javax.swing.Icon;
import javax.swing.JPanel;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public abstract class AbstractEditor
		extends JPanel
		implements IEditor {

	private static final long serialVersionUID = -2379950551933668781L;

	protected Icon icon;
	
	private boolean dirty = false;
	private boolean saveAsAllowed = false;
	
	public Icon getIcon() {
		return icon;
	}
	
	public JPanel getPanel() {
		return this;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}
	
	protected void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return saveAsAllowed;
	}
	
	public void setSaveAsAllowed(boolean saveAsAllowed) {
		this.saveAsAllowed = saveAsAllowed;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
	}
	
	@Override
	public void doSaveAs(IProgressMonitor monitor) {	
	}
}
