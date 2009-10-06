package org.gitools.ui.platform.navigator;

import org.gitools.model.Project;
import org.gitools.resources.IResource;

public class ProjectNode extends NavigatorNode {

	public ProjectNode(Project project) {
		super(project);
	}

	protected Project getProject() {
		return (Project) getObject();
	}
	
	@Override
	public String getLabelText() {
		IResource res = getProject().getResource();
		return res.relativize(res.getParent()).toString();
	}
}
