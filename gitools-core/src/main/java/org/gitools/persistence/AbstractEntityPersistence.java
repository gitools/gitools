package org.gitools.persistence;

import java.util.Properties;

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

}
