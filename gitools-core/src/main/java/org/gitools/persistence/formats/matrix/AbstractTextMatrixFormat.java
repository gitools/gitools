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

package org.gitools.persistence.formats.matrix;

import edu.upf.bg.csv.CSVReader;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;

import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

public abstract class AbstractTextMatrixFormat<R extends BaseMatrix> extends AbstractMatrixFormat<R> {

    private int skipLines;
    private Set<Integer> skipColumns;

    private R matrix;

    public AbstractTextMatrixFormat(String extension, String mime, Class<R> resourceClass) {
        this(extension, mime, resourceClass, 0);
    }

    public AbstractTextMatrixFormat(String extension, String mime, Class<R> resourceClass, int skipLines, Integer... skipColumns) {
        super(extension, mime, resourceClass);
        this.skipLines = skipLines;
        this.skipColumns = new HashSet<Integer>(Arrays.asList(skipColumns));
    }

    protected boolean isBinaryValues() {
        if (getProperties().containsKey(BINARY_VALUES))
            return (Boolean) getProperties().get(BINARY_VALUES);
        else
            return false;
    }

    protected abstract R createEntity();

    @Override
    protected void configureResource(IResourceLocator resourceLocator, Properties properties, IProgressMonitor progressMonitor) throws PersistenceException {
        super.configureResource(resourceLocator, properties, progressMonitor);

        matrix = createEntity();

        readMetadata(resourceLocator, matrix, progressMonitor);
    }

    protected R read(IResourceLocator resourceLocator, ValueTranslator valueTranslator, IProgressMonitor progressMonitor) throws PersistenceException {

        progressMonitor.begin("Loading matrix...", 1);

        readData(resourceLocator, matrix, valueTranslator, null, null, progressMonitor);

        progressMonitor.end();

        return matrix;
    }

