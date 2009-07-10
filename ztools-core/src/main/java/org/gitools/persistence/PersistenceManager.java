package org.gitools.persistence;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.gitools.model.Project;
import org.gitools.model.ResourceContainer;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.ObjectMatrix;
import org.gitools.resources.FileResource;
import org.gitools.resources.IResource;

import edu.upf.bg.progressmonitor.DefaultProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class PersistenceManager {

	private static final Map<Class<? extends Object>, Class<?>> 
		persistenceMap = new HashMap<Class<? extends Object>, Class<?>>();

	static {

		persistenceMap.put(
				Project.class, XmlGenericPersistence.class);
		persistenceMap.put(
				ResourceContainer.class,XmlResourcePersistence.class);
		persistenceMap.put(
				MatrixFigure.class,XmlGenericPersistence.class);
		persistenceMap.put(
				ObjectMatrix.class,TextObjectMatrixPersistence.class);
		persistenceMap.put(
				AnnotationMatrix.class,TextAnnotationMatrixPersistence.class);

	}

	/**
	 * @param baseResource
	 * @param resource
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static IEntityPersistence createEntityPersistence(IResource resource) {

		IEntityPersistence entityPersistence = null;

		if (resource.getClass().equals(FileResource.class)) {

			String extension = getExtension(
					((FileResource) resource).getFile(),
					FileExtensions.extensionSeparator);

			entityPersistence = createEntityPersistence(resource,extension);

		}
		return entityPersistence;
	}

	/**
	 * @param baseResource
	 * @param resource
	 * @param extension
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static IEntityPersistence createEntityPersistence(IResource resource, String extension) {

		if (resource.getClass().equals(FileResource.class)) {

			Class<?> fileResourceClass = persistenceMap
					.get(FileExtensions.getEntityClass(extension));
			
			try {

				if (fileResourceClass.equals(XmlResourcePersistence.class)) {
					Constructor<?> c = fileResourceClass.getConstructor(
							IResource.class, String.class);
					return (IEntityPersistence) c.newInstance(resource,extension);
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

		Class<?> fileResourceClass = persistenceMap.get(entityClass);

		try {
			if (fileResourceClass.equals(XmlGenericPersistence.class)) {
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
	 * método load necesita el baseResource para crear el IResource en el caso
	 * de los ResourceContainers ya que éstos guardan la uri relativa de sus
	 * resources referenciados.
	 * 
	 * @param baseResource
	 *            representa el resource a nivel de aplicación
	 * @param resource
	 *            resource absoluto a leer.
	 * @return
	 * @throws PersistenceException
	 */
	@SuppressWarnings("unchecked")
	public static Object load(IResource resource)
			throws PersistenceException {

		IProgressMonitor monitor = new DefaultProgressMonitor();
		monitor.begin("Start loading ... " + resource.toURI(), 1);

		IEntityPersistence<Object> entityPersistence = createEntityPersistence(resource);
		return entityPersistence.read(resource, monitor);
	}

	
	
	@SuppressWarnings("unchecked")
	public static Object load(IResource resource, String fileExtension) throws PersistenceException {

		IProgressMonitor monitor = new DefaultProgressMonitor();
		monitor.begin("Start loading ... " + resource.toURI(), 1);

		IEntityPersistence<Object> entityPersistence = createEntityPersistence(resource, fileExtension);
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
			Object entity) throws PersistenceException {

		IProgressMonitor monitor = new DefaultProgressMonitor();
		monitor.begin("Storing ... " + resource.toURI(), 1);

		IEntityPersistence<Object> entityPersistence = 
			(IEntityPersistence<Object>) createEntityPersistence(baseResource, entity.getClass());
		
		entityPersistence.write(resource, entity, monitor);
		
		return true;
	}

	private static String getExtension(File file, String extensionSeparator) {

		String fileName = file.getName();
		String extension = fileName.substring(fileName.lastIndexOf(extensionSeparator) + 1);
		fileName.replace(extension," ");

		System.out.println("la extension es la siguiente " + fileName + " "+ extension);
		return extension;

	}

}
