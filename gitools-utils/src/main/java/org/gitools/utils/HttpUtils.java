/*
 * #%L
 * gitools-core
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
package org.gitools.utils;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static void downloadFile(InputStream is, @NotNull File outputFile) throws IOException {


        OutputStream out = null;

        try {
            out = new FileOutputStream(outputFile);

            byte[] buf = new byte[64 * 1024];
            int bytesRead = 0;
            while ((bytesRead = is.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    public static long getContentLength(URL url) {

        if (url == null) {
            return -1;
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
        } catch (IOException e) {
            return -1;
        }

        String contentLengthString = conn.getHeaderField("Content-Length");
        conn.disconnect();

        if (contentLengthString == null) {
            return -1;
        } else {
            return Long.parseLong(contentLengthString);
        }

    }
}
