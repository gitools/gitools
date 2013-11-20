/*
 * #%L
 * gitools-utils
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.utils.fileutils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class IOUtils {


    public static Reader openReader(File path) throws IOException {
        if (path == null) {
            return null;
        }

        if (path.getName().endsWith(".gz")) {
            return new InputStreamReader(new GZIPInputStream(new FileInputStream(path)));
        } else {
            return new BufferedReader(new FileReader(path));
        }
    }


    public static Writer openWriter(File path) throws IOException {
        return openWriter(path, false);
    }


    private static Writer openWriter(File path, boolean append) throws IOException {
        if (path == null) {
            return null;
        }

        if (path.getName().endsWith(".gz")) {
            return new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(path, append)));
        } else {
            return new BufferedWriter(new FileWriter(path, append));
        }
    }


    public static OutputStream openOutputStream(File path) throws IOException {
        return openOutputStream(path, false);
    }


    private static OutputStream openOutputStream(File path, boolean append) throws IOException {
        if (path == null) {
            return null;
        }

        if (path.getName().endsWith(".gz")) {
            return new GZIPOutputStream(new FileOutputStream(path, append));
        } else {
            return new BufferedOutputStream(new FileOutputStream(path, append));
        }
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

    /**
     * @noinspection UnusedDeclaration
     */
    public static void copyStream(InputStream src, OutputStream dst) throws IOException {
        ReadableByteChannel source = null;
        WritableByteChannel destination = null;
        try {
            source = Channels.newChannel(src);
            destination = Channels.newChannel(dst);
            copyChannel(source, destination);
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
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
