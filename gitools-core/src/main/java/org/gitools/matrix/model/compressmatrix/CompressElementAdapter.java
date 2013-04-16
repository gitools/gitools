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
package org.gitools.matrix.model.compressmatrix;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.gitools.datafilters.DoubleTranslator;
import org.gitools.matrix.model.IMatrixLayer;
import org.gitools.matrix.model.IMatrixLayers;
import org.gitools.matrix.model.SimpleMatrixLayers;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.formats.compressmatrix.CompressMatrixFormat;
import org.gitools.utils.MemoryUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

/**
 * This element adapter contains all the values of a matrix and takes care
 * to expand the rows at request time.
 * <p/>
 * Keeps a dynamic cache with the expanded values. The size of the cache
 * will grow depending on the available free memory.
 */
public class CompressElementAdapter
{
    private static final char SEPARATOR = '\t';
    public static final int MINIMUM_AVAILABLE_MEMORY_THRESHOLD = (int) (3 * Runtime.getRuntime().maxMemory() / 10);
    private static DoubleTranslator TRANSLATOR = new DoubleTranslator();

    private final byte[] dictionary;
    private final Inflater decompresser = new Inflater();
    private final SimpleMatrixLayers properties;
    private final Map<Integer, CompressRow> values;
    private final CompressDimension columns;

    private final LoadingCache<Integer, Map<Integer, Double[]>> rowsCache;


    /**
     * Instantiates a new Compress element adapter.
     *
     * @param dictionary the compression dictionary common for all compressed values
     * @param headers    the identifiers of the attributes
     * @param values     the values a map with the row position as a key and a {@link CompressRow} with all the column values.
     * @param columns    the column dimension of the matrix.
     */
    public CompressElementAdapter(byte[] dictionary, String[] headers, Map<Integer, CompressRow> values, CompressDimension columns)
    {
        this.dictionary = dictionary;
        this.values = values;
        this.columns = columns;

        // We assume that all the attributes are doubles.
        this.properties = new SimpleMatrixLayers(double.class, headers);

        // Force a garbage collector now
        Runtime.getRuntime().gc();

        // Use a maximum of 50% of the available memory
        long availableMemory = MemoryUtils.getAvailableMemory() / 2;

        // Estimate uncompress matrix size
        int matrixSize = 0;
        for (CompressRow value : values.values())
        {
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
                        new CacheLoader<Integer, Map<Integer, Double[]>>()
                        {
                            public Map<Integer, Double[]> load(Integer row)
                            {
                                return uncompress(CompressElementAdapter.this.values.get(row));
                            }
                        });

        // Create a timer that watches every 5 seconds the available memory
        // and evict all the cache if it is below a minimum threshold.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate( new TimerTask()
        {
            @Override
            public void run()
            {
                if (MemoryUtils.getAvailableMemory() < MINIMUM_AVAILABLE_MEMORY_THRESHOLD)
                {
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
    private Double toDouble(String values, int index)
    {

        if (values == null)
        {
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
     *
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

    public Class<?> getElementClass()
    {
        return int[].class;
    }

    public int getPropertyCount()
    {
        return properties.size();
    }

    public IMatrixLayer getProperty(int index)
    {
        return properties.get(index);
    }

    public IMatrixLayers getProperties()
    {
        return properties;
    }

    public int getPropertyIndex(String label)
    {
        for (int i = 0; i < properties.size(); i++)
        {
            if (properties.get(i).getId().equals(label))
            {
                return i;
            }
        }

        return -1;
    }

    public Object getValue(Object element, int index)
    {
        int[] rowAndColumn = (int[]) element;

        int row = rowAndColumn[0];
        int column = rowAndColumn[1];

        // The cache is who loads the value if it's not already loaded.
        Map<Integer, Double[]> rowValues = rowsCache.getUnchecked(row);

        if (rowValues != null)
        {
            Double[] columnValues = rowValues.get(column);

            if (columnValues != null)
            {
                return columnValues[index];
            }
        }

        return null;
    }

    public boolean isEmpty(int row, int column)
    {
        return (getValue(new int[] {row, column}, 0) == null);
    }

    public void setValue(Object element, int index, Object value)
    {
        throw new UnsupportedOperationException("Read only matrix");
    }

    /**
     * Expands a compressed row
     *
     * @param compressRow The compressed row
     * @return A map from column to an array of strings with the values
     */
    private synchronized Map<Integer, Double[]> uncompress(CompressRow compressRow)
    {

        Map<Integer, Double[]> values = new HashMap<Integer, Double[]>(columns.size() / 4);

        try
        {
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
            while (in.available() > 0)
            {
                int column = in.readInt();

                String line = new String(CompressMatrixFormat.readBuffer(in), "UTF-8");
                Double properties[] = new Double[getPropertyCount()];
                for (int i=0; i < getPropertyCount(); i++)
                {
                    properties[i] = toDouble(line, i);
                }

                values.put(column, properties);
            }
            in.close();

            return values;

        } catch (Exception e)
        {
            throw new PersistenceException(e);
        }
    }


    public void detach()
    {
        this.rowsCache.invalidateAll();
        System.gc();
    }
}
