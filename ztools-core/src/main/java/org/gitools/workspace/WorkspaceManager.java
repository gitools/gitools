package org.gitools.workspace;

import java.io.File;

import org.gitools.model.Project;
import org.gitools.model.Workspace;
import org.gitools.persistence.FilePathResolver;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.FileSuffixes;

import edu.upf.bg.progressmonitor.NullProgressMonitor;

public class WorkspaceManager {

	private static WorkspaceManager instance;
	
	private Workspace currentWorkspace;
	
	private WorkspaceManager() {
	}
	
	public static final WorkspaceManager getDefault() {
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
		
		Workspace workspace = new Workspace(path);
		return workspace;
	}
	
	public Workspace open(File path) throws PersistenceException {
		if (!path.isDirectory())
			throw new RuntimeException("Illegal workspace: " + path.getAbsolutePath());
		
		Workspace workspace = new Workspace(path);
		loadProjects(workspace);
		
		return workspace;
	}
	
	private void loadProjects(Workspace workspace) throws PersistenceException {
		File[] resources = workspace.getPath().listFiles();
		for (File resource : resources) {
			if (resource.isDirectory()) {
				File projectResource = new File(resource, FileSuffixes.PROJECT);
				if (projectResource.exists()) {
					Project project = (Project) PersistenceManager.getDefault().load(
							new FilePathResolver(), projectResource, null, new NullProgressMonitor());
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
