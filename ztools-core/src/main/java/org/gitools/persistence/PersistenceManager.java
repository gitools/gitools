package org.gitools.persistence;

import org.gitools.resources.IResource;

public class PersistenceManager {

	@SuppressWarnings("unchecked")
	public static IEntityPersistence createEntityPersistence(IResource resource) {
		// TODO
		return null;
	}
	
	public static <T> IEntityPersistence<T> createEntityPersistence(Class<T> entityClass) {
		// TODO
		return null;
	}
}
