package es.imim.bg.ztools.resources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ResourceFile {

	private String resourcePath;
	private File resourceFile;
	
	public ResourceFile(String resourceName) {
		this(new File(resourceName));
	}
	
	public ResourceFile(File resourceFile) {
		this.resourcePath = resourceFile.getAbsolutePath();
		this.resourceFile = resourceFile;
	}
	
	protected Reader openReader() throws FileNotFoundException, IOException {
		if (resourcePath == null)
			return null;
		
		if (resourcePath.endsWith(".gz"))
			return
				new InputStreamReader(
					new GZIPInputStream(
							new FileInputStream(
									new File(resourcePath))));
		else
			return 
				new BufferedReader(
					new FileReader(
							new File(resourcePath)));
	}
	
	protected Writer openWriter() throws FileNotFoundException, IOException {
		if (resourcePath == null)
			return null;
		
		if (resourcePath.endsWith(".gz"))
			return
				new OutputStreamWriter(
					new GZIPOutputStream(
							new FileOutputStream(
									new File(resourcePath))));
		else
			return 
				new BufferedWriter(
					new FileWriter(
							new File(resourcePath)));
	}
	
	public String getResourcePath() {
		return resourcePath;
	}
	
	public File getResourceFile() {
		return resourceFile;
	}
}
