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

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gitools.model.xml.FileResourceXmlAdapter;

@XmlJavaTypeAdapter(FileResourceXmlAdapter.class)
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
}
