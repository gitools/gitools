package org.gitools.ui.platform.editor;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.ui.platform.view.IView;

public interface IEditor extends IView {

	Object getModel();
	
	boolean isDirty();
	
	void doSave(IProgressMonitor monitor);
	
	boolean isSaveAsAllowed();
	
	void doSaveAs(IProgressMonitor monitor);

	void doVisible();
}
