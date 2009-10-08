package org.gitools.ui.platform.navigator;

import org.gitools.model.Workspace;

public class WorkspaceNode extends AbstractNode {

	private static final long serialVersionUID = 5411843318201716836L;

	private Workspace workspace;
	
	public WorkspaceNode(Workspace workspace) {
		super();
		
		this.workspace = workspace;
	}
	
	public Workspace getWorkspace() {
		return workspace;
	}
	
	@Override
	public String getLabel() {
		return "workspace";
	}
}
