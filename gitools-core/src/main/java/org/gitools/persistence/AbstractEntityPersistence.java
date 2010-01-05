package org.gitools.persistence;

public abstract class AbstractEntityPersistence<T> implements IEntityPersistence<T> {

	protected IPathResolver pathResolver;
	
	public void setPathResolver(IPathResolver pathResolver) {
		this.pathResolver = pathResolver;
	}
	
	public IPathResolver getFileObjectResolver() {
		return pathResolver;
	}
}
