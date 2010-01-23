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
import java.util.regex.Pattern;
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

	public static String getRelativePath(String basePath, String targetPath) {
		
		final String pathSeparator = File.separator;

		boolean isDir = false;
		{
			File f = new File(targetPath);
			isDir = f.isDirectory();
		}
		//  We need the -1 argument to split to make sure we get a trailing
		//  "" token if the base ends in the path separator and is therefore
		//  a directory. We require directory paths to end in the path
		//  separator -- otherwise they are indistinguishable from files.
		String[] base = basePath.split(Pattern.quote(pathSeparator), -1);
		String[] target = targetPath.split(Pattern.quote(pathSeparator), 0);

		//  First get all the common elements. Store them as a string,
		//  and also count how many of them there are.
		String common = "";
		int commonIndex = 0;
		for (int i = 0; i < target.length && i < base.length; i++) {
			if (target[i].equals(base[i])) {
				common += target[i] + pathSeparator;
				commonIndex++;
			} else {
				break;
			}
		}

		if (commonIndex == 0) {
			//  Whoops -- not even a single common path element. This most
			//  likely indicates differing drive letters, like C: and D:.
			//  These paths cannot be relativized. Return the target path.
			return targetPath;
			//  This should never happen when all absolute paths
			//  begin with / as in *nix.
		}

		String relative = "";
		if (base.length == commonIndex) {
			//  Comment this out if you prefer that a relative path not start with ./
			relative = "." + pathSeparator;
		} else {
			int numDirsUp = base.length - commonIndex - (isDir ? 0 : 1); /* only subtract 1 if it  is a file. */
			//  The number of directories we have to backtrack is the length of
			//  the base path MINUS the number of common path elements, minus
			//  one because the last element in the path isn't a directory.
			for (int i = 1; i <= (numDirsUp); i++) {
				relative += ".." + pathSeparator;
			}
		}
		//if we are comparing directories then we
		if (targetPath.length() > common.length()) {
			//it's OK, it isn't a directory
			relative += targetPath.substring(common.length());
		}

		return relative;

	}
}
