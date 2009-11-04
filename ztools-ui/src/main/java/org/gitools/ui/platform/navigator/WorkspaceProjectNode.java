package org.gitools.ui.platform.navigator;

import java.io.File;

import org.gitools.persistence.FileSuffixes;
import org.gitools.workspace.WorkspaceProjectRef;

public class WorkspaceProjectNode extends AbstractNode {

	private static final long serialVersionUID = 8882086396274624151L;

	private WorkspaceProjectRef projectRef;
	
	public WorkspaceProjectNode(WorkspaceProjectRef project) {
		this.projectRef = project;
	}
	
	public WorkspaceProjectRef getProjectRef() {
		return projectRef;
	}
	
	@Override
	public void expand() {
		super.expand();
		
		File projectFile = new File(projectRef.getUrl());
		
		File[] files = projectFile.listFiles();
		for (File file : files) {
			if (!file.getName().matches(FileSuffixes.PROJECT)) {
				FileNode node = new FileNode(file);
				add(node);
			}
		}
	}
	
	@Override
	public String getLabel() {
		File projectFile = new File(projectRef.getUrl());
		
		return projectFile.getName();
	}
}
