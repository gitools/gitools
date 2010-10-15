package org.gitools.persistence;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.Properties;

public class PersistenceManager implements Serializable {

	private static final long serialVersionUID = -1442103565401901838L;

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

	// Maps PersistenceClass <-> Mime <-> EntityClass <-> Extension <-> Mime

	private final Map<String, Class<? extends IEntityPersistence<?>>> mimeToPClass =
		new HashMap<String, Class<? extends IEntityPersistence<?>>>();

	private final Map<String, String> mimeToExt = new HashMap<String, String>();

	private final Map<String, String> extToMime = new HashMap<String, String>();

	private final Map<Class<?>, String> eclassToMime = new HashMap<Class<?>, String>();
	

	private final Map<FileRef, Object> entityCache = new HashMap<FileRef, Object>();
	private final Map<Object, FileRef> entityFileRefMap = new HashMap<Object, FileRef>();
	
	public static PersistenceManager getDefault() {
		if (defaultManager == null)
			defaultManager = new PersistenceManager();
		return defaultManager;
	}
	
	public PersistenceManager() {
	}

	public void registerFormat(String mime, String extension,
			Class<? extends IEntityPersistence<?>> pclass) {
		registerFormat(mime, extension, null, pclass);
	}

	public void registerFormat(String mime, String extension,
			Class<?> eclass, Class<? extends IEntityPersistence<?>> pclass) {

		mimeToPClass.put(mime, pclass);
		mimeToExt.put(mime, extension);

		if (eclass != null)
			eclassToMime.put(eclass, mime);

		registerExtension(extension, mime);
	}

	public void registerExtension(String extension, String mime) {
		extToMime.put(extension, mime);
		extToMime.put(extension + ".gz", mime);
	}

	public String getMimeFromFile(String fileName) {
		for (Map.Entry<String, String> entry : extToMime.entrySet())
			if (fileName.endsWith(entry.getKey()))
				return entry.getValue();
		return null;
	}

	public String getMimeFromEntity(Class<?> entityClass) {
		return eclassToMime.get(entityClass);
	}

	public String getExtensionFromMime(String mime) {
		return mimeToExt.get(mime);
	}

	public String getExtensionFromEntity(Class<?> entityClass) {
		return getExtensionFromMime(getMimeFromEntity(entityClass));
	}

	@SuppressWarnings("unchecked")
	public <T> IEntityPersistence<T> createEntityPersistence(
			String mimeType, Properties properties) {

		Class<?> persistenceClass = mimeToPClass.get(mimeType);
		
		try {
			Constructor<?> c = persistenceClass.getConstructor();
			IEntityPersistence<T> entityPersistence =
				(IEntityPersistence<T>) c.newInstance();

			entityPersistence.setPersistenceManager(this);
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

		return load(file, null, new Properties(), monitor);
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
			Properties properties,
			IProgressMonitor monitor)
				throws PersistenceException {

		return load(file, null, properties, monitor);
	}

	public Object load(
			File file,
			String mimeType,
			Properties properties,
			IProgressMonitor monitor)
				throws PersistenceException {

		if (mimeType == null)
			mimeType = getMimeFromFile(file.getName());

		FileRef fileRef = new FileRef(file, mimeType);
		
		if (entityCache.containsKey(fileRef))
			return entityCache.get(fileRef);
		
		IEntityPersistence<Object> entityPersistence = (IEntityPersistence<Object>) 
			createEntityPersistence(mimeType, properties);

		Object entity = entityPersistence.read(file, monitor);

		entityCache.put(fileRef, entity);
		entityFileRefMap.put(entity, fileRef);

		return entity;
	}

	public void store(
			File file,
			Object entity,
			IProgressMonitor monitor)
			throws PersistenceException {

		String mimeType = getMimeFromEntity(entity.getClass());

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

		FileRef fileRef = new FileRef(file, mimeType);

		entityCache.put(fileRef, entity);
		entityFileRefMap.put(entity, fileRef);
	}

	public void clearEntityCache() {
		entityCache.clear();
		entityFileRefMap.clear();
	}

	public void clearEntityCache(Object entity) {
		FileRef fileRef = entityFileRefMap.get(entity);
		if (fileRef != null) {
			entityFileRefMap.remove(entity);
			entityCache.remove(fileRef);
		}
	}

	public void clearEntityCache(File file, String mimeType) {
		FileRef fileRef = new FileRef(file, mimeType);

		if (entityCache.containsKey(fileRef)) {
			Object entity = entityCache.get(fileRef);
			entityFileRefMap.remove(entity);
			entityCache.remove(fileRef);
		}
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
