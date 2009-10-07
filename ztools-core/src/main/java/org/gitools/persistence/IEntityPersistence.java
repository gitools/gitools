package org.gitools.persistence;

import org.apache.commons.vfs.FileObject;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public interface IEntityPersistence<T> {

	void setFileObjectResolver(IFileObjectResolver fileObjectResolver);
	
	T read(FileObject resource, IProgressMonitor monitor)
		throws PersistenceException;
	
	void write(FileObject resource, T entity, IProgressMonitor monitor)
		throws PersistenceException;;
}
