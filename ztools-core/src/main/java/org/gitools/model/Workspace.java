package org.gitools.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.gitools.persistence.FilePathResolver;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.FileSuffixes;

import edu.upf.bg.progressmonitor.NullProgressMonitor;

//TODO use the one implemented with netbeans
public class Workspace extends Artifact {

	private static final long serialVersionUID = 1304302155274885394L;

	private File path;
	
	//TODO ProjectRef
	private List<Project> projects = new ArrayList<Project>();
	
	public Workspace(File path) {
		this.path = path;
	}
	
	public File getPath() {
		return path;
	}
	
	public void setPath(File path) {
		this.path = path;
		//TODO reload();
	}
	
	public List<Project> getProjects() {
		return projects;
	}

	public void addProject(Project project) {
		projects.add(project);
	}

	public void createProject(String name) throws PersistenceException {
		createProject(name, new Project());
	}

	public void createProject(String name, Project project) throws PersistenceException {
		File resource = new File(path, name);
		resource.mkdirs();
		resource = new File(resource, FileSuffixes.PROJECT);
		
		//TODO project.setResource(resource);
		PersistenceManager.getDefault().store(
				new FilePathResolver(), resource, project, new NullProgressMonitor());
		
		projects.add(project);
	}
}
