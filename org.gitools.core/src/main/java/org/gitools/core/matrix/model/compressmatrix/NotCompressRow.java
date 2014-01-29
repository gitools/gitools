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
package org.gitools.core.matrix.model.compressmatrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * This class contains all the values of an uncompressed row
 */
public class NotCompressRow {

    private static final Logger log = LoggerFactory.getLogger(NotCompressRow.class);
    private int row;
    private int totalLayers;
    private double[][] values;
    private boolean[] indices;
    private CompressDimension columns;

    /**
     * @param columns The columns dimension
     */
    public NotCompressRow(int row, int totalLayers, CompressDimension columns) {
        this.row = row;
        this.totalLayers = totalLayers;
        this.values = new double[columns.size()][totalLayers];
        this.indices = new boolean[columns.size()];
        Arrays.fill(indices, false);
        this.columns = columns;
    }

    /**
     * Gets row position.
     *
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * Append a new column fields.
     *
     * @param value the fields
     */
    public void append(String value) {
        int column = columns.indexOf(AbstractCompressor.parseField(value, 0));
        double[] cells = parseDoubles(value);
        indices[column] = true;
        values[column] = cells;
    }

    /**
     * Converts the content of the row into a sequence of [column position int],[values length int],[values byte buffer]
     *
     * @return
     * @throws java.io.IOException
     */
    public byte[] toByteArray() throws IOException {

        // Write the buffer
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(indices.length * ((8 * totalLayers) + 4));
        DataOutputStream out = new DataOutputStream(bytes);

        for (int column = 0; column < indices.length; column++) {

            if (indices[column]) {
                // Column position
                out.writeInt(column);

                // Column values
                byte[] line = createColumnLine(values[column]);
                out.write(line);
            }
        }
        out.close();
        bytes.close();
        return bytes.toByteArray();
    }

    private double[] parseDoubles(String fields) {

        double values[] = new double[totalLayers];

        for (int i = 0; i < totalLayers; i++) {
            String value = AbstractCompressor.parseField(fields, i + 2);
            values[i] = Double.NaN;
            if (value != null) {
                if (!value.equals("-")) {
                    try {
                        values[i] = Double.parseDouble(value);
                    } catch (NumberFormatException e) {
                        log.error("Malformed number '" + value + "'", e);
                    }
                }
            }
        }

        return values;
    }

    /**
     * Returns a byte array with all the fields except the two first that are the row and the column.
     *
     * @param values
     * @return
     * @throws java.io.UnsupportedEncodingException
     *
     */
    private byte[] createColumnLine(double[] values) throws UnsupportedEncodingException {
        byte[] bytes = new byte[8 * values.length];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        for (double value : values) {
            buffer.putDouble(value);
        }
        return bytes;
    }
}
