package org.gitools.ui.platform.editor;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.ui.platform.view.IView;

public interface IEditor extends IView {

	Object getModel();
	
	boolean isDirty();

	/** Called when save action called for the editor */
	void doSave(IProgressMonitor monitor);
	
	boolean isSaveAsAllowed();
	
	void doSaveAs(IProgressMonitor monitor);

	/** Called when editor gets visible */
	void doVisible();

	/** Called before closing the editor.
	 * Return true to confirm close or false to cancel close.
	 *
	 * @return true -> close, false -> cancel close
	 */
	boolean doClose();
}
