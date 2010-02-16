package org.gitools.persistence;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.gitools.persistence.text.AnnotationMatrixTextPersistence;
import org.gitools.persistence.text.DoubleMatrixTextPersistence;
import org.gitools.persistence.text.ModuleMapTextIndicesPersistence;
import org.gitools.persistence.text.ObjectMatrixTextPersistence;
import org.gitools.persistence.xml.ContainerXmlPersistence;
import org.gitools.persistence.xml.EnrichmentAnalysisXmlPersistence;
import org.gitools.persistence.xml.HeatmapXmlPersistence;
import org.gitools.persistence.xml.ProjectXmlPersistence;
import org.gitools.persistence.xml.TableFigureXmlPersistence;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.Properties;
import org.gitools.persistence.text.DoubleBinaryMatrixTextPersistence;
import org.gitools.persistence.text.ElementListsTextPersistence;
import org.gitools.persistence.text.ModuleMapText2CPersistence;

public class PersistenceManager implements Serializable {

	private static final long serialVersionUID = -1442103565401901838L;

	private static class FileKey {
		private File file;
		public FileKey(File file) {
			this.file = file;
		}
		@Override
		public int hashCode() {			
			return (int) (37 * file.hashCode() + file.lastModified());
		}
		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			else if (!(obj instanceof FileKey))
				return false;
			FileKey f = (FileKey) obj;
			return file.hashCode() == f.file.hashCode()
				&& file.lastModified() == f.file.lastModified();
		}
	}
	
	private static PersistenceManager defaultManager;
	
	private final Map<String, Class<? extends IEntityPersistence<?>>> persistenceMap =
		new HashMap<String, Class<? extends IEntityPersistence<?>>>();

	private final Map<FileKey, Object> entityCache = new HashMap<FileKey, Object>();
	private final Map<Object, File> entityFileMap = new HashMap<Object, File>();
	
	public static final PersistenceManager getDefault() {
		if (defaultManager == null)
			defaultManager = new PersistenceManager();
		return defaultManager;
	}
	
	public PersistenceManager() {
		persistenceMap.put(MimeTypes.PROJECT, ProjectXmlPersistence.class);
		persistenceMap.put(MimeTypes.ENRICHMENT_ANALYSIS, EnrichmentAnalysisXmlPersistence.class);
		persistenceMap.put(MimeTypes.CONTENT, ContainerXmlPersistence.class);
		persistenceMap.put(MimeTypes.HEATMAP_FIGURE, HeatmapXmlPersistence.class);
		persistenceMap.put(MimeTypes.TABLE_FIGURE, TableFigureXmlPersistence.class);
		persistenceMap.put(MimeTypes.OBJECT_MATRIX, ObjectMatrixTextPersistence.class);
		persistenceMap.put(MimeTypes.DOUBLE_MATRIX, DoubleMatrixTextPersistence.class);
		persistenceMap.put(MimeTypes.DOUBLE_BINARY_MATRIX, DoubleBinaryMatrixTextPersistence.class);
		persistenceMap.put(MimeTypes.ELEMENT_LISTS, ElementListsTextPersistence.class);
		persistenceMap.put(MimeTypes.ANNOTATION_MATRIX, AnnotationMatrixTextPersistence.class);
		persistenceMap.put(MimeTypes.MODULES_2C_MAP, ModuleMapText2CPersistence.class);
		persistenceMap.put(MimeTypes.MODULES_INDEXED_MAP, ModuleMapTextIndicesPersistence.class);
	}
	
	@SuppressWarnings("unchecked")
	public <T> IEntityPersistence<T> createEntityPersistence(
			String mimeType, Properties properties) {

		Class<?> persistenceClass = persistenceMap.get(mimeType);
		
		try {
			Constructor<?> c = persistenceClass.getConstructor();
			IEntityPersistence<T> entityPersistence =
				(IEntityPersistence<T>) c.newInstance();

			entityPersistence.setProperties(properties);

			return entityPersistence;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Object load(
			File file,
			IProgressMonitor monitor)
				throws PersistenceException {

		return load(file, null, monitor);
	}

	public Object load(
			File file,
			String mimeType,
			IProgressMonitor monitor)
				throws PersistenceException {

		return load(file, mimeType, new Properties(), monitor);
	}

	public Object load(
			File file,
			String mimeType,
			Properties properties,
			IProgressMonitor monitor)
				throws PersistenceException {
		
		FileKey fileKey = new FileKey(file);
		
		if (entityCache.containsKey(fileKey))
			return entityCache.get(fileKey);

		if (mimeType == null)
			mimeType = MimeTypeManager.getDefault().fromFile(file);
		
		IEntityPersistence<Object> entityPersistence = (IEntityPersistence<Object>) 
			createEntityPersistence(mimeType, properties);

	
		//FIXME: heap problems with annotations..
		Object entity = entityPersistence.read(file, monitor);

		entityCache.put(fileKey, entity);
		entityFileMap.put(entity, file);

		return entity;
	}

	public void store(
			File file,
			Object entity,
			IProgressMonitor monitor)
			throws PersistenceException {

		String mimeType = MimeTypeManager.getDefault().fromClass(entity.getClass());

		store(file, mimeType, entity, new Properties(), monitor);
	}

	public void store(
			File file,
			String mimeType,
			Object entity,
			IProgressMonitor monitor)
			throws PersistenceException {

		store(file, mimeType, entity, new Properties(), monitor);
	}

	public void store(
			File file,
			String mimeType,
			Object entity,
			Properties properties,
			IProgressMonitor monitor) 
			throws PersistenceException {
		
		IEntityPersistence<Object> entityPersistence = (IEntityPersistence<Object>) 
			createEntityPersistence(mimeType, properties);

		entityPersistence.write(file, entity, monitor);
		
		entityCache.put(new FileKey(file), entity);
		entityFileMap.put(entity, file);
	}
	
	public File getEntityFile(Object entity) {
		return entityFileMap.get(entity);
	}
}
