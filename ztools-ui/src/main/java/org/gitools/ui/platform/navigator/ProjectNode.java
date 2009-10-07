package org.gitools.ui.platform.navigator;

import org.gitools.model.Project;

public class ProjectNode extends NavigatorNode {

	public ProjectNode(Project project) {
		super(project);
	}

	protected Project getProject() {
		return (Project) getObject();
	}
	
	@Override
	public String getLabelText() {
		return "project: " + getProject().toString();
	}
}
