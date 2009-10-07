package org.gitools.persistence;

import org.apache.commons.vfs.FileObject;

public interface IFileObjectResolver {

	FileObject createResourceFromString(String location);
}
