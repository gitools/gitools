package org.gitools.resources;

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
import java.net.URI;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Deprecated
public class FileResource implements IResource {

	private static final long serialVersionUID = 1165427781549776789L;

	private File file;
	
	public FileResource() {
	}
	
	public FileResource(String path) {
		this(new File(path));
	}
	
	public FileResource(File file) {
		this.file = file;	
	}
	
	public FileResource(URI uri){
		this.file = new File(uri);
	}
	
	@Override
	public Reader openReader() throws FileNotFoundException, IOException {
		return openReader(file);
	}
	
	@Override
	public Writer openWriter() throws FileNotFoundException, IOException {
		return openWriter(file);
	}
	
	@Override
	public URI toURI() {
		return file.toURI();
	}
	
	public String getResourcePath() {
		return file.getAbsolutePath();
	}
	
	public File getFile() {
		return file;
	}

	public static Reader openReader(String path) throws FileNotFoundException, IOException {
		if (path == null)
			return null;
		
		return openReader(new File(path));
	}
	
	public static Reader openReader(File path) throws FileNotFoundException, IOException {
		if (path == null)
			return null;
		
		if (path.getName().endsWith(".gz"))
			return
				new BufferedReader(
						new InputStreamReader(
								new GZIPInputStream(
										new FileInputStream(path))));
		else
			return 
				new BufferedReader(
					new FileReader(path));
	}
	
	public static Writer openWriter(String path) throws FileNotFoundException, IOException {
		if (path == null)
			return null;
		
		return openWriter(new File(path));
	}
	
	public static Writer openWriter(File path) throws FileNotFoundException, IOException {
		if (path == null)
			return null;
		
		if (path.getName().endsWith(".gz"))
			return
				new BufferedWriter(
						new OutputStreamWriter(
								new GZIPOutputStream(
										new FileOutputStream(path))));
		else
			return 
				new BufferedWriter(
					new FileWriter(path));
	}

	@Override
	public FileResource resolve(String str) {
		File rfile = new File(file, str);
		return new FileResource(rfile);
	}

	@Override
	public IResource relativize(IResource resource) {
		return new FileResource (file.toURI().relativize(resource.toURI()));
	}

	@Override
	public IResource[] list() {
		File[] files = file.listFiles();
		IResource[] resources = new IResource[files.length];
		for (int i = 0; i < files.length; i++)
			resources[i] = new FileResource(files[i]);
			
		return resources;
	}
	
	@Override
	public boolean isContainer() {
		return file.isDirectory();
	}

	@Override
	public boolean exists() {
		return file.exists();
	}

	@Override
	public void mkdir() {
		file.mkdirs();
	}
	
	@Override
	public IResource getParent() {
		return new FileResource(file.getParentFile());
	}
	
	@Override
	public String toString() {
		return file.getAbsolutePath();
	}
}