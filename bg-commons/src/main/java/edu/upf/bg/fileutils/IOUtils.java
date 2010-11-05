package edu.upf.bg.fileutils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class IOUtils {

	public static Reader openReader(File path) throws IOException {
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

	public static OutputStream openOutputStream(File path) throws IOException {
		return openOutputStream(path, false);
	}

	public static OutputStream openOutputStream(File path, boolean append) throws IOException {
		if (path == null)
			return null;

		if (path.getName().endsWith(".gz"))
			return
				new GZIPOutputStream(
						new FileOutputStream(path, append));
		else
			return
				new BufferedOutputStream(
					new FileOutputStream(path, append));
	}

	public static void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	public static void copyStream(InputStream src, OutputStream dst) throws IOException {
		ReadableByteChannel source = null;
		WritableByteChannel destination = null;
		try {
			source = Channels.newChannel(src);
			destination = Channels.newChannel(dst);
			copyChannel(source, destination);
		} finally {
			if (source != null)
				source.close();
			if (destination != null)
				destination.close();
		}
	}

	private static void copyChannel(ReadableByteChannel src, WritableByteChannel dest) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
		while (src.read(buffer) != -1) {
			buffer.flip(); // prepare the buffer to be drained
			dest.write(buffer); // write to the channel, may block
			// If partial transfer, shift remainder down
			// If buffer is empty, same as doing clear()
			buffer.compact();
		}
		// EOF will leave buffer in fill state
		buffer.flip();
		// make sure the buffer is fully drained.
		while (buffer.hasRemaining())
			dest.write(buffer);
	}

}
