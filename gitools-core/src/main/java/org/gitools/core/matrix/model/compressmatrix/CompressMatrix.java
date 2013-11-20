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
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.core.matrix.model.AbstractMatrix;
import org.gitools.core.matrix.model.MatrixLayer;
import org.gitools.core.matrix.model.MatrixLayers;
import org.gitools.utils.MemoryUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * The type Compress matrix.
 * <p/>
 * This format keep the rows compressed at memory, and has a dynamic cache that can expand or
 * contract depending on the user free memory.
 */
public class CompressMatrix extends AbstractMatrix<MatrixLayers, CompressDimension> {

    private final byte[] dictionary;
    private final Inflater decompresser = new Inflater();
    private final Map<Integer, CompressRow> values;
    private final LoadingCache<Integer, double[][]> rowsCache;

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
        super(createMatrixLayers(headers), rows, columns);

        this.dictionary = dictionary;
        this.values = values;

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
        cacheSize = (cacheSize > values.size() ? values.size() + (values.size() / 2) : cacheSize);
        cacheSize = (cacheSize < 40 ? 40 : cacheSize);

        // Create the rows cache
        rowsCache = CacheBuilder.newBuilder()
                .maximumSize(cacheSize)
                .build(
                        new CacheLoader<Integer, double[][]>() {
                            public double[][] load(Integer row) {
                                return uncompress(CompressMatrix.this.values.get(row));
                            }
                        });

        // Fill the cache in background
        final int max = Math.min(values.size(), cacheSize);
        Runnable fillCache = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < max; i++) {
                    rowsCache.getUnchecked(i);
                }
            }
        };
        (new Thread(fillCache, "LoadingCache")).start();

    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, String... identifiers) {

        int rowIndex = getRows().indexOf(identifiers[0]);
        int columnIndex = getColumns().indexOf(identifiers[1]);
        int layerIndex = getLayers().indexOf(layer.getId());

        // The cache is who loads the value if it's not already loaded.
        double[][] rowValues = rowsCache.getUnchecked(rowIndex);

        if (rowValues != null) {

            if (columnIndex != -1) {
                return (T) (Double) rowValues[columnIndex][layerIndex];
            }
        }

        return null;
    }

    @Override
    public <T> void set(IMatrixLayer<T> layer, T value, String... identifiers) {
        throw new UnsupportedOperationException("Read only matrix");
    }

    /**
     * Expands a compressed row
     *
     * @param compressRow The compressed row
     * @return A map from column to an array of strings with the values
     */
    private synchronized double[][] uncompress(CompressRow compressRow) {

        double[][] values = new double[getColumns().size()][getLayers().size()];

        // Initialize all to NaN
        for (int i = 0; i < getColumns().size(); i++) {
            for (int j = 0; j < getLayers().size(); j++) {
                values[i][j] = Double.NaN;
            }
        }

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
                for (int i = 0; i < getLayers().size(); i++) {
                    values[column][i] = in.readDouble();
                }
            }
            in.close();

            return values;

        } catch (Exception e) {
            throw new RuntimeException("Error extracting the matrix", e);
        }
    }

    public void detach() {
        this.rowsCache.invalidateAll();
    }

    public byte[] getDictionary() {
        return dictionary;
    }

    public Map<Integer, CompressRow> getCompressRows() {
        return values;
    }

    private static MatrixLayers createMatrixLayers(String[] headers) {

        // We assume that all the attributes are doubles.
        List<MatrixLayer> matrixLayers = new ArrayList<>(headers.length);
        for (String header : headers) {
            matrixLayers.add(new MatrixLayer(header, double.class));
        }
        return new MatrixLayers<>(matrixLayers);

    }


}
