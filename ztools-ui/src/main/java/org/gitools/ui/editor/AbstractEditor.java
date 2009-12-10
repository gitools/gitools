package org.gitools.ui.editor;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.ui.view.AbstractView;

public abstract class AbstractEditor
		extends AbstractView
		implements IEditor {

	private static final long serialVersionUID = -2379950551933668781L;

	private boolean dirty = false;
	private boolean saveAsAllowed = false;
	
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
