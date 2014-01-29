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
package org.gitools.analysis._DEPRECATED.matrix.model.compressmatrix;

import org.gitools.api.matrix.MatrixDimensionKey;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.Deflater;

public abstract class AbstractCompressor {
    protected static final char SEPARATOR = '\t';

    private static final int MAX_LINES_TO_DICTIONARY = 5000000;
    private static final int MIN_FREQUENCY = 2;
    private static final int MAX_DICTIONARY_ENTRIES = 10000;

    private CompressDimension rows;
    private CompressDimension columns;
    private String[] header;
    private byte[] dictionary;
    private byte[] outBuffer;
    private Deflater deflater = new Deflater();
    private long fileLinesCount;
    private long maxLineLength;

    public AbstractCompressor() {
    }

    protected CompressDimension getColumns() {
        return columns;
    }

    protected CompressDimension getRows() {
        return rows;
    }

    protected String[] getHeader() {
        return header;
    }

    protected byte[] getDictionary() {
        return dictionary;
    }

    protected long getMaxLineLength() {
        return maxLineLength;
    }

    protected long getTotalLines() {
        return fileLinesCount;
    }

    /**
     * Convert and array of Strings into a byte array
     *
     * @param values The strings
     * @return The byte arrays
     * @throws java.io.UnsupportedEncodingException
     *
     */
    public static byte[] stringToByteArray(String[] values) throws UnsupportedEncodingException {
        StringBuilder buffer = new StringBuilder(values.length * 10);
        for (String value : values) {
            buffer.append(value).append('\t');
        }
        return buffer.toString().getBytes("UTF-8");
    }

    /**
     * Fast field split
     *
     * @param str The string to split using SEPARATOR
     * @param num The position to return
     * @return The string at 'num' position using 'SEPARATOR' in 'str' string.
     */
    protected static String parseField(String str, int num) {
        int start = -1;
        for (int i = 0; i < num; i++) {
            start = str.indexOf(SEPARATOR, start + 1);
            if (start == -1)
                return null;
        }

        int end = str.indexOf(SEPARATOR, start + 1);
        if (end == -1)
            end = str.length();

        String result = str.substring(start + 1, end);
        return result.replace('"', ' ').trim();
    }

    protected CompressRow compressRow(NotCompressRow row) throws Exception {
        // Prepare a buffer with all the columns of this row
        byte[] input = row.toByteArray();

        // Compress the columns into 'outBuffer'
        int length = compressDeflater(input);

        byte[] content = Arrays.copyOf(outBuffer, length);
        return new CompressRow(input.length, content);

    }


    /**
     * Compress an 'input' byte array into the 'outBuffer'
     *
     * @param input Array to compress
     * @return The length of the compressed buffer
     * @throws Exception
     */
    private int compressDeflater(byte[] input) throws Exception {
        deflater.reset();
        deflater.setInput(input);
        deflater.setDictionary(dictionary);
        deflater.finish();

        return deflater.deflate(outBuffer);
    }

    /**
     * Builds a frequency base compression dictionary and look for all the columns
     * and rows identifiers available.
     *
     * @param reader Input matrix reader
     * @throws Exception
     */
    protected void initialize(IMatrixReader reader) throws Exception {
        // Some internal variables
        Map<String, Integer> frequencies = new HashMap<>();
        Set<String> rows = new HashSet<>(1000);
        Set<String> columns = new HashSet<>(1000);
        String[] fields;

        // Read the headers
        String[] headers = reader.readNext();
        header = Arrays.copyOfRange(headers, 2, headers.length);

        int maxLineLength = 0;
        fileLinesCount = 0;
        while ((fields = reader.readNext()) != null) {
            fileLinesCount++;

            if (!columns.contains(fields[0])) {
                columns.add(fields[0]);
            }

            if (!rows.contains(fields[1])) {
                rows.add(fields[1]);
            }

            // Update frequencies
            if (fileLinesCount < MAX_LINES_TO_DICTIONARY) {
                int length = 0;
                for (int i = 2; i < fields.length; i++) {
                    length += fields[i].length() + 1;
                    Integer freq = frequencies.get(fields[i]);
                    freq = (freq == null) ? 1 : freq + 1;
                    frequencies.put(fields[i], freq);
                }

                if (length > maxLineLength) {
                    maxLineLength = length;
                }
            }
        }

        this.maxLineLength = maxLineLength;
        reader.close();

        // Filter entries with frequency = 1
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(frequencies.size());
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            if (entry.getValue() > MIN_FREQUENCY) {
                entries.add(entry);
            }
        }

        // Sort the frequency table by frequency
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue().compareTo(o2.getValue()));
            }
        });

        // Convert the frequency table into a deflate dictionary
        StringBuilder buffer = new StringBuilder();
        long count = 0;
        for (Map.Entry<String, Integer> entry : entries) {
            buffer.append(entry.getKey());

            if (count++ > MAX_DICTIONARY_ENTRIES) {
                break;
            }
        }
        buffer.append('\t');
        dictionary = buffer.toString().getBytes("UTF-8");

        // Allocate the maximum buffer required when compressing
        outBuffer = new byte[2 * (maxLineLength + 1) * columns.size()];

        // Initialize rows and columns
        this.rows = new CompressDimension(MatrixDimensionKey.ROWS, rows.toArray(new String[rows.size()]));
        this.columns = new CompressDimension(MatrixDimensionKey.COLUMNS, columns.toArray(new String[columns.size()]));
    }

    public interface IMatrixReader {

        String[] readNext();

        void close();
    }

}
