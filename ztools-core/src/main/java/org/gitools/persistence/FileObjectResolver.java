package org.gitools.persistence;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;

public class FileObjectResolver implements IFileObjectResolver {

	protected FileObject basePath;
	
	public FileObjectResolver(FileObject basePath) {
		this.basePath = basePath;
	}
	
	public FileObjectResolver() {
		this(null);
	}

	@Override
	public FileObject createResourceFromString(String location) {
		FileObject fileObject = null;
		try {
			FileSystemManager fsManager = VFS.getManager();
			fileObject = fsManager.resolveFile(location);
			//TODO relative paths's
		} catch (FileSystemException e) {}
		
		return fileObject;
	}
}
