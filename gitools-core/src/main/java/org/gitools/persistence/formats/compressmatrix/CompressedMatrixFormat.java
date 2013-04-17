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
package org.gitools.persistence.formats.compressmatrix;

import org.gitools.matrix.model.compressmatrix.CompressDimension;
import org.gitools.matrix.model.compressmatrix.CompressElementAdapter;
import org.gitools.matrix.model.compressmatrix.CompressMatrix;
import org.gitools.matrix.model.compressmatrix.CompressRow;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.formats.AbstractResourceFormat;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CompressedMatrixFormat extends AbstractResourceFormat<CompressMatrix> {

    public static final String EXTENSION = "cmatrix";

    public CompressedMatrixFormat() {
        super(EXTENSION, CompressMatrix.class);
    }

    @Override
    protected CompressMatrix readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

        try {
            DataInputStream in = new DataInputStream(resourceLocator.openInputStream());

            // Dictionary
            byte[] dictionary = readBuffer(in);

            // Columns
            String[] columns = splitBuffer(readBuffer(in));

            // Rows
            String[] rows = splitBuffer(readBuffer(in));

            // Headers
            String[] headers = splitBuffer(readBuffer(in));

            // Values
            Map<Integer, CompressRow> values = new HashMap<Integer, CompressRow>(rows.length);

            for (int i = 0; i < rows.length; i++) {
                int row = in.readInt();
                int uncompressLength = in.readInt();
                values.put(row, new CompressRow(uncompressLength, readBuffer(in)));
            }

            in.close();

            CompressDimension rowDim = new CompressDimension(rows);
            CompressDimension colDim = new CompressDimension(columns);
            return new CompressMatrix(
                    rowDim, colDim, new CompressElementAdapter(dictionary, headers, values, colDim)
            );

        } catch (IOException e) {
            throw new PersistenceException(e);
        }


    }

    /**
     * Read a byte array that starts with an integer that contains the buffer length to read.
     *
     * @param in the input stream
     * @return the byte array
     * @throws IOException
     */
    public static byte[] readBuffer(DataInputStream in) throws IOException {

        int length = in.readInt();
        byte[] buffer = new byte[length];
        if (in.read(buffer) != length) {
            throw new PersistenceException("Incorrect buffer length");
        }

        return buffer;
    }

    private static Pattern TAB = Pattern.compile("\t");

    private static String[] splitBuffer(byte[] buffer) throws UnsupportedEncodingException {
        String line = new String(buffer, "UTF-8");
        return TAB.split(line);
    }
}
