package org.gitools.matrix.model.compressmatrix;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.zip.Deflater;

public abstract class AbstractCompressor
{
    protected static final char SEPARATOR = '\t';

    private CompressDimension rows;
    private CompressDimension columns;
    private String[] header;
    private byte[] dictionary;
    private byte[] outBuffer;
    private Deflater deflater = new Deflater();
    private long fileLinesCount;
    private long totalLineLength;

    public AbstractCompressor()
    {
    }

    protected CompressDimension getColumns()
    {
        return columns;
    }

    protected CompressDimension getRows()
    {
        return rows;
    }

    protected String[] getHeader()
    {
        return header;
    }

    protected byte[] getDictionary()
    {
        return dictionary;
    }

    protected long getAverageLineLength()
    {
        return totalLineLength / fileLinesCount;
    }

    protected long getTotalLines()
    {
        return fileLinesCount;
    }

    /**
     * Convert and array of Strings into a byte array
     *
     * @param values The strings
     * @return The byte arrays
     * @throws java.io.UnsupportedEncodingException
     */
    protected static byte[] stringToByteArray(String[] values) throws UnsupportedEncodingException
    {
        StringBuilder buffer = new StringBuilder(values.length * 10);
        for (String value : values)
        {
            buffer.append(value).append('\t');
        }
        return buffer.toString().getBytes("UTF-8");
    }

    /**
     * This class contains all the values of an uncompressed row
     */
    protected static class NotCompressRow
    {
        private List<String> values;
        private CompressDimension columns;

        /**
         * @param columns The columns dimension
         */
        public NotCompressRow(CompressDimension columns)
        {
            this.values = new ArrayList<String>(columns.size());
            this.columns = columns;
        }

        /**
         * Append a new column fields.
         *
         * @param columnFields the fields
         */
        public void append(String columnFields)
        {
            values.add(columnFields);
        }

        /**
         * Converts the content of the row into a sequence of [column position int],[values length int],[values byte buffer]
         *
         * @return
         * @throws java.io.IOException
         */
        public byte[] toByteArray() throws IOException
        {
            // Write the buffer
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(values.size() * (columns.size() - 2) * 8);
            DataOutputStream out = new DataOutputStream(bytes);
            for (String value : values)
            {
                // Column position
                int column = columns.getIndex(parseField(value, 0));
                out.writeInt(column);

                // Column values
                byte[] line = createColumnLine(value);
                out.writeInt(line.length);
                out.write(line);
            }
            out.close();
            bytes.close();
            return bytes.toByteArray();
        }

        /**
         * Returns a byte array with all the fields except the two first that are the row and the column.
         *
         * @param fields
         * @return
         * @throws UnsupportedEncodingException
         */
        private byte[] createColumnLine(String fields) throws UnsupportedEncodingException
        {
            int firstTab = fields.indexOf(SEPARATOR);
            int secondTab = fields.indexOf(SEPARATOR, firstTab + 1);
            return fields.substring(secondTab+1).getBytes("UTF-8");
        }
    }

    /**
     * Fast field split
     *
     * @param str The string to split using SEPARATOR
     * @param num The position to return
     * @return The string at 'num' position using 'SEPARATOR' in 'str' string.
     *
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

    protected CompressRow compressRow(NotCompressRow row) throws Exception
    {
        // Prepare a buffer with all the columns of this row
        byte[] input = row.toByteArray();

        // Compress the columns into 'outBuffer'
        int length = compressDeflater(input);

        byte[] content = Arrays.copyOf(outBuffer, length);
        CompressRow compressRow = new CompressRow(input.length, content);

        return compressRow;
    }


    /**
     * Compress an 'input' byte array into the 'outBuffer'
     *
     * @param input Array to compress
     * @return The length of the compressed buffer
     * @throws Exception
     */
    private int compressDeflater(byte[] input) throws Exception
    {
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
    protected void initialize(IMatrixReader reader) throws Exception
    {
        // Some internal variables
        Map<String, Integer> frequencies = new HashMap<String, Integer>();
        Set<String> rows = new HashSet<String>(1000);
        Set<String> columns = new HashSet<String>(1000);
        String[] fields;

        // Read the headers
        header = reader.readNext();

        int maxLineLength = 0;
        totalLineLength = 0;
        fileLinesCount = 0;
        while ((fields = reader.readNext()) != null)
        {
            fileLinesCount++;

            if (!columns.contains(fields[0]))
            {
                columns.add(fields[0]);
            }

            if (!rows.contains(fields[1]))
            {
                rows.add(fields[1]);
            }

            // Update frequencies
            int length = 0;
            for (String field : fields)
            {
                length += field.length() + 1;
                Integer freq = frequencies.get(field);
                freq = (freq == null) ? 1 : freq + 1;
                frequencies.put(field, freq);
            }
            totalLineLength += length;
            if (length > maxLineLength)
            {
                maxLineLength = length;
            }
        }

        // Sort the frequency table by frequency
        List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(frequencies.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>()
        {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
            {
                return (o1.getValue().compareTo(o2.getValue()));
            }
        });

        // Convert the frequency table into a deflate dictionary
        StringBuilder buffer = new StringBuilder();
        for (Map.Entry<String, Integer> entry : entries)
        {
            buffer.append(entry.getKey());
        }
        buffer.append('\t');
        dictionary = buffer.toString().getBytes("UTF-8");

        // Allocate the maximum buffer required when compressing
        outBuffer = new byte[2 * (maxLineLength + 1) * columns.size()];

        // Initialize rows and columns
        this.rows = new CompressDimension(rows.toArray(new String[0]));
        this.columns = new CompressDimension(columns.toArray(new String[0]));
    }

    public interface IMatrixReader {

        String[] readNext();
    }

}
