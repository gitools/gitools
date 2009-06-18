package org.gitools.persistence;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.gitools.model.Artifact;
import org.gitools.model.Project;
import org.gitools.model.ResourceContainer;
import org.gitools.resources.FileResource;
import org.gitools.resources.IResource;

import edu.upf.bg.progressmonitor.DefaultProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;



public class PersistenceManager {

	private static final Map<String, Class<?>> extensionsPersistenceMap = 
		new HashMap<String, Class<?>>();

	private static final Map<Class<? extends Artifact>, Class<?>> classesPersistenceMap = 
		new HashMap<Class<? extends Artifact>, Class<?>>();

	static {

		//Fill the extensions persistence map
		extensionsPersistenceMap.put(FileExtensions.PROJECT, ProjectPersistence.class);
		extensionsPersistenceMap.put(FileExtensions.RESOURCE_CONTAINER,
				ResourceContainerPersistence.class);
		
		//Fill the class persistence map
		classesPersistenceMap.put(Project.class, ProjectPersistence.class);
		classesPersistenceMap.put(ResourceContainer.class,
				ResourceContainerPersistence.class);

	}

	@SuppressWarnings("unchecked")
	public static IEntityPersistence createEntityPersistence(IResource resource) {

		if (resource.getClass().equals(FileResource.class)) {

			String extension = getExtension(
					((FileResource) resource).getFile(),
					FileExtensions.extensionSeparator);

			Class<?> fileResourceClass = 
				extensionsPersistenceMap.get(extension);

			try {
				return 
				(IEntityPersistence) fileResourceClass.newInstance();
			} 
			catch (Exception e) {
				return null;
			}
		}

		return null;

	}

	@SuppressWarnings("unchecked")			
	public static <T> IEntityPersistence<T> createEntityPersistence(
			Class<T> entityClass) {

		Class<?> fileResourceClass = 
			classesPersistenceMap.get(entityClass);

		try {
			return 
			(IEntityPersistence<T>) fileResourceClass.newInstance();
		} 
		catch (Exception e) {
			return null;
		}
	}

	private static String getExtension(File file, String extensionSeparator) {
		String fileName = file.getName();
		return fileName.substring(fileName.lastIndexOf(extensionSeparator) + 1);
	}


	@SuppressWarnings("unchecked")
	public static Artifact load(IResource resource) throws PersistenceException {
		
		IProgressMonitor monitor= new DefaultProgressMonitor();
		monitor.begin("Start loading ... "+ resource.toURI(), 1);
	
		IEntityPersistence<Artifact> entityPersistence = createEntityPersistence(resource);
		return entityPersistence.read(resource, monitor);
	}


	@SuppressWarnings("unchecked")
	public static boolean store(IResource resource, Artifact entity) throws PersistenceException{
	
		IProgressMonitor monitor= new DefaultProgressMonitor();
		monitor.begin("Storing ... "+ resource.toURI(), 1);
	
		IEntityPersistence<Artifact> entityPersistence = (IEntityPersistence<Artifact>) createEntityPersistence(entity.getClass());
		entityPersistence.write(resource, entity, monitor);
		return true;

	}
}
