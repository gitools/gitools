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
package org.gitools.exporter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @noinspection ALL
 */
public class AbstractHtmlExporter {

    File basePath;
    String indexName;

    AbstractHtmlExporter() {
        basePath = new File(System.getProperty("user.dir"));
        indexName = "index.html";
    }

    public File getBasePath() {
        return basePath;
    }

    public void setBasePath(File basePath) {
        this.basePath = basePath;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    // FIXME
    @Nullable
    File getTemplatePath() {
        File templatePath;
        try {
            URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
            File appPath = new File(url.toURI());
            appPath = (appPath.getParentFile() != null) ? appPath.getParentFile() : appPath;
            appPath = (appPath.getParentFile() != null) ? appPath.getParentFile() : appPath;
            templatePath = new File(appPath, "templates/default");
            if (!templatePath.exists()) {
                appPath = (appPath.getParentFile() != null) ? appPath.getParentFile() : appPath;
                templatePath = new File(appPath, "templates/default");
                if (!templatePath.exists()) {
                    return null;
                }
            }
            //System.out.println(templatePath.getAbsolutePath());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return templatePath;
    }

    void copy(@NotNull File src, File dst) throws IOException {
        File[] list = src.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return true;
                //return !name.endsWith(".vm");
            }
        });

        for (File file : list) {
            File dstFile = new File(dst, file.getName());
            //System.out.println(file.getAbsolutePath() + "\n\t-> " + dstFile.getAbsolutePath());
            if (file.isFile()) {
                copyFile(file, dstFile);
            } else if (file.isDirectory()) {
                dstFile.mkdir();
                copy(file, dstFile);
            }
        }
    }

    private void copyFile(File src, File dst) throws IOException {
        FileChannel in = new FileInputStream(src).getChannel();
        FileChannel out = new FileOutputStream(dst).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (in.read(buffer) != -1) {
            buffer.flip(); // Prepare for writing
            out.write(buffer);
            buffer.clear(); // Prepare for reading
        }
    }
}
