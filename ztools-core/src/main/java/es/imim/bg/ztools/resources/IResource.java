package es.imim.bg.ztools.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import es.imim.bg.progressmonitor.ProgressMonitor;

public interface IResource<T> {

	T load(ProgressMonitor monitor) 
		throws FileNotFoundException, IOException, DataFormatException;

}
