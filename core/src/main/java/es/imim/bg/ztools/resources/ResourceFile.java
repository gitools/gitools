package es.imim.bg.ztools.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

public class ResourceFile {

	private String resourceName;
	
	public ResourceFile(String resourceName) {
		this.resourceName = resourceName;
	}
	
	protected Reader openReader() throws FileNotFoundException, IOException {
		if (resourceName == null)
			return null;
		
		if (resourceName.endsWith(".gz"))
			return
				new InputStreamReader(
					new GZIPInputStream(
							new FileInputStream(
									new File(resourceName))));
		else
			return 
				new BufferedReader(
					new FileReader(
							new File(resourceName)));
	}
	
	public String getResourceName() {
		return resourceName;
	}
}
