package org.gitools.persistence;

import java.io.File;

public interface IPathResolver {

	//TODO Rename to stringToPath
	File createResourceFromString(String location);
	
	//TODO Add pathToString() : String
	String pathToString();
}
