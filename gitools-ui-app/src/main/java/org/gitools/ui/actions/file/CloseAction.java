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

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;


public class CloseAction extends BaseAction {

	private static final long serialVersionUID = 2399811452235609343L;

	public CloseAction() {
		super("Close");
		
		setDesc("Close current tab");
		setSmallIconFromResource(IconNames.close16);
		setLargeIconFromResource(IconNames.close24);
		setMnemonic(KeyEvent.VK_O);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		EditorsPanel editorsPanel = AppFrame.instance().getEditorsPanel();
		AbstractEditor currentEditor = editorsPanel.getSelectedEditor();
		if (currentEditor != null)
			editorsPanel.removeEditor(currentEditor);
		
		AppFrame.instance().refresh();
		AppFrame.instance().setStatusText("View closed.");
	}

	@Override
	public boolean isEnabledByEditor(IEditor editor) {
		return editor != null;
	}
}
