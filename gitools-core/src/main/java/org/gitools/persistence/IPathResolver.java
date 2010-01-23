package org.gitools.persistence;

import java.io.File;

public interface IPathResolver {

	File stringToPath(String location);
	
	//TODO Add pathToString() : String
	String pathToString();
}
