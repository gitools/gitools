package org.gitools.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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

public class PersistenceUtils {

	public static Reader openReader(File path) throws IOException {
		if (path == null)
			return null;
		
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
	
	public static Writer openWriter(File path) throws IOException {
		return openWriter(path, false);
	}
	
	public static Writer openWriter(File path, boolean append) throws IOException {
		if (path == null)
			return null;
		
		if (path.getName().endsWith(".gz"))
			return
				new OutputStreamWriter(
					new GZIPOutputStream(
							new FileOutputStream(path, append)));
		else
			return 
				new BufferedWriter(
					new FileWriter(path, append));
	}
}
