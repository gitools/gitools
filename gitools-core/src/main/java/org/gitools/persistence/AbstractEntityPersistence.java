package org.gitools.persistence;

import java.util.Properties;

public abstract class AbstractEntityPersistence<T> implements IEntityPersistence<T> {

	protected Properties properties;

	public Properties getProperties() {
		return properties;
	}

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
