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

import org.apache.commons.csv.CSVStrategy;

public class ResourceFile {

	public static final CSVStrategy defaultCsvStrategy = 
		new CSVStrategy('\t', '"', '#', true, true, true);
	
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
		return openReader(resourcePath);
	}
	
	protected Writer openWriter() throws FileNotFoundException, IOException {
		return openWriter(resourcePath);
	}
	
	public String getResourcePath() {
		return resourcePath;
	}
	
	public File getResourceFile() {
		return resourceFile;
	}

	public static Reader openReader(File path) throws FileNotFoundException, IOException {
		if (path == null)
			return null;
		
		if (path.getName().endsWith(".gz"))
			return
				new InputStreamReader(
					new GZIPInputStream(
							new FileInputStream(path)));
		else
			return 
				new BufferedReader(
					new FileReader(path));
	}
	
	protected Writer openWriter(File path) throws FileNotFoundException, IOException {
		if (path == null)
			return null;
		
		if (path.getName().endsWith(".gz"))
			return
				new OutputStreamWriter(
					new GZIPOutputStream(
							new FileOutputStream(path)));
		else
			return 
				new BufferedWriter(
					new FileWriter(path));
	}
	
	public static Reader openReader(String path) throws FileNotFoundException, IOException {
		if (path == null)
			return null;
		
		return openReader(new File(path));
	}
	
	protected Writer openWriter(String path) throws FileNotFoundException, IOException {
		if (path == null)
			return null;
		
		return openWriter(new File(path));
	}
}
