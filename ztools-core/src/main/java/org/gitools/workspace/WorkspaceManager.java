package org.gitools.workspace;

import java.io.File;

import org.gitools.model.Project;
import org.gitools.model.Workspace;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.ResourceNameSuffixes;
import org.gitools.resources.FileResource;
import org.gitools.resources.IResource;

import edu.upf.bg.progressmonitor.NullProgressMonitor;

public class WorkspaceManager {

	private static WorkspaceManager instance;
	
	private Workspace currentWorkspace;
	
	private WorkspaceManager() {
	}
	
	public static final WorkspaceManager instance() {
		if (instance == null)
			instance = new WorkspaceManager();
		return instance;
	}
	
	public boolean exists(File path) {
		return path.exists() && path.isDirectory();
	}
	
	public Workspace create(File path) {
		if (!path.exists())
			path.mkdirs();
		
		Workspace workspace = new Workspace();
		workspace.setResource(new FileResource(path));
		return workspace;
	}
	
	public Workspace open(File path) throws PersistenceException {
		if (!path.isDirectory())
			throw new RuntimeException("Illegal workspace: " + path.getAbsolutePath());
		
		Workspace workspace = new Workspace();
		workspace.setResource(new FileResource(path));
		loadProjects(workspace);
		
		return workspace;
	}
	
	private void loadProjects(Workspace workspace) throws PersistenceException {
		IResource[] resources = workspace.getResource().list();
		for (IResource resource : resources) {
			if (resource.isContainer()) {
				IResource projectResource = resource.resolve(ResourceNameSuffixes.PROJECT);
				if (projectResource.exists()) {
					Project project = (Project) PersistenceManager.load(projectResource, null, new NullProgressMonitor());
					workspace.addProject(project);
				}
			}
		}
	}

	public void close(Workspace workspace) {
		throw new RuntimeException("unimplemented");
	}

	public void setCurrent(Workspace workspace) {
		this.currentWorkspace = workspace;
	}
	
	public Workspace getCurrent() {
		return currentWorkspace;
	}
}
