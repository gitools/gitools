package org.gitools.persistence;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

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
import org.gitools.persistence.text.AnnotationMatrixTextPersistence;
import org.gitools.persistence.text.DoubleMatrixTextPersistence;
import org.gitools.persistence.text.ModuleMapTextIndicesPersistence;
import org.gitools.persistence.text.ObjectMatrixTextPersistence;
import org.gitools.persistence.xml.AbstractXmlPersistence;
import org.gitools.persistence.xml.EnrichmentAnalysisXmlPersistence;
import org.gitools.persistence.xml.MatrixFigureXmlPersistence;
import org.gitools.persistence.xml.ResourceXmlPersistence;
import org.gitools.persistence.xml.TableFigureXmlPersistence;
import org.gitools.resources.IResource;
import org.gitools.resources.factory.ResourceFactory;

import edu.upf.bg.progressmonitor.DefaultProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class PersistenceManager {

	private static final Map<Class<? extends Object>, Class<?>> 
		persistenceMap = new HashMap<Class<? extends Object>, Class<?>>();

	private static final Map<Integer, Object > cache = new HashMap<Integer, Object>();
	
	static {
		persistenceMap.put(Project.class, AbstractXmlPersistence.class);
		persistenceMap.put(EnrichmentAnalysis.class, EnrichmentAnalysisXmlPersistence.class);
		persistenceMap.put(ResourceContainer.class, ResourceXmlPersistence.class);
		persistenceMap.put(MatrixFigure.class, MatrixFigureXmlPersistence.class);
		persistenceMap.put(TableFigure.class,  TableFigureXmlPersistence.class);
		persistenceMap.put(ObjectMatrix.class, ObjectMatrixTextPersistence.class);
		persistenceMap.put(DoubleMatrix.class, DoubleMatrixTextPersistence.class);
		persistenceMap.put(AnnotationMatrix.class, AnnotationMatrixTextPersistence.class);
		persistenceMap.put(ModuleMap.class, ModuleMapTextIndicesPersistence.class);
	}

	@SuppressWarnings("unchecked")
	public static <T> IEntityPersistence<T> createEntityPersistence(
			ResourceFactory resourceFactory,
			Class<T> entityClass) {

		Class<?> resourceClass = persistenceMap.get(entityClass);
		
		try {
			Constructor<?> c;
			
			if (resourceClass.equals(AbstractXmlPersistence.class)) {
				c = resourceClass.getConstructor(Class.class);
				return (IEntityPersistence) c.newInstance(entityClass);
			}
			
			if ((resourceClass.equals(MatrixFigureXmlPersistence.class)) ||
				(resourceClass.equals(TableFigureXmlPersistence.class))  ||
				(resourceClass.equals(EnrichmentAnalysisXmlPersistence.class))  ||
				(resourceClass.equals(ResourceXmlPersistence.class))) {
				
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

		int key =resource.hashCode();
		if (cache.containsKey(key)){
			System.out.println("using cache for " + resource.toURI());
			return cache.get(key);
		}
		
		IProgressMonitor monitor = 
			new DefaultProgressMonitor();
		monitor.begin("Start loading ... " + resource.toURI(), 1);

		Class<?> entityClass;
		
		if(entityType==null)
			entityClass = ResourceNameSuffixes.getEntityClass(resource);
		else entityClass = ResourceNameSuffixes.getEntityClass(entityType);
		
		IEntityPersistence<Object> entityPersistence = (IEntityPersistence<Object>) 
			createEntityPersistence( resourceFactory, entityClass);
		

		Object entity = entityPersistence.
			read(resource, monitor);
		
		//FIXME: it must go on every entityPersistence
		if (entity instanceof Artifact)
			((Artifact) entity).setResource(resource);		
	
		if(!cache.containsKey(key))
			cache.put(key, entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public static boolean store(
			ResourceFactory resourceFactory,
			IResource resource,
			Object entity) 
			throws PersistenceException {

		// FIXME the monitor should be passed through method parameters
		IProgressMonitor monitor = new DefaultProgressMonitor();
		monitor.begin("Storing ... " + resource.toURI(), 1);

		IEntityPersistence<Object> entityPersistence = (IEntityPersistence<Object>) 
			createEntityPersistence(resourceFactory, entity.getClass());

		entityPersistence.
			write(resource, entity, monitor);

		return true;
	}
}
