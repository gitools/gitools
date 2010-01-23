package org.gitools.persistence;

import java.io.File;

//TODO Rename to BasePathResolver
public class FilePathResolver implements IPathResolver {

	protected File basePath;
	
	public FilePathResolver(File basePath) {
		this.basePath = basePath;
	}
	
	public FilePathResolver() {
		this(null);
	}

	@Override
	public File stringToPath(String location) {
		File file = new File(location);
		if (!file.isAbsolute())
			file = new File(basePath, location);
		return file;
	}

	@Override
	public String pathToString() {
		return basePath.toString();
	}
}
