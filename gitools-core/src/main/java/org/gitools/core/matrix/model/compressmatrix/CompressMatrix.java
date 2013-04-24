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


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.gitools.core.datafilters.DoubleTranslator;
import org.gitools.core.matrix.model.*;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.core.persistence.formats.compressmatrix.CompressedMatrixFormat;
import org.gitools.core.utils.MemoryUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

/**
 * The type Compress matrix.
 * <p/>
 * This format keep the rows compressed at memory, and has a dynamic cache that can expand or
 * contract depending on the user free memory.
 */
public class CompressMatrix extends AbstractMatrix {

    private static final char SEPARATOR = '\t';
    private static final int MINIMUM_AVAILABLE_MEMORY_THRESHOLD = (int) (3 * Runtime.getRuntime().maxMemory() / 10);
    private static DoubleTranslator TRANSLATOR = new DoubleTranslator();
    private final CompressDimension rows;
    private final CompressDimension columns;
    private final byte[] dictionary;
    private final Inflater decompresser = new Inflater();
    private final MatrixLayers layers;
    private final Map<Integer, CompressRow> values;
    private final LoadingCache<Integer, Map<Integer, Double[]>> rowsCache;

    /**
     * Instantiates a new Compress matrix.
     *
     * @param rows       the rows
     * @param columns    the columns
     * @param dictionary the compression dictionary common for all compressed values
     * @param headers    the identifiers of the attributes
     * @param values     the values a map with the row position as a key and a {@link CompressRow} with all the column values.
     */
    public CompressMatrix(CompressDimension rows, CompressDimension columns, byte[] dictionary, String[] headers, Map<Integer, CompressRow> values) {

        this.rows = rows;
        this.columns = columns;
        this.dictionary = dictionary;
        this.values = values;

        // We assume that all the attributes are doubles.
        this.layers = new MatrixLayers(double.class, headers);

        // Force a garbage collector now
        Runtime.getRuntime().gc();

        // Use a maximum of 50% of the available memory
        long availableMemory = MemoryUtils.getAvailableMemory() / 2;

        // Estimate uncompress matrix size
        int matrixSize = 0;
        for (CompressRow value : values.values()) {
            matrixSize = matrixSize + value.getNotCompressedLength() + 4;
        }

        // Calculate rows cache size
        double fact = (double) availableMemory / (double) matrixSize;
        int cacheSize = (int) ((double) values.size() * fact);
        cacheSize = (cacheSize > values.size() ? values.size() : cacheSize);
        cacheSize = (cacheSize < 40 ? 40 : cacheSize);

        // Create the rows cache
        rowsCache = CacheBuilder.newBuilder()
                .maximumSize(cacheSize)
                .build(
                        new CacheLoader<Integer, Map<Integer, Double[]>>() {
                            public Map<Integer, Double[]> load(Integer row) {
                                return uncompress(CompressMatrix.this.values.get(row));
                            }
                        });

        // Create a timer that watches every 5 seconds the available memory
        // and evict all the cache if it is below a minimum threshold.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (MemoryUtils.getAvailableMemory() < MINIMUM_AVAILABLE_MEMORY_THRESHOLD) {
                    System.out.println("WARNING: Memory too low, cleaning cache.");
                    rowsCache.invalidateAll();
                    System.gc();
                }
            }
        }, 5000, 5000);

    }

    /**
     * @param values A string with all values
     * @param index  The target attribute identifier
     * @return Double value
     */
    private static Double toDouble(String values, int index) {

        if (values == null) {
            return null;
        }

        String value = parseField(values, index);
        return TRANSLATOR.stringToValue(value);
    }

    /**
     * Fast field split
     *
     * @param str The string to split using SEPARATOR
     * @param num The position to return
     * @return The string at 'num' position using 'SEPARATOR' in 'str' string.
     */
    private static String parseField(String str, int num) {
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

    @Override
    public IMatrixDimension getColumns() {
        return columns;
    }

    @Override
    public IMatrixDimension getRows() {
        return rows;
    }

    @Override
    public Object getValue(int[] position, int layer) {

        // The cache is who loads the value if it's not already loaded.
        Map<Integer, Double[]> rowValues = rowsCache.getUnchecked(rows.getPosition(position));

        if (rowValues != null) {
            Double[] columnValues = rowValues.get(columns.getPosition(position));

            if (columnValues != null) {
                return columnValues[layer];
            }
        }

        return null;
    }

    @Override
    public void setValue(int[] position, int layer, Object value) {
        throw new UnsupportedOperationException("Read only matrix");
    }

    @Override
    public IMatrixLayers getLayers() {
        return layers;
    }

    /**
     * Expands a compressed row
     *
     * @param compressRow The compressed row
     * @return A map from column to an array of strings with the values
     */
    private synchronized Map<Integer, Double[]> uncompress(CompressRow compressRow) {

        Map<Integer, Double[]> values = new HashMap<Integer, Double[]>(columns.size() / 4);

        try {
            byte[] result = new byte[compressRow.getNotCompressedLength()];

            // Expand the row
            decompresser.setInput(compressRow.getContent());
            decompresser.inflate(result);
            decompresser.setDictionary(dictionary);
            int resultLength = decompresser.inflate(result);
            decompresser.reset();

            // Read all the columns
            // [column position int],[values length int],[values byte buffer]
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(result));
            while (in.available() > 0) {
                int column = in.readInt();

                String line = new String(CompressedMatrixFormat.readBuffer(in), "UTF-8");
                Double properties[] = new Double[layers.size()];
                for (int i = 0; i < properties.length; i++) {
                    properties[i] = toDouble(line, i);
                }

                values.put(column, properties);
            }
            in.close();

            return values;

        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    public void detach() {
        this.rowsCache.invalidateAll();
        System.gc();
    }

}
