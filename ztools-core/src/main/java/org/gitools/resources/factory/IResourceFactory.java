package org.gitools.resources.factory;

import org.gitools.resources.IResource;

public interface IResourceFactory {

	IResource createResourceFromString(String location);
}
