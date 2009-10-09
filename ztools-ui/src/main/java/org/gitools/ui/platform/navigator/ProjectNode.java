package org.gitools.ui.platform.navigator;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.gitools.model.Project;

public class ProjectNode extends AbstractNode {

	private static final long serialVersionUID = 8882086396274624151L;

	private Project project;
	
	public ProjectNode(Project project) {
		this.project = project;
	}
	
	public Project getProject() {
		return project;
	}
	
	@Override
	public void expand() {
		super.expand();
		
		try {
			FileObject projectFile = project.getResource().getParent();
			FileObject[] files = projectFile.getChildren();
			for (FileObject file : files) {
				if (!file.getName().getBaseName().matches("project.xml")) {
					FileObjectNode node = new FileObjectNode(file);
					add(node);
				}
			}
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getLabel() {
		FileName name = project.getResource().getName();
		return name.getParent().getBaseName();
	}
}
