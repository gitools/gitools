package org.gitools.persistence;

public abstract class AbstractEntityPersistence<T> implements IEntityPersistence<T> {

	protected IFileObjectResolver fileObjectResolver;
	
	public void setFileObjectResolver(IFileObjectResolver fileObjectResolver) {
		this.fileObjectResolver = fileObjectResolver;
	}
	
	public IFileObjectResolver getFileObjectResolver() {
		return fileObjectResolver;
	}
}
