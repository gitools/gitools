/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.platform.editor;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.ui.platform.view.IView;

public interface IEditor extends IView {

	Object getModel();
	
	boolean isDirty();

	/** Called when save action called for the editor */
	void doSave(IProgressMonitor monitor);
	
	boolean isSaveAsAllowed();

    boolean isSaveAllowed();
	
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
