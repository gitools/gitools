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
}
