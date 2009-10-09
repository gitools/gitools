package org.gitools.persistence;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.vfs.FileObject;
import org.gitools.model.Artifact;
import org.gitools.model.Container;
import org.gitools.model.ModuleMap;
import org.gitools.model.Project;
import org.gitools.model.analysis.EnrichmentAnalysis;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.figure.TableFigure;
import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.DoubleMatrix;
import org.gitools.model.matrix.ObjectMatrix;
import org.gitools.persistence.text.AnnotationMatrixTextPersistence;
import org.gitools.persistence.text.DoubleMatrixTextPersistence;
import org.gitools.persistence.text.ModuleMapTextIndicesPersistence;
import org.gitools.persistence.text.ObjectMatrixTextPersistence;
import org.gitools.persistence.xml.ContainerXmlPersistence;
import org.gitools.persistence.xml.EnrichmentAnalysisXmlPersistence;
import org.gitools.persistence.xml.MatrixFigureXmlPersistence;
import org.gitools.persistence.xml.ProjectXmlPersistence;
import org.gitools.persistence.xml.TableFigureXmlPersistence;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class PersistenceManager {

	private static final Map<Class<?>, Class<? extends IEntityPersistence<?>>> persistenceMap =
		new HashMap<Class<?>, Class<? extends IEntityPersistence<?>>>();

	private static final Map<Integer, Object > cache = new HashMap<Integer, Object>();
	
	static {
		persistenceMap.put(Project.class, ProjectXmlPersistence.class);
		persistenceMap.put(EnrichmentAnalysis.class, EnrichmentAnalysisXmlPersistence.class);
		persistenceMap.put(Container.class, ContainerXmlPersistence.class);
		persistenceMap.put(MatrixFigure.class, MatrixFigureXmlPersistence.class);
		persistenceMap.put(TableFigure.class,  TableFigureXmlPersistence.class);
		persistenceMap.put(ObjectMatrix.class, ObjectMatrixTextPersistence.class);
		persistenceMap.put(DoubleMatrix.class, DoubleMatrixTextPersistence.class);
		persistenceMap.put(AnnotationMatrix.class, AnnotationMatrixTextPersistence.class);
		persistenceMap.put(ModuleMap.class, ModuleMapTextIndicesPersistence.class);
	}

	@SuppressWarnings("unchecked")
	public static <T> IEntityPersistence<T> createEntityPersistence(
			IFileObjectResolver fileObjectResolver,
			Class<T> entityClass) {

		Class<?> persistenceClass = persistenceMap.get(entityClass);
		
		try {
			Constructor<?> c = persistenceClass.getConstructor();
			IEntityPersistence<T> entityPersistence =
				(IEntityPersistence<T>) c.newInstance();
			entityPersistence.setFileObjectResolver(fileObjectResolver);
			
			return entityPersistence;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static Object load(
			FileObject resource,
			String entityType,
			IProgressMonitor monitor)
				throws PersistenceException {
		
		return load(new FileObjectResolver(), resource, entityType, monitor);
	}
	
	@SuppressWarnings("unchecked")
	public static Object load(
			IFileObjectResolver fileObjectResolver,
			FileObject resource,
			String entityType,
			IProgressMonitor monitor)
				throws PersistenceException {
		
		int key = resource.hashCode();
		
		if (cache.containsKey(key))
			return cache.get(key);

		Class<?> entityClass;
		
		if (entityType == null)
			entityClass = ResourceNameSuffixes.getEntityClass(resource);
		else
			entityClass = ResourceNameSuffixes.getEntityClass(entityType);
		
		IEntityPersistence<Object> entityPersistence = (IEntityPersistence<Object>) 
			createEntityPersistence(fileObjectResolver, entityClass);

		Object entity = entityPersistence.read(resource, monitor);

		if (entity instanceof Artifact)
			((Artifact) entity).setResource(resource);		
	
		//if (!cache.containsKey(key))
		cache.put(key, entity);

		return entity;
	}

	public static void store(
			FileObject resource,
			Object entity,
			IProgressMonitor monitor) 
			throws PersistenceException {
		
		store(new FileObjectResolver(), resource, entity, monitor);
	}
	
	@SuppressWarnings("unchecked")
	public static void store(
			IFileObjectResolver fileObjectResolver,
			FileObject resource,
			Object entity,
			IProgressMonitor monitor) 
			throws PersistenceException {

		//monitor.begin("Storing " + resource.getName() + " ...", 1);

		IEntityPersistence<Object> entityPersistence = (IEntityPersistence<Object>) 
			createEntityPersistence(fileObjectResolver, entity.getClass());

		entityPersistence.write(resource, entity, monitor);
	}
}
