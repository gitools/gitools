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

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class SaveAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public SaveAction() {
		super("Save");

		setDesc("Save changes");
		setLargeIconFromResource(IconNames.save24);
		setSmallIconFromResource(IconNames.save16);
		setMnemonic(KeyEvent.VK_S);
	}

	@Override
	public boolean isEnabledByEditor(IEditor editor) {

        if (editor == null)
            return false;

        EditorsPanel editorPanel;
        editorPanel = AppFrame.instance().getEditorsPanel();

        return editorPanel.getSelectedEditor().isSaveAllowed();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		final IEditor currentEditor = editorPanel.getSelectedEditor();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				currentEditor.doSave(monitor);
			}
		});
	}
}
