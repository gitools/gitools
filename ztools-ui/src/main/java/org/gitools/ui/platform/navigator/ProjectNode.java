package org.gitools.ui.platform.navigator;

import java.io.File;

import org.gitools.model.Project;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.PersistenceManager;

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
		
		File projectFile = PersistenceManager.getDefault()
								.getEntityFile(project).getParentFile();
		
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
		File projectFile = PersistenceManager.getDefault()
								.getEntityFile(project).getParentFile();
		
		return projectFile.getParentFile().getName();
	}
}
