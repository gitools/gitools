package org.gitools.persistence;

import org.gitools.resources.IResource;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public interface IEntityPersistence<T> {

	T read(IResource resource, IProgressMonitor monitor)
		throws PersistenceException;
	
	void write(IResource resource, T entity, IProgressMonitor monitor)
		throws PersistenceException;;
}
