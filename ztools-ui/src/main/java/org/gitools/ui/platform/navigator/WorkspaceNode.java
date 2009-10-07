package org.gitools.ui.platform.navigator;

import java.util.List;

import org.gitools.model.Project;
import org.gitools.model.Workspace;

public class WorkspaceNode extends NavigatorNode {

	public WorkspaceNode(Workspace workspace) {
		super(workspace);
	}
	
	protected Workspace getWorkspace() {
		return (Workspace) getObject();
	}
	
	@Override
	public int getChildCount() {
		return getWorkspace().getProjects().size();
	}
	
	@Override
	public NavigatorNode getChild(int index) {
		return new ProjectNode(getWorkspace().getProjects().get(index));
	}
	
	@Override
	public int getIndexOfChild(NavigatorNode child) {
		List<Project> projects = getWorkspace().getProjects();
		return projects.indexOf(child.getObject());
	}
	
	@Override
	public boolean isLeaf() {
		return getWorkspace().getProjects().size() == 0;
	}
}
