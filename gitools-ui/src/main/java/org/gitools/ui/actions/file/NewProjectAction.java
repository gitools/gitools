package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import org.gitools.persistence.PersistenceException;
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
