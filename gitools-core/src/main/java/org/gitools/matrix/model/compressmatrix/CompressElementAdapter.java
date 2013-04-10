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
import com.google.common.cache.Weigher;
import org.gitools.datafilters.DoubleTranslator;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.formats.compressmatrix.CompressMatrixFormat;
import org.gitools.utils.MemoryUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * This element adapter contains all the values of a matrix and takes care
 * to expand the rows at request time.
 * <p/>
 * Keeps a dynamic cache with the expanded values. The size of the cache
 * will grow depending on the available free memory.
 */
public class CompressElementAdapter implements IElementAdapter
{
    private static final char SEPARATOR = '\t';
    private static DoubleTranslator TRANSLATOR = new DoubleTranslator();

    private final byte[] dictionary;
    private final Inflater decompresser = new Inflater();
    private final List<IElementAttribute> properties;
    private final Map<Integer, CompressRow> values;
    private final CompressDimension columns;
    private final LoadingCache<Integer, Map<Integer, String>> cache;


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
        this.properties = new ArrayList<IElementAttribute>(headers.length);
        for (int i = 2; i < headers.length; i++)
        {
            this.properties.add(new CompressElementAttribute(headers[i], double.class));
        }


        // A weigher that estimates the size of an uncompressed row
        Weigher<Integer, Map<Integer, String>> weigher = new Weigher<Integer, Map<Integer, String>>()
        {
            public int weigh(Integer row, Map<Integer, String> values)
            {
                // We use the expanded buffer size and some bytes per map key
                return CompressElementAdapter.this.values.get(row).getLength() + 8 * values.size();
            }
        };

        // Force a garbage collector now
        Runtime.getRuntime().gc();

        // Use a maximum of 80% of the available memory
        long maxMemory = 8 * MemoryUtils.getAvailableMemory() / 10;

        // Create the dynamic cache
        cache = CacheBuilder.newBuilder()
                .maximumWeight(maxMemory)
                .weigher(weigher)
                .build(
                        new CacheLoader<Integer, Map<Integer, String>>()
                        {
                            public Map<Integer, String> load(Integer row)
                            {
                                return uncompress(CompressElementAdapter.this.values.get(row));
                            }
                        });

    }

    /**
     * @param values A map of column to attributes values
     * @param column The target column
     * @param index  The target attribute identifier
     * @return Returns null if the row or the attribute don't exist, otherwise the double value.
     */
    private static Object toDouble(Map<Integer, String> values, int column, int index)
    {
        String columnValues = values.get(column);

        if (columnValues == null)
        {
            return null;
        }

        String value = parseField(columnValues, index);
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

    @Override
    public Class<?> getElementClass()
    {
        return int[].class;
    }

    @Override
    public int getPropertyCount()
    {
        return properties.size();
    }

    @Override
    public IElementAttribute getProperty(int index)
    {
        return properties.get(index);
    }

    @Override
    public List<IElementAttribute> getProperties()
    {
        return properties;
    }

    @Override
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

    @Override
    public Object getValue(Object element, int index)
    {
        int[] rowAndColumn = (int[]) element;

        int row = rowAndColumn[0];
        int column = rowAndColumn[1];

        // The cache is who loads the value if it's not already loaded.
        Map<Integer, String> rowValues = cache.getUnchecked(row);

        if (rowValues != null)
        {
            return toDouble(rowValues, column, index);
        }

        return null;
    }

    @Override
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
    private Map<Integer, String> uncompress(CompressRow compressRow)
    {

        Map<Integer, String> values = new HashMap<Integer, String>(columns.size() / 4);

        try
        {
            byte[] result = new byte[compressRow.getLength()];

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
                values.put(column, new String(CompressMatrixFormat.readBuffer(in), "UTF-8"));
            }
            in.close();

            return values;

        } catch (Exception e)
        {
            throw new PersistenceException(e);
        }
    }


}
