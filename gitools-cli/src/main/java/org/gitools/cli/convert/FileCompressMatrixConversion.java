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

import org.gitools.matrix.model.compressmatrix.AbstractCompressor;
import org.gitools.matrix.model.compressmatrix.CompressRow;
import org.gitools.utils.MemoryUtils;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;
import org.gitools.utils.tools.exception.ToolException;

import java.io.*;
import java.util.*;

/**
 * Converts a 'tdm' file into a compressed 'cmatrix' direct from the file.
 * This conversion doesn't need to full load the input matrix into the memory.
 */
public class FileCompressMatrixConversion extends AbstractCompressor {

    /**
     * Convert a 'tdm' matrix into a compressed 'cmatrix'.
     *
     * @param inputFile       the 'tdm' input file
     * @param outputFile      the 'cmatrix' output file
     * @param progressMonitor the progress monitor
     * @throws ToolException
     */
    public void convert(String inputFile, String outputFile, IProgressMonitor progressMonitor) throws ToolException {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(outputFile));

            // Build the compression dictionary calculating the
            // frequencies of all the words in the file
            progressMonitor.begin("Building dictionary...", 1);
            initialize(new FileMatrixReader(new CSVReader(new FileReader(inputFile))));

            progressMonitor.begin("Writing dictionary...", 1);
            out.writeInt(getDictionary().length);
            out.write(getDictionary());

            progressMonitor.begin("Writing columns...", 1);
            byte[] buffer = stringToByteArray(getColumns().getLabels());
            out.writeInt(buffer.length);
            out.write(buffer);

            progressMonitor.begin("Writing rows...", 1);
            buffer = stringToByteArray(getRows().getLabels());
            out.writeInt(buffer.length);
            out.write(buffer);

            progressMonitor.begin("Writing headers...", 1);
            buffer = stringToByteArray(getHeader());
            out.writeInt(buffer.length);
            out.write(buffer);

            // Try to estimate how many rows per round we can do
            // A round consist of load all the values in memory and
            // group them by row.
            System.gc();
            long estimatedMemoryUsage = (long) ((getAverageLineLength() * getColumns().size()) + 32);

            progressMonitor.debug("Average line length " + getAverageLineLength());
            progressMonitor.debug("Columns " + getColumns().size());

            long range = (long) (((double) MemoryUtils.getAvailableMemory() * 0.8) / (double) estimatedMemoryUsage);

            long count = 0;
            long from = 0;
            long to = (from + range > getRows().size()) ? getRows().size() : from + range;

            estimatedMemoryUsage = estimatedMemoryUsage * to;
            progressMonitor.debug("Estimated memory usage " + estimatedMemoryUsage);

            List<String> rowsList = Arrays.asList(getRows().getLabels());

            // Start the 'group by' row process
            while (from < rowsList.size()) {
                progressMonitor.begin("> Computing rows between " + from + " to " + to + " of " + getRows().size(), 1);

                Set<String> someRows = new HashSet<String>(rowsList.subList((int) from, (int) to));

                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                String line;

                // Prepare group buffers
                Map<String, NotCompressRow> groups = new HashMap<String, NotCompressRow>();
                System.gc();

                progressMonitor.debug("Free memory: " + MemoryUtils.getAvailableMemory());

                long memoryBefore = MemoryUtils.getAvailableMemory();

                progressMonitor.begin("Initializing buffers...", 1);
                for (String row : someRows) {
                    groups.put(row, new NotCompressRow(getColumns()));
                }

                // Skip the file header
                line = reader.readLine();

                progressMonitor.debug("Free memory: " + MemoryUtils.getAvailableMemory());

                progressMonitor.begin("Start group by row...", (int) (getTotalLines() / 2000));
                while ((line = reader.readLine()) != null) {
                    if ((count % 2000) == 0) {
                        progressMonitor.worked(1);
                    }
                    count++;

                    String row = parseField(line, 1);
                    if (someRows.contains(row)) {
                        NotCompressRow group = groups.get(row);
                        group.append(line);
                    }
                }
                reader.close();

                long memoryAfter = MemoryUtils.getAvailableMemory();

                progressMonitor.debug("Free memory: " + MemoryUtils.getAvailableMemory());

                progressMonitor.debug("Real memory usage: " + (memoryBefore - memoryAfter) + " (estimated " + estimatedMemoryUsage + " - over estimation " + 100 * ((double) (memoryBefore - memoryAfter - estimatedMemoryUsage) / (double) (-memoryBefore + memoryAfter)) + "%)");

                // Compress the row and write to disk
                progressMonitor.begin("Compressing...", groups.size());
                for (Map.Entry<String, NotCompressRow> group : groups.entrySet()) {
                    progressMonitor.worked(1);

                    // The row position
                    out.writeInt(getRows().getIndex(group.getKey()));

                    // Row to compress
                    NotCompressRow row = group.getValue();

                    // Compress the row
                    CompressRow compressRow = compressRow(row);

                    // Write the length of the buffer before compression
                    out.writeInt(compressRow.getNotCompressedLength());

                    // The length of the compressed buffer with the columns
                    out.writeInt(compressRow.getContent().length);

                    // The buffer with all the columns
                    out.write(compressRow.getContent());
                }

                // Calculate next range of rows to compute in the next round
                from = from + range;
                to = (from + range > getRows().size()) ? getRows().size() : from + range;

            }

            out.close();
            progressMonitor.end();

        } catch (Exception e) {
            throw new ToolException(e);
        }

    }


    public static class FileMatrixReader implements IMatrixReader {

        private CSVReader reader;

        public FileMatrixReader(CSVReader reader) {
            this.reader = reader;
        }

        @Override
        public String[] readNext() {
            try {
                return reader.readNext();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        public void close() {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * For test purposes
     *
     * @param args
     */
    public static void main(String[] args) {
        FileCompressMatrixConversion test = new FileCompressMatrixConversion();

        String inputFile = args[0];
        String outputFile = args[1];

        IProgressMonitor progressMonitor = new StreamProgressMonitor(System.out, true, true);

        try {
            test.convert(inputFile, outputFile, progressMonitor);
        } catch (ToolException e) {
            e.printStackTrace();
        }
    }

}
