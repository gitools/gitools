package org.gitools.persistence;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.gitools.model.Artifact;
import org.gitools.model.Project;
import org.gitools.model.ResourceContainer;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.ObjectMatrix;
import org.gitools.resources.IResource;

import edu.upf.bg.progressmonitor.DefaultProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class PersistenceManager {

	private static final Map<Class<? extends Object>, Class<?>> persistenceMap = new HashMap<Class<? extends Object>, Class<?>>();

	static {

		persistenceMap.put(Project.class, 
				XmlGenericPersistence.class);
		persistenceMap.put(ResourceContainer.class,
				XmlResourcePersistence.class);
		persistenceMap.put(MatrixFigure.class, 
				XmlResourcePersistence.class);
		persistenceMap.put(ObjectMatrix.class,
				TextObjectMatrixPersistence.class);
		persistenceMap.put(AnnotationMatrix.class,
				TextAnnotationMatrixPersistence.class);

	}

	/**
	 * @param base
	 * @param resource
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static IEntityPersistence createEntityPersistence(IResource resource) {

		return createEntityPersistence(resource, getExtension(resource));
	}

	/**
	 * @param base
	 * @param resource
	 * @param extension
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static IEntityPersistence createEntityPersistence(
			IResource resource, String extension) {
			
			Class<?> entityClass = 
		    	Extensions.getEntityClass(extension);
			
		    Class<?> resourceClass = 
				persistenceMap.get(entityClass);

			try {
				
				Constructor<?>c ;
				
				if (resourceClass.equals(XmlResourcePersistence.class)) {
					c = resourceClass.getConstructor(IResource.class,Class.class);
					return (IEntityPersistence) c.newInstance(resource,entityClass);
				}

				if (resourceClass.equals(XmlGenericPersistence.class)) {
					c = resourceClass.getConstructor(Class.class);
					return (IEntityPersistence) c.newInstance(entityClass);
				}
				
			
				return (IEntityPersistence) resourceClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

	}

	@SuppressWarnings("unchecked")
	public static <T> IEntityPersistence<T> createEntityPersistence(
			IResource resource, Class<T> entityClass) {

		Class<?> resourceClass = persistenceMap.get(entityClass);
		
		try {
				
			Constructor<?> c;
			
			if (resourceClass.equals(XmlResourcePersistence.class)) {
				c = resourceClass.getConstructor(IResource.class, Class.class);
				return (IEntityPersistence) c.newInstance(resource,entityClass);
			}
			
			if (resourceClass.equals(XmlGenericPersistence.class)) {
				c = resourceClass.getConstructor(Class.class);
					return (IEntityPersistence) c.newInstance(entityClass);
				}
			
			return (IEntityPersistence<T>) resourceClass.newInstance();
		
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
	 * @param base
	 *            representa el resource a nivel de aplicación
	 * @param resource
	 *            resource absoluto a leer.
	 * @return
	 * @throws PersistenceException
	 */
	
	@SuppressWarnings("unchecked")
	public static Object load(IResource resource) throws PersistenceException {

		IProgressMonitor monitor = new DefaultProgressMonitor();
		monitor.begin("Start loading ... " + resource.toURI(), 1);

		IEntityPersistence<Object> entityPersistence = createEntityPersistence(resource);
		
		Object entity = entityPersistence.read(resource, monitor);
		
		if (entity instanceof Artifact)
			((Artifact) entity).setResource(resource);		
	
		return entity;
	}

	@SuppressWarnings("unchecked")
	public static Object load(IResource resource, String fileExtension)
			throws PersistenceException {

		IProgressMonitor monitor = new DefaultProgressMonitor();
		monitor.begin("Start loading ... " + resource.toURI(), 1);

		IEntityPersistence<Object> entityPersistence = createEntityPersistence(
				resource, fileExtension);
		

		Object entity = entityPersistence.read(resource, monitor);
		
		if (entity instanceof Artifact)
			((Artifact) entity).setResource(resource);		
	
		return entity;
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
	public static boolean store(IResource resource, Object entity) throws PersistenceException {

		IProgressMonitor monitor = new DefaultProgressMonitor();
		monitor.begin("Storing ... " + resource.toURI(), 1);

		IEntityPersistence<Object> entityPersistence = (IEntityPersistence<Object>) createEntityPersistence(
				resource, entity.getClass());

		entityPersistence.write(resource, entity, monitor);

		return true;
	}

	private static String getExtension(IResource resource) {

		String extension;
		String uri = resource.toURI().toString();

		if (uri.contains("contents.xml"))
			extension = Extensions.Contents;
		
		else if( uri.contains("results"))
			extension = Extensions.OBJECT_MATRIX;
		
		else {
			extension = uri.substring(uri.lastIndexOf('.') + 1);
			extension.replace(extension, " ");
		}

		System.out.println("la extension es la siguiente " + uri + " "
				+ extension);
		return extension;

	}

}
