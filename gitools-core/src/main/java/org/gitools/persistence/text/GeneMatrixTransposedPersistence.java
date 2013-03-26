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

public class GeneMatrixTransposedPersistence extends BaseMatrixPersistence<DoubleBinaryMatrix> {

    @Override
    public DoubleBinaryMatrix read(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

        progressMonitor.begin("Reading names ...", 1);

        DoubleBinaryMatrix matrix = new DoubleBinaryMatrix();

        Reader reader = null;
        try {
            reader = resourceLocator.getReader();
        } catch (Exception e) {
            throw new PersistenceException("Error opening: " + resourceLocator.getURL(), e);
        }

        CSVReader parser = new CSVReader(reader);

        try {
            // read file

            String[] fields;

            List<String> colNames = new ArrayList<String>();
            List<int[]> colRowIndices = new ArrayList<int[]>();

            Map<String, Integer> rowIndices = new HashMap<String, Integer>();

            while ((fields = parser.readNext()) != null) {

                if (fields.length < 2)
                    throw new PersistenceException("Invalid row, at least 2 columns required (name and description) at line " + parser.getLineNumber());

                colNames.add(fields[0]);

                // fields[1] is for description, but currently not used

                int[] rows = new int[fields.length - 2];
                colRowIndices.add(rows);

                for (int i = 2; i < fields.length; i++) {
                    Integer rowIndex = rowIndices.get(fields[i]);
                    if (rowIndex == null) {
                        rowIndex = rowIndices.size();
                        rowIndices.put(fields[i], rowIndex);
                    }

                    rows[i - 2] = rowIndex;
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
            int numColumns = colNames.size();

            matrix.makeCells(numRows, numColumns);

            // set row names

            for (Map.Entry<String, Integer> entry : rowIndices.entrySet())
                matrix.setRow(entry.getValue(), entry.getKey());

            // fill matrix with background value

            double backgroundValue = getBackgroundValue();
            for (int row = 0; row < numRows; row++)
                for (int col = 0; col < numColumns; col++)
                    matrix.setCellValue(row, col, 0, backgroundValue);

            // set column names and cell values

            int colIndex = 0;
            Iterator<String> colNameIt = colNames.iterator();
            Iterator<int[]> colRowsIt = colRowIndices.iterator();

            while (colNameIt.hasNext()) {
                matrix.setColumn(colIndex, colNameIt.next());
                int[] rows = colRowsIt.next();
                for (int row : rows)
                    matrix.setCellValue(row, colIndex, 0, 1.0);
                colIndex++;
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
            throw new PersistenceException("Error writing: " + resourceLocator.getURL(), e);
        }

        PrintWriter pw = new PrintWriter(writer);

        final int rowCount = matrix.getRowCount();

        for (int ci = 0; ci < matrix.getColumnCount(); ci++) {
            pw.append(matrix.getColumnLabel(ci));
            pw.append('\t'); // description, but currently not used
            for (int ri = 0; ri < rowCount; ri++) {
                Double value = MatrixUtils.doubleValue(matrix.getCellValue(ri, ci, 0));
                if (value == 1.0)
                    pw.append('\t').append(matrix.getRowLabel(ri));
            }
            pw.println();
        }

        try {
            writer.close();
        } catch (Exception e) {
            throw new PersistenceException("Error closing: " + resourceLocator.getURL(), e);
        }

        progressMonitor.end();
    }
}