    private void readMetadata(IResourceLocator resourceLocator, R matrix, IProgressMonitor progressMonitor) throws PersistenceException {

        progressMonitor.begin("Reading names ...", 1);

        try {

            InputStream in = resourceLocator.openInputStream();
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            if (skipLines > 0) {
                for (int s = 0; s < skipLines; s++) {
                    parser.readNext();
                }
            }

            final String[] header = skipColumns(parser.readNext());
            if (header.length < 2)
                throw new DataFormatException("At least 2 columns expected.");

            // Read datafile name and column names

            matrix.setTitle(header[0]);

            int numColumns = header.length - 1;

            String[] columnNames = new String[numColumns];
            System.arraycopy(header, 1, columnNames, 0, numColumns);
            matrix.setColumns(columnNames);

            // Read row names

            String[] populationLabels = getPopulationLabels();
            final Set<String> popLabelsSet = populationLabels != null ?
                    new HashSet<String>(Arrays.asList(populationLabels)) : null;

            String[] fields;

            final List<String> names = new ArrayList<String>();
            while ((fields = skipColumns(parser.readNext())) != null) {
                if (popLabelsSet == null || popLabelsSet.contains(fields[0]))
                    names.add(fields[0]);
            }

            // Incorporate background names

            if (populationLabels != null) {
                final Set<String> nameSet = new HashSet<String>(names);
                for (String name : populationLabels) {
                    if (!nameSet.contains(name)) {
                        names.add(name);
                        nameSet.add(name);
                    }
                }
            }

            String[] rowNames = names.toArray(new String[names.size()]);
            matrix.setRows(rowNames);

            in.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        progressMonitor.info(matrix.getColumns().cardinality() + " columns and "
                + matrix.getRows().cardinality() + " rows");

        progressMonitor.end();
    }

    private void readData(IResourceLocator resourceLocator, R matrix, ValueTranslator valueTranslator, int[] columnsOrder, int[] rowsOrder, IProgressMonitor progressMonitor) throws PersistenceException {

        progressMonitor.begin("Reading data ...", 1);

        int numColumns = matrix.getColumns().cardinality();
        int numRows = matrix.getRows().cardinality();

        String[] columnNames = matrix.getColumnStrings();
        String[] rowNames = matrix.getRowStrings();

        // Sort column names ordered by columnsOrder

        final String[] finalColumnNames = new String[numColumns];
        for (int i = 0; i < numColumns; i++) {
            int colidx = columnsOrder != null ? columnsOrder[i] : i;
            finalColumnNames[colidx] = columnNames[i];
        }
        columnNames = finalColumnNames;

        // Read rows names and data ordered by rowsOrder

        String[] populationLabels = getPopulationLabels();
        final Set<String> popLabelsSet = populationLabels != null ?
                new HashSet<String>(Arrays.asList(populationLabels)) : null;

        try {
            InputStream in = resourceLocator.openInputStream();
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            if (skipLines > 0) {
                for (int s = 0; s < skipLines; s++) {
                    parser.readNext();
                }
            }

            parser.readNext(); // discard header

            matrix.makeCells(numRows, numColumns);

            String[] fields;
            int row = 0;

            while ((fields = skipColumns(parser.readNext())) != null) {
                if (popLabelsSet != null && !popLabelsSet.contains(fields[0]))
                    continue;

                int rowidx = rowsOrder != null ? rowsOrder[row] : row;

                if (rowidx >= 0) {
                    rowNames[rowidx] = fields[0];

                    int col = 0;

                    while (col < numColumns && col < fields.length - 1) {

                        int colidx = columnsOrder != null ? columnsOrder[col] : col;

                        Double value = (Double) valueTranslator.stringToValue(fields[col + 1]);

                        matrix.setCellValue(rowidx, colidx, 0, value);
                        col++;
                    }

                    // fill the rest of the columns with NaNs
                    while (col < numColumns) {
                        int colidx = columnsOrder != null ? columnsOrder[col] : col;
                        matrix.setCellValue(rowidx, colidx, 0, Double.NaN);
                        col++;
                    }
                }
                row++;
            }

            // Fill the rest of population rows with background value

            Double backgroundValue = getBackgroundValue();
            while (row < numRows) {
                for (int col = 0; col < numColumns; col++)
                    matrix.setCellValue(row, col, 0, backgroundValue);

                row++;
            }

            in.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        progressMonitor.end();
    }

    private String[] skipColumns(String[] columns) {
        if (skipColumns.isEmpty()) {
            return columns;
        }

        if (columns == null || columns.length == 0) {
            return columns;
        }

        String[] result = new String[columns.length - skipColumns.size()];
        int r = 0;
        for (int i = 0; i < columns.length; i++) {
            if (!skipColumns.contains(Integer.valueOf(i))) {
                result[r] = columns[i];
                r++;

                if (r == result.length) {
                    break;
                }
            }
        }

        return result;
    }

    protected void write(IResourceLocator resourceLocator, R matrix, ValueTranslator valueTranslator, IProgressMonitor monitor) throws PersistenceException {

        monitor.begin("Saving matrix...", matrix.getColumnCount());

        try {
            OutputStream out = resourceLocator.openOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            int numCols = matrix.getColumnCount();

            final String[] colNames = matrix.getColumnStrings();

            pw.print(matrix.getTitle() != null ? matrix.getTitle() : "");

            for (int i = 0; i < numCols; i++) {
                final String name = i < colNames.length ? colNames[i] : "";
                pw.print('\t');
                pw.print(name);
            }

            pw.print('\n');

            int numRows = matrix.getRowCount();

            final String[] rowNames = matrix.getRowStrings();

            for (int i = 0; i < numRows; i++) {
                final String name = i < rowNames.length ? rowNames[i] : "";

                pw.print(name);

                for (int j = 0; j < numCols; j++) {
                    pw.print('\t');
                    pw.print(valueTranslator.valueToString(
                            matrix.getCellValue(i, j, 0)));
                }

                pw.print('\n');

                monitor.worked(1);
            }

            out.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        monitor.end();
    }

}
