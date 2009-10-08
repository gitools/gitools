package org.gitools.ui.platform.navigator;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTree;

import org.gitools.model.Workspace;

public class NavigatorPanel extends JPanel {

	private static final long serialVersionUID = 5667653501673874725L;

	private Workspace workspace;
	
	private JTree tree;
	
	public NavigatorPanel() {
		this.workspace = new Workspace();
	}
	
	public NavigatorPanel(Workspace workspace) {
		this.workspace = workspace;
		
		createComponents();
	}

	private void createComponents() {
		tree = new JTree(new WorkspaceNode(workspace));
		
		setLayout(new BorderLayout());
		add(tree);
	}
}
