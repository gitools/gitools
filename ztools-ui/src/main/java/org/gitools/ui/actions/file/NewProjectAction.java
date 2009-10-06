package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import org.gitools.model.Workspace;
import org.gitools.persistence.PersistenceException;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;
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
		//new UnimplementedAction().actionPerformed(e);
		
		final String name = (String) JOptionPane.showInputDialog(AppFrame.instance(),
				"Project name", "New project...",
				JOptionPane.QUESTION_MESSAGE);

		Workspace workspace = WorkspaceManager.instance().getCurrent();
		try {
			workspace.createProject(name);
		} catch (PersistenceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
