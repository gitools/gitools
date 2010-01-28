package org.gitools.persistence;

import java.io.File;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public interface IEntityPersistence<T> {

	T read(File file, IProgressMonitor monitor)
		throws PersistenceException;
	
	void write(File file, T entity, IProgressMonitor monitor)
		throws PersistenceException;;
}
