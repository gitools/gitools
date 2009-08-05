package org.gitools.persistence;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.gitools.model.Artifact;
import org.gitools.model.ModuleMap;
import org.gitools.model.Project;
import org.gitools.model.ResourceContainer;
import org.gitools.model.analysis.EnrichmentAnalysis;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.figure.TableFigure;
import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.DoubleMatrix;
import org.gitools.model.matrix.ObjectMatrix;
import org.gitools.resources.IResource;
import org.gitools.resources.factory.ResourceFactory;

import edu.upf.bg.progressmonitor.DefaultProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class PersistenceManager {

	private static final Map<Class<? extends Object>, Class<?>> 
		persistenceMap = new HashMap<Class<? extends Object>, Class<?>>();

	private static Set<Class<?>> cache = new HashSet<Class<?>>();
	
	static {

		persistenceMap.put(Project.class, XmlGenericPersistence.class);
		persistenceMap.put(EnrichmentAnalysis.class, XmlEnrichmentAnalysisPersistence.class);
		persistenceMap.put(ResourceContainer.class, XmlResourcePersistence.class);
		persistenceMap.put(MatrixFigure.class, XmlMatrixFigurePersistence.class);
		persistenceMap.put(TableFigure.class,  XmlTableFigurePersistence.class);
		persistenceMap.put(ObjectMatrix.class, TextObjectMatrixPersistence.class);
		persistenceMap.put(DoubleMatrix.class, TextDoubleMatrixPersistence.class);
		persistenceMap.put(AnnotationMatrix.class, TextAnnotationMatrixPersistence.class);
		persistenceMap.put(ModuleMap.class, ModuleMapPersistence.class);

	}



	@SuppressWarnings("unchecked")
	public static <T> IEntityPersistence<T> 
		createEntityPersistence(ResourceFactory resourceFactory, Class<T> entityClass) {

		Class<?> resourceClass = persistenceMap.get(entityClass);
		
		try {
			Constructor<?> c;
			
			if (resourceClass.equals(XmlGenericPersistence.class)) {
				c = resourceClass.getConstructor(Class.class);
					return (IEntityPersistence) c.newInstance(entityClass);
				}
			
			if ((resourceClass.equals(XmlMatrixFigurePersistence.class)) ||
				(resourceClass.equals(XmlTableFigurePersistence.class))  ||
				(resourceClass.equals(XmlEnrichmentAnalysisPersistence.class))  ||
				(resourceClass.equals(XmlResourcePersistence.class))) {
				
				c = resourceClass.getConstructor(ResourceFactory.class, Class.class);
				return (IEntityPersistence) c.newInstance(resourceFactory, entityClass);
			}
			
			
			
			return (IEntityPersistence<T>) resourceClass.newInstance();
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@SuppressWarnings("unchecked")
	public static Object load(
			ResourceFactory resourceFactory,
			IResource resource, 
			String entityType)
				throws PersistenceException {

		IProgressMonitor monitor = 
			new DefaultProgressMonitor();
		monitor.begin("Start loading ... " + resource.toURI(), 1);

		Class<?> entityClass;
		
		if(entityType==null)
			entityClass = Extensions.getEntityClass(resource);
		else entityClass = Extensions.getEntityClass(entityType);
		
		IEntityPersistence<Object> entityPersistence = (IEntityPersistence<Object>) 
			createEntityPersistence( resourceFactory, entityClass);
		

		Object entity = entityPersistence.
			read(resource, monitor);
		
		//FIXME: it must go on every entityPersistence
		if (entity instanceof Artifact)
			((Artifact) entity).setResource(resource);		
	
		return entity;
	}

	
	@SuppressWarnings("unchecked")
	public static boolean store(
			ResourceFactory resourceFactory,
			IResource resource,
			Object entity) 
			throws PersistenceException {

		IProgressMonitor monitor = new DefaultProgressMonitor();
		monitor.begin("Storing ... " + resource.toURI(), 1);

		IEntityPersistence<Object> entityPersistence = (IEntityPersistence<Object>) 
			createEntityPersistence(resourceFactory, entity.getClass());

		entityPersistence.
			write(resource, entity, monitor);

		return true;
	}
}
