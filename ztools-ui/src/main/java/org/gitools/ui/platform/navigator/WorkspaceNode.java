package org.gitools.ui.platform.navigator;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.gitools.model.Project;
import org.gitools.model.Workspace;

public class WorkspaceNode extends AbstractNode {

	private static final long serialVersionUID = 5411843318201716836L;

	private Workspace workspace;
	
	public WorkspaceNode(Workspace workspace) {
		super();
		
		this.workspace = workspace;
		
		for (Project project : workspace.getProjects())
			add(new ProjectNode(project));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void refresh() {
		final Map<Project, ProjectNode> nodeMap = new HashMap<Project, ProjectNode>();
		Enumeration<ProjectNode> e = children();
		while (e.hasMoreElements()) {
			ProjectNode node = e.nextElement();
			nodeMap.put(node.getProject(), node);
		}
		
		// Check that current project nodes exist in file system
		for (Entry<Project, ProjectNode> entry : nodeMap.entrySet()) {
			final Project project = entry.getKey();
			final ProjectNode node = entry.getValue();
			try {
				final FileObject fileObject = project.getResource();
				fileObject.refresh();
				if (!fileObject.exists())
					remove(node);
			} catch (FileSystemException ex) {
				ex.printStackTrace();
			}
		}
		
		// Check for new project nodes
		List<Project> projects = workspace.getProjects();
		for (Project project : projects) {
			if (!nodeMap.containsKey(project)) {
				ProjectNode node = new ProjectNode(project);
				nodeMap.put(project, node);
				add(node);
			}
		}
	}
	
	public Workspace getWorkspace() {
		return workspace;
	}
	
	@Override
	public String getLabel() {
		return "workspace";
	}
}
