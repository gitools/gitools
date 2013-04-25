package org.gitools.core.persistence.locators.filters.cache;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class HttpUtils {

    public void downloadFile(InputStream is, @NotNull File outputFile) throws IOException {


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
}
