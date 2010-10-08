package org.gitools.persistence;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public abstract class AbstractEntityPersistence<T> implements IEntityPersistence<T> {

	protected PersistenceManager manager;

	protected Properties properties;

	public PersistenceManager getPersistenceManager() {
		return manager != null ? manager : PersistenceManager.getDefault();
	}

	@Override
	public void setPersistenceManager(PersistenceManager manager) {
		this.manager = manager;
	}

	public Properties getProperties() {
		return properties;
	}

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public String[] getMetadataKeys() {
		return new String[0];
	}

	@Override
	public Map<String, Object> readMetadata(File file, String[] keys, IProgressMonitor monitor) throws PersistenceException {
		return new HashMap<String, Object>();
	}

}
