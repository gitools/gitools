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
import org.gitools.persistence.text.GeneMatrixPersistence;
import org.gitools.persistence.text.GeneMatrixTransposedPersistence;
import org.gitools.persistence.text.GeneSetPersistence;
import org.gitools.persistence.text.ModuleMapText2CPersistence;

public class PersistenceManager implements Serializable {

	private static final long serialVersionUID = -1442103565401901838L;

	/*private static class FileKey {
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
	}*/

	public static class FileRef {
		private File file;
		private String mime;

		public FileRef(File file, String mime) {
			this.file = file;
			this.mime = mime;
		}
		public File getFile() {
			return file;
		}
		public String getMime() {
			return mime;
		}
		@Override
		public int hashCode() {
			int code = file.hashCode();
			code = 37 * code + (int) file.lastModified();
			code = 37 * code + mime.hashCode();
			return code;
		}
		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			else if (!(obj instanceof FileRef))
				return false;
			FileRef f = (FileRef) obj;
			return file.hashCode() == f.file.hashCode()
				&& file.lastModified() == f.file.lastModified()
				&& ((mime == null && f.mime == null) || (mime != null && mime.equals(f.mime)));
		}
	}
	
	private static PersistenceManager defaultManager;
	
	private final Map<String, Class<? extends IEntityPersistence<?>>> persistenceMap =
		new HashMap<String, Class<? extends IEntityPersistence<?>>>();

	private final Map<FileRef, Object> entityCache = new HashMap<FileRef, Object>();
	private final Map<Object, FileRef> entityFileRefMap = new HashMap<Object, FileRef>();
	
	public static final PersistenceManager getDefault() {
		if (defaultManager == null)
			defaultManager = new PersistenceManager();
		return defaultManager;
	}
	
	public PersistenceManager() {
		persistenceMap.put(MimeTypes.PROJECT, ProjectXmlPersistence.class);
		persistenceMap.put(MimeTypes.CONTENT, ContainerXmlPersistence.class);
		persistenceMap.put(MimeTypes.TABLE_FIGURE, TableFigureXmlPersistence.class);

		persistenceMap.put(MimeTypes.ENRICHMENT_ANALYSIS, EnrichmentAnalysisXmlPersistence.class);

		persistenceMap.put(MimeTypes.HEATMAP_FIGURE, HeatmapXmlPersistence.class);
		
		persistenceMap.put(MimeTypes.GENE_SET, GeneSetPersistence.class);
		
		persistenceMap.put(MimeTypes.GENE_MATRIX, GeneMatrixPersistence.class);
		persistenceMap.put(MimeTypes.GENE_MATRIX_TRANSPOSED, GeneMatrixTransposedPersistence.class);
		persistenceMap.put(MimeTypes.OBJECT_MATRIX, ObjectMatrixTextPersistence.class);
		persistenceMap.put(MimeTypes.DOUBLE_MATRIX, DoubleMatrixTextPersistence.class);
		persistenceMap.put(MimeTypes.DOUBLE_BINARY_MATRIX, DoubleBinaryMatrixTextPersistence.class);

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
		
		FileRef fileKey = new FileRef(file, mimeType);
		
		if (entityCache.containsKey(fileKey))
			return entityCache.get(fileKey);

		if (mimeType == null)
			mimeType = MimeTypeManager.getDefault().fromFile(file);
		
		IEntityPersistence<Object> entityPersistence = (IEntityPersistence<Object>) 
			createEntityPersistence(mimeType, properties);

	
		//FIXME: heap problems with annotations..
		Object entity = entityPersistence.read(file, monitor);

		entityCache.put(fileKey, entity);
		entityFileRefMap.put(entity, new FileRef(file, mimeType));

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
		
		entityCache.put(new FileRef(file, mimeType), entity);
		entityFileRefMap.put(entity, new FileRef(file, mimeType));
	}

	@Deprecated // use getEntityFileRef() instead
	public File getEntityFile(Object entity) {
		return entityFileRefMap.get(entity).getFile();
	}

	@Deprecated // use getEntityFileRef() instead
	public String getEntityMime(Object entity) {
		return entityFileRefMap.get(entity).getMime();
	}

	public FileRef getEntityFileRef(Object entity) {
		return entityFileRefMap.get(entity);
	}
}
