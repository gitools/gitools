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
package org.gitools.core.persistence.formats.matrix;

import cern.colt.matrix.ObjectFactory1D;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.core.matrix.model.matrix.DoubleBinaryMatrix;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.core.persistence.formats.FileSuffixes;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class GeneMatrixFormat extends AbstractMatrixFormat<DoubleBinaryMatrix> {

    public GeneMatrixFormat() {
        super(FileSuffixes.GENE_MATRIX, DoubleBinaryMatrix.class);
    }

    @NotNull
    @Override
    protected DoubleBinaryMatrix readResource(@NotNull IResourceLocator resourceLocator, @NotNull IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Reading ...", 1);

        DoubleBinaryMatrix matrix = new DoubleBinaryMatrix();

        try {

            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

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

                if (fields.length > columnNames.length) {
                    throw new PersistenceException("Row with more columns than expected at line " + parser.getLineNumber());
                }

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
                    if (index == null) {
                        rowIndices.put(name, rowIndices.size());
                    }
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
                    matrix.setValue(row, col, 0, backgroundValue);

            // set cell values

            for (int col = 0; col < columnNames.length; col++) {
                Set<Integer> colIndices = indices.get(col);
                for (Integer index : colIndices)
                    matrix.setValue(index, col, 0, 1.0);
            }

            in.close();

            progressMonitor.info(matrix.getColumns().size() + " columns and " + matrix.getRows().size() + " rows");

            progressMonitor.end();
        } catch (IOException e) {
            throw new PersistenceException(e);
        }

        return matrix;
    }


    @Override
    protected void writeResource(@NotNull IResourceLocator resourceLocator, @NotNull DoubleBinaryMatrix matrix, @NotNull IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Saving matrix...", matrix.getColumns().size());

        try {
            OutputStream out = resourceLocator.openOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            int numColumns = matrix.getColumns().size();
            int numRows = matrix.getRows().size();

            // column labels
            pw.append(matrix.getLabel(0));
            for (int col = 1; col < matrix.getColumns().size(); col++)
                pw.append('\t').append(matrix.getLabel(col));
            pw.println();

            // descriptions
            for (int col = 1; col < matrix.getColumns().size(); col++)
                pw.append('\t');
            pw.println();

            // data
            StringBuilder line = new StringBuilder();

            int finishedColumns = 0;
            int[] positions = new int[numColumns];
            while (finishedColumns < numColumns) {
                boolean validLine = false;
                for (int col = 0; col < numColumns; col++) {
                    if (col != 0) {
                        line.append('\t');
                    }

                    int row = positions[col];
                    if (row < numRows) {
                        double value = MatrixUtils.doubleValue(matrix.getValue(row, col, 0));
                        while (value != 1.0 && (row < numRows - 1))
                            value = MatrixUtils.doubleValue(matrix.getValue(++row, col, 0));

                        if (value == 1.0) {
                            line.append(matrix.internalRowLabel(row));
                            validLine = true;
                        }

                        positions[col] = row + 1;
                        if (positions[col] >= numRows) {
                            finishedColumns++;
                        }
                    }
                }

                if (validLine) {
                    pw.append(line).append('\n');
                }

                line.setLength(0);
            }

            out.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        progressMonitor.end();
    }


}
