package org.gitools.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.ResourceNameSuffixes;

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
		FileObject resource;
		try {
			resource = getResource().resolveFile(name);
			resource.createFolder();
			resource = resource.resolveFile(ResourceNameSuffixes.PROJECT);
		} catch (FileSystemException e) {
			throw new PersistenceException("Error creating project " + name, e);
		}
		
		project.setResource(resource);
		PersistenceManager.store(resource, project, new NullProgressMonitor());
		projects.add(project);
	}
}
