package org.gitools.model;

import java.util.ArrayList;
import java.util.List;

import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.ResourceNameSuffixes;
import org.gitools.resources.IResource;

import edu.upf.bg.progressmonitor.NullProgressMonitor;


public class Workspace extends Artifact {

	private static final long serialVersionUID = 1304302155274885394L;

	private List<Project> projects = new ArrayList<Project>();
	
	public Workspace() {
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
		IResource resource = getResource().resolve(name);
		resource.mkdir();
		resource = resource.resolve(ResourceNameSuffixes.PROJECT);
		project.setResource(resource);
		PersistenceManager.store(resource, project, new NullProgressMonitor());
		projects.add(project);
	}
}
