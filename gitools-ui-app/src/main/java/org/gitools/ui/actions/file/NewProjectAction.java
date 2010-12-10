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

import javax.swing.JOptionPane;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.workspace.NavigatorPanel;
import org.gitools.workspace.Workspace;
import org.gitools.workspace.WorkspaceManager;

public class NewProjectAction extends BaseAction {

	private static final long serialVersionUID = 2246209819863926918L;

	public NewProjectAction() {
		super("Project ...");
		setMnemonic(KeyEvent.VK_P);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		final String name = (String) JOptionPane.showInputDialog(AppFrame.instance(),
				"Project name", "New project...",
				JOptionPane.QUESTION_MESSAGE);

		Workspace workspace = WorkspaceManager.getDefault().getWorkspace();
		//try {
			//TODO workspace.createProject(name);
			
			NavigatorPanel navPanel = AppFrame.instance().getNavigatorPanel();
			navPanel.getWorkspaceNode().refresh();
			navPanel.refresh();
		/*} catch (PersistenceException ex) {
			ex.printStackTrace();
			AppFrame.instance().setStatusText("Error creating project.");
		}*/
	}

}
