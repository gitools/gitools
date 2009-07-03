package org.gitools.persistence;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.gitools.model.Artifact;
import org.gitools.model.Project;
import org.gitools.model.ResourceContainer;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.resources.FileResource;
import org.gitools.resources.IResource;

import edu.upf.bg.progressmonitor.DefaultProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class PersistenceManager {

	private static final Map<String, Class<?>> extensionsPersistenceMap = new HashMap<String, Class<?>>();

	private static final Map<Class<? extends Artifact>, Class<?>> classesPersistenceMap = new HashMap<Class<? extends Artifact>, Class<?>>();

	static {

		// map to used to read an artifact
		extensionsPersistenceMap.put(FileExtensions.PROJECT,
				JAXBPersistence.class);
		extensionsPersistenceMap.put(FileExtensions.RESOURCE_CONTAINER,
				JAXBPersistence.class);
		extensionsPersistenceMap.put(FileExtensions.MATRIX_FIGURE,
				JAXBPersistence.class);

		
		// map to store an object
		classesPersistenceMap.put(Project.class, JAXBPersistence.class);
		classesPersistenceMap.put(ResourceContainer.class,JAXBPersistence.class);
		classesPersistenceMap.put(MatrixFigure.class, JAXBPersistence.class);
	}

	/**
	 * @param baseResource
	 * @param resource
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static IEntityPersistence createEntityPersistence(
			IResource baseResource, IResource resource) {

		if (resource.getClass().equals(FileResource.class)) {

			String extension = getExtension(
					((FileResource) resource).getFile(),
					FileExtensions.extensionSeparator);

			Class<?> fileResourceClass = extensionsPersistenceMap
					.get(extension);

			try {

				if (fileResourceClass.equals(JAXBPersistence.class)) {
					Constructor<?> c = fileResourceClass.getConstructor(
							IResource.class, String.class);
					return (IEntityPersistence) c.newInstance(baseResource,
							extension);
				}

				return (IEntityPersistence) fileResourceClass.newInstance();
			} catch (Exception e) {
				return null;
			}
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public static <T> IEntityPersistence<T> createEntityPersistence(
			IResource baseResource, Class<T> entityClass) {

		Class<?> fileResourceClass = classesPersistenceMap.get(entityClass);

		try {
			if (fileResourceClass.equals(JAXBPersistence.class)) {
				Constructor<?> c = fileResourceClass.getConstructor(
						IResource.class, Class.class);
				return (IEntityPersistence) c.newInstance(baseResource,
						entityClass);
			}
			return (IEntityPersistence<T>) fileResourceClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Dado un resource absoluto, carga su artifact asociado en memoria. El
	 * método load necesita el baseResource para crear el IResource en el caso de los
	 * ResourceContainers ya que éstos guardan la uri relativa de sus resources referenciados.
	 * 
	 * @param baseResource
	 *            representa el resource a nivel de aplicación
	 * @param resource
	 *            resource absoluto a leer.
	 * @return
	 * @throws PersistenceException
	 */
	@SuppressWarnings("unchecked")
	public static Artifact load(IResource baseResource, IResource resource)
			throws PersistenceException {

		IProgressMonitor monitor = new DefaultProgressMonitor();
		monitor.begin("Start loading ... " + resource.toURI(), 1);

		IEntityPersistence<Artifact> entityPersistence = createEntityPersistence(
				baseResource, resource);
		return entityPersistence.read(resource, monitor);
	}

	/**
	 * Dado un resource absoluto escribe en el stream de salida el artifact
	 * asociado. Necesitamos el baseResource para relativizar los resources
	 * contenidos dentro de un ResourceContainer
	 * 
	 * @param baseResource
	 *            resource a nivel de aplicación
	 * @param resource
	 *            resource absoluto en donde escribir
	 * @param entity
	 *            entity a persistir
	 * @return
	 * @throws PersistenceException
	 */
	@SuppressWarnings("unchecked")
	public static boolean store(IResource baseResource, IResource resource,
			Artifact entity) throws PersistenceException {

		IProgressMonitor monitor = new DefaultProgressMonitor();
		monitor.begin("Storing ... " + resource.toURI(), 1);

		IEntityPersistence<Artifact> entityPersistence = (IEntityPersistence<Artifact>) createEntityPersistence(
				baseResource, entity.getClass());
		entityPersistence.write(resource, entity, monitor);
		return true;

	}

	private static String getExtension(File file, String extensionSeparator) {
		String fileName = file.getName();
		return fileName.substring(fileName.lastIndexOf(extensionSeparator) + 1);
	}

}
