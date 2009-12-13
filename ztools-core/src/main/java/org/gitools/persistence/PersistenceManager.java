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
import org.gitools.persistence.xml.MatrixFigureXmlPersistence;
import org.gitools.persistence.xml.ProjectXmlPersistence;
import org.gitools.persistence.xml.TableFigureXmlPersistence;

import edu.upf.bg.progressmonitor.IProgressMonitor;

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
		persistenceMap.put(MimeTypes.MATRIX_FIGURE, MatrixFigureXmlPersistence.class);
		persistenceMap.put(MimeTypes.TABLE_FIGURE, TableFigureXmlPersistence.class);
		persistenceMap.put(MimeTypes.OBJECT_MATRIX, ObjectMatrixTextPersistence.class);
		persistenceMap.put(MimeTypes.DOUBLE_MATRIX, DoubleMatrixTextPersistence.class);
		persistenceMap.put(MimeTypes.ANNOTATION_MATRIX, AnnotationMatrixTextPersistence.class);
		persistenceMap.put(MimeTypes.MODULE_MAP, ModuleMapTextIndicesPersistence.class);
	}
	
	@SuppressWarnings("unchecked")
	public <T> IEntityPersistence<T> createEntityPersistence(
			IPathResolver pathResolver,
			String mimeType) {

		Class<?> persistenceClass = persistenceMap.get(mimeType);
		
		try {
			Constructor<?> c = persistenceClass.getConstructor();
			IEntityPersistence<T> entityPersistence =
				(IEntityPersistence<T>) c.newInstance();
			entityPersistence.setPathResolver(pathResolver);
			
			return entityPersistence;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Object load(
			IPathResolver pathResolver,
			File file,
			IProgressMonitor monitor)
				throws PersistenceException {
		return load(pathResolver, file, null, monitor);
	}
	
	public Object load(
			IPathResolver pathResolver,
			File file,
			String mimeType,
			IProgressMonitor monitor)
				throws PersistenceException {
		
		FileKey fileKey = new FileKey(file);
		
		if (entityCache.containsKey(fileKey))
			return entityCache.get(fileKey);

		if (mimeType == null)
			mimeType = MimeTypeManager.getDefault().fromFile(file);
		
		IEntityPersistence<Object> entityPersistence = (IEntityPersistence<Object>) 
			createEntityPersistence(pathResolver, mimeType);

	
		//FIXME: heap problems with annotations..
		Object entity = entityPersistence.read(file, monitor);

		entityCache.put(fileKey, entity);
		entityFileMap.put(entity, file);

		return entity;
	}

	public void store(
			IPathResolver pathResolver,
			File file,
			Object entity,
			IProgressMonitor monitor) 
			throws PersistenceException {

		String mimeType = MimeTypeManager.getDefault().fromClass(entity.getClass());
		
		IEntityPersistence<Object> entityPersistence = (IEntityPersistence<Object>) 
			createEntityPersistence(pathResolver, mimeType);

		entityPersistence.write(file, entity, monitor);
		
		entityCache.put(new FileKey(file), entity);
		entityFileMap.put(entity, file);
	}
	
	public File getEntityFile(Object entity) {
		return entityFileMap.get(entity);
	}
}
