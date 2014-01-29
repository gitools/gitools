/*
 * #%L
 * gitools-biomart
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
package org.gitools.datasources.biomart.queryhandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

public class TsvFileQueryHandler implements BiomartQueryHandler {

    private final File file;
    private final boolean compressionEnabled;

    private PrintWriter writer;

    public TsvFileQueryHandler(File file, boolean compresssionEnabled) {
        this.file = file;
        this.compressionEnabled = compresssionEnabled;
    }

    @Override
    public void begin() throws Exception {
        OutputStream stream = new FileOutputStream(file);
        if (compressionEnabled) {
            stream = new GZIPOutputStream(stream);
        }

        writer = new PrintWriter(stream);
    }

    @Override
    public void line(String[] rowFields) {
        if (rowFields.length > 0) {
            writer.print(rowFields[0]);
            for (int i = 1; i < rowFields.length; i++) {
                writer.print('\t');
                writer.print(rowFields[i]);
            }
            writer.println();
        }
    }

    @Override
    public void end() {
        writer.close();
    }

}
