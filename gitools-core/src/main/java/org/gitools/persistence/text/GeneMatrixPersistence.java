/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.persistence.text;

import cern.colt.matrix.ObjectFactory1D;
import edu.upf.bg.csv.CSVReader;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.*;


public class GeneMatrixPersistence
        extends BaseMatrixPersistence<DoubleBinaryMatrix> {

    @Override
    public DoubleBinaryMatrix read(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Reading ...", 1);

        DoubleBinaryMatrix matrix = new DoubleBinaryMatrix();

        Reader reader = null;
        try {
            reader = resourceLocator.getReader();
        } catch (Exception e) {
            throw new PersistenceException("Error opening: " + resourceLocator.getURL(), e);
        }

        CSVReader parser = new CSVReader(reader);

        try {
            // read column names
            String[] columnNames = parser.readNext();

            // Discard descriptions
            parser.readNext();

            String[] fields;
            // read file
            Map<String, Integer> rowIndices = new HashMap<String, Integer>();

            List<Set<Integer>> indices = new ArrayList<Set<Integer>>(columnNames.length);
            for (int i = 0; i < columnNames.length; i++)
                indices.add(new HashSet<Integer>());

            while ((fields = parser.readNext()) != null) {

                if (fields.length > columnNames.length)
                    throw new PersistenceException("Row with more columns than expected at line " + parser.getLineNumber());

                for (int i = 0; i < fields.length; i++) {
                    String name = fields[i];
                    if (!name.isEmpty()) {
                        Integer rowIndex = rowIndices.get(name);
                        if (rowIndex == null) {
                            rowIndex = rowIndices.size();
                            rowIndices.put(name, rowIndex);
                        }
                        indices.get(i).add(rowIndex);
                    }
                }
            }

            // incorporate population labels

            String[] populationLabels = getPopulationLabels();
            if (populationLabels != null) {
                for (String name : populationLabels) {
                    Integer index = rowIndices.get(name);
                    if (index == null)
                        rowIndices.put(name, rowIndices.size());
                }
            }

            int numRows = rowIndices.size();

            matrix.makeCells(numRows, columnNames.length);

            // set row names

            matrix.setRows(ObjectFactory1D.dense.make(numRows));
            for (Map.Entry<String, Integer> entry : rowIndices.entrySet())
                matrix.setRow(entry.getValue(), entry.getKey());

            // set column names

            matrix.setColumns(columnNames);

            // fill matrix with background value

            double backgroundValue = getBackgroundValue();
            for (int row = 0; row < numRows; row++)
                for (int col = 0; col < columnNames.length; col++)
                    matrix.setCellValue(row, col, 0, backgroundValue);

            // set cell values

            for (int col = 0; col < columnNames.length; col++) {
                Set<Integer> colIndices = indices.get(col);
                for (Integer index : colIndices)
                    matrix.setCellValue(index, col, 0, 1.0);
            }

            reader.close();

            progressMonitor.info(matrix.getColumnCount() + " columns and " + matrix.getRowCount() + " rows");

            progressMonitor.end();
        } catch (IOException e) {
            throw new PersistenceException(e);
        }

        return matrix;
    }

    @Override
    public void write(IResourceLocator resourceLocator, DoubleBinaryMatrix matrix, IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Saving matrix...", matrix.getColumnCount());
        progressMonitor.info("To: " + resourceLocator.getURL());

        Writer writer;
        try {
            writer = resourceLocator.getWriter();
        } catch (Exception e) {
            throw new PersistenceException("Error opening: " + resourceLocator.getURL(), e);
        }

        PrintWriter pw = new PrintWriter(writer);

        int numColumns = matrix.getColumnCount();
        int numRows = matrix.getRowCount();

        // column labels
        pw.append(matrix.getColumnLabel(0));
        for (int col = 1; col < matrix.getColumnCount(); col++)
            pw.append('\t').append(matrix.getColumnLabel(col));
        pw.println();

        // descriptions
        for (int col = 1; col < matrix.getColumnCount(); col++)
            pw.append('\t');
        pw.println();

        // data
        StringBuilder line = new StringBuilder();

        int finishedColumns = 0;
        int[] positions = new int[numColumns];
        while (finishedColumns < numColumns) {
            boolean validLine = false;
            for (int col = 0; col < numColumns; col++) {
                if (col != 0)
                    line.append('\t');

                int row = positions[col];
                if (row < numRows) {
                    double value = MatrixUtils.doubleValue(matrix.getCellValue(row, col, 0));
                    while (value != 1.0 && (row < numRows - 1))
                        value = MatrixUtils.doubleValue(matrix.getCellValue(++row, col, 0));

                    if (value == 1.0) {
                        line.append(matrix.getRowLabel(row));
                        validLine = true;
                    }

                    positions[col] = row + 1;
                    if (positions[col] >= numRows)
                        finishedColumns++;
                }
            }

            if (validLine)
                pw.append(line).append('\n');

            line.setLength(0);
        }

        try {
            writer.close();
        } catch (Exception e) {
            throw new PersistenceException("Error closing: " + resourceLocator.getURL(), e);
        }

        progressMonitor.end();
    }


}
