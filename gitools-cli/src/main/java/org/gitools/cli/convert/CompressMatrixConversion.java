/*
 * #%L
 * gitools-cli
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
package org.gitools.cli.convert;

import org.gitools.matrix.model.compressmatrix.CompressDimension;
import org.gitools.utils.MemoryUtils;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;
import org.gitools.utils.tools.exception.ToolException;

import java.io.*;
import java.util.*;
import java.util.zip.Deflater;

/**
 * Converts a 'tdm' file into a compressed 'ctdm'
 */
public class CompressMatrixConversion
{

    private CompressDimension rows;
    private CompressDimension columns;
    private byte[] header;
    private byte[] dictionary;
    private byte[] outBuffer;
    private Deflater deflater = new Deflater();
    private long fileLinesCount;
    private long totalLineLength;


    /**
     * Convert a 'tdm' matrix into a compressed 'ctdm'.
     *
     * @param inputFile the 'tdm' input file
     * @param outputFile the 'ctdm' output file
     * @param progressMonitor the progress monitor
     * @throws ToolException
     */
    public void convert(String inputFile, String outputFile, IProgressMonitor progressMonitor) throws ToolException
    {
        try
        {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(outputFile));

            // Build the compression dictionary calculating the
            // frequencies of all the words in the file
            progressMonitor.begin("Building dictionary...", 1);
            buildDictionaryRowsAndCols(inputFile);


            progressMonitor.begin("Writing dictionary...", 1);
            out.writeInt(dictionary.length);
            out.write(dictionary);

            progressMonitor.begin("Writing columns...", 1);
            byte[] buffer = stringToByteArray(columns.getLabels());
            out.writeInt(buffer.length);
            out.write(buffer);

            progressMonitor.begin("Writing rows...", 1);
            buffer = stringToByteArray(rows.getLabels());
            out.writeInt(buffer.length);
            out.write(buffer);
            buffer = null;

            progressMonitor.begin("Writing headers...", 1);
            out.writeInt(header.length);
            out.write(header);
            header = null;

            // Try to estimate how many rows per round we can do
            // A round consist of load all the values in memory and
            // group them by row.
            System.gc();
            long range = (MemoryUtils.getAvailableMemory() / ((long) ((totalLineLength/fileLinesCount) * columns.size()) + 32));
            long count = 0;
            long from = 0;
            long to = (from + range > rows.size()) ? rows.size() : from + range;
            Map<String, NotCompressRow> groups = new HashMap<String, NotCompressRow>();
            List<String> rowsList = Arrays.asList(rows.getLabels());

            // Start the 'group by' row process
            while (from < rowsList.size())
            {
                progressMonitor.begin("> Computing rows between " + from + " to " + to + " of " + rows.size(), 1);

                Set<String> someRows = new HashSet<String>(rowsList.subList((int)from, (int)to));

                CSVReader reader = new CSVReader(new FileReader(inputFile));
                String[] line;

                // Prepare group buffers
                groups.clear();
                System.gc();

                progressMonitor.debug("Free memory: " + MemoryUtils.getAvailableMemory());

                progressMonitor.begin("Initializing buffers...", 1);
                for (String row : someRows)
                {
                    groups.put(row, new NotCompressRow(columns));
                }

                // Skip the file header
                line = reader.readNext();

                progressMonitor.begin("Start group by row...", (int) (fileLinesCount / 2000));
                while ((line = reader.readNext()) != null)
                {
                    if ((count % 2000) == 0)
                    {
                        progressMonitor.worked(1);
                    }
                    count++;

                    if (someRows.contains(line[1]))
                    {
                        NotCompressRow group = groups.get(line[1]);
                        group.append(line);
                    }
                }
                reader.close();

                progressMonitor.debug("Free memory: " + MemoryUtils.getAvailableMemory());

                // Compress the row and write to disk
                progressMonitor.begin("Compressing...", groups.size());
                for (Map.Entry<String, NotCompressRow> group : groups.entrySet())
                {
                    progressMonitor.worked(1);

                    // The row position
                    out.writeInt(rows.getIndex(group.getKey()));

                    // Prepare a buffer with all the columns of this row
                    NotCompressRow row = group.getValue();
                    byte[] input = row.toByteArray();

                    // Compress the columns into 'outBuffer'
                    int length = compressDeflater(input);

                    // Write the length of the buffer before compression
                    out.writeInt(input.length);

                    // The length of the compressed buffer with the columns
                    out.writeInt(length);

                    // The buffer with all the columns
                    out.write(outBuffer, 0, length);
                }

                // Calculate next range of rows to compute in the next round
                from = from + range;
                to = (from + range > rows.size()) ? rows.size() : from + range;

            }

            out.close();
            progressMonitor.end();

        } catch (Exception e)
        {
            throw new ToolException(e);
        }

    }

    /**
     * Convert and array of Strings into a byte array
     *
     * @param values The strings
     * @return The byte arrays
     * @throws UnsupportedEncodingException
     */
    private byte[] stringToByteArray(String[] values) throws UnsupportedEncodingException
    {
        StringBuilder buffer = new StringBuilder(values.length * 10);
        for (String value : values)
        {
            buffer.append(value).append('\t');
        }
        return buffer.toString().getBytes("UTF-8");
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
     * @param file Input file in 'tdm' format.
     * @throws Exception
     */
    private void buildDictionaryRowsAndCols(String file) throws Exception
    {
        // Some internal variables
        CSVReader parser = new CSVReader(new FileReader(file));
        Map<String, Integer> frequencies = new HashMap<String, Integer>();
        Set<String> rows = new HashSet<String>(1000);
        Set<String> columns = new HashSet<String>(1000);
        String[] fields;

        // Read the headers
        fields = parser.readNext();
        StringBuilder headBuffer = new StringBuilder(fields.length * 10);
        for (String header : fields)
        {
            headBuffer.append(header).append('\t');
        }

        // Initialize the global array headers
        header = headBuffer.toString().getBytes("UTF-8");

        int maxLineLength = 0;
        totalLineLength = 0;
        fileLinesCount = 0;
        while ((fields = parser.readNext()) != null)
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

    /**
     * This class contains all the values of an uncompressed row
     */
    private static class NotCompressRow
    {
        private List<String[]> values;
        private CompressDimension columns;

        /**
         * @param columns The columns dimension
         */
        public NotCompressRow(CompressDimension columns)
        {
            this.values = new ArrayList<String[]>(columns.size());
            this.columns = columns;
        }

        /**
         * Append a new column fields.
         *
         * @param columnFields the fields
         */
        public void append(String[] columnFields)
        {
            values.add(columnFields);
        }

        /**
         * Converts the content of the row into a sequence of [column position int],[values length int],[values byte buffer]
         *
         * @return
         * @throws IOException
         */
        public byte[] toByteArray() throws IOException
        {
            // Write the buffer
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(values.size() * (columns.size() - 2) * 8);
            DataOutputStream out = new DataOutputStream(bytes);
            for (String[] value : values)
            {
                // Column position
                int column = columns.getIndex(value[0]);
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
        private byte[] createColumnLine(String[] fields) throws UnsupportedEncodingException
        {
            StringBuilder union = new StringBuilder((fields.length-2) * 8);
            for (int i=2; i < fields.length; i++)
            {
                union.append(fields[i]).append('\t');
            }

            return union.toString().getBytes("UTF-8");
        }
    }

    public static void main(String[] args)
    {
        CompressMatrixConversion test = new CompressMatrixConversion();

        String inputFile = args[0];
        String outputFile = args[1];

        IProgressMonitor progressMonitor = new StreamProgressMonitor(System.out, true, true);

        try
        {
            test.convert(inputFile, outputFile, progressMonitor);
        } catch (ToolException e)
        {
            e.printStackTrace();
        }
    }

}
