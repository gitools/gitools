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

package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.platform.AppFrame;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;

public class SaveAsAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public SaveAsAction() {
		super("Save As ...");
		setDesc("Save to another location");
		setMnemonic(KeyEvent.VK_A);
	}

	@Override
	public boolean isEnabledByEditor(IEditor editor) {
		return false;
		//return editor != null && editor.isSaveAsAllowed();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		UnimplementedDialog.show(AppFrame.instance());
	}
}
