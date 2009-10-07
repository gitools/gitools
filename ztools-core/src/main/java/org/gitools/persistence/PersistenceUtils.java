package org.gitools.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

public class PersistenceUtils {

	public static Reader openReader(FileObject path) throws FileSystemException, IOException {
		if (path == null)
			return null;
		
		FileName pathName = path.getName();
		if (!pathName.getScheme().equals("gz")
				&& path.getName().getExtension().equals(".gz"))
			return
				new BufferedReader(
						new InputStreamReader(
								new GZIPInputStream(
										path.getContent().getInputStream())));
		else
			return 
				new BufferedReader(
					new InputStreamReader(
							path.getContent().getInputStream()));
	}
	
	public static Writer openWriter(FileObject path) throws FileNotFoundException, IOException {
		if (path == null)
			return null;
		
		FileName pathName = path.getName();
		if (!pathName.getScheme().equals("gz")
				&& path.getName().getExtension().equals(".gz"))
			return
				new BufferedWriter(
						new OutputStreamWriter(
								new GZIPOutputStream(
										path.getContent().getOutputStream())));
		else
			return 
				new BufferedWriter(
					new OutputStreamWriter(
							path.getContent().getOutputStream()));
	}
}
