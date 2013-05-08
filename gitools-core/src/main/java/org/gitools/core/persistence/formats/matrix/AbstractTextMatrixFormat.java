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

import org.gitools.core.datafilters.ValueTranslator;
import org.gitools.core.matrix.model.matrix.BaseMatrix;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

public abstract class AbstractTextMatrixFormat<R extends BaseMatrix> extends AbstractMatrixFormat<R> {

    private final int skipLines;
    private final Set<Integer> skipColumns;

    private R matrix;

    AbstractTextMatrixFormat(String extension, Class<R> resourceClass) {
        this(extension, resourceClass, 0);
    }

    AbstractTextMatrixFormat(String extension, Class<R> resourceClass, int skipLines, Integer... skipColumns) {
        super(extension, resourceClass);
        this.skipLines = skipLines;
        this.skipColumns = new HashSet<Integer>(Arrays.asList(skipColumns));
    }

    protected boolean isBinaryValues() {
        if (getProperties().containsKey(BINARY_VALUES)) {
            return (Boolean) getProperties().get(BINARY_VALUES);
        } else {
            return false;
        }
    }

    @NotNull
    protected abstract R createEntity();

    @Override
    protected void configureResource(@NotNull IResourceLocator resourceLocator, @NotNull Properties properties, @NotNull IProgressMonitor progressMonitor) throws PersistenceException {
        super.configureResource(resourceLocator, properties, progressMonitor);

        matrix = createEntity();

        readMetadata(resourceLocator, matrix, progressMonitor);
    }

    R read(@NotNull IResourceLocator resourceLocator, @NotNull ValueTranslator valueTranslator, @NotNull IProgressMonitor progressMonitor) throws PersistenceException {

        progressMonitor.begin("Loading matrix...", 1);

        readData(resourceLocator, matrix, valueTranslator, null, null, progressMonitor);

        progressMonitor.end();

        return matrix;
    }

    private void readMetadata(@NotNull IResourceLocator resourceLocator, @NotNull R matrix, @NotNull IProgressMonitor progressMonitor) throws PersistenceException {

        progressMonitor.begin("Reading names ...", 1);

        try {

            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            if (skipLines > 0) {
                for (int s = 0; s < skipLines; s++) {
                    parser.readNext();
                }
            }

            final String[] header = skipColumns(parser.readNext());
            if (header.length < 2) {
                throw new DataFormatException("At least 2 columns expected.");
            }

            // Read datafile name and column names

            matrix.setTitle(header[0]);

            int numColumns = header.length - 1;

            String[] columnNames = new String[numColumns];
            System.arraycopy(header, 1, columnNames, 0, numColumns);
            matrix.setColumns(columnNames);

            // Read row names

            String[] populationLabels = getPopulationLabels();
            final Set<String> popLabelsSet = populationLabels != null ? new HashSet<String>(Arrays.asList(populationLabels)) : null;

            String[] fields;

            final List<String> names = new ArrayList<String>();
            while ((fields = skipColumns(parser.readNext())) != null) {
                if (popLabelsSet == null || popLabelsSet.contains(fields[0])) {
                    names.add(fields[0]);
                }
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

        progressMonitor.info(matrix.getInternalColumns().cardinality() + " columns and " + matrix.getInternalRows().cardinality() + " rows");

        progressMonitor.end();
    }

    private void readData(@NotNull IResourceLocator resourceLocator, @NotNull R matrix, @NotNull ValueTranslator valueTranslator, @Nullable int[] columnsOrder, @Nullable int[] rowsOrder, @NotNull IProgressMonitor progressMonitor) throws PersistenceException {

        progressMonitor.begin("Reading data ...", 1);

        int numColumns = matrix.getInternalColumns().cardinality();
        int numRows = matrix.getInternalRows().cardinality();

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
        final Set<String> popLabelsSet = populationLabels != null ? new HashSet<String>(Arrays.asList(populationLabels)) : null;

        try {
            InputStream in = resourceLocator.openInputStream(progressMonitor);
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
                if (popLabelsSet != null && !popLabelsSet.contains(fields[0])) {
                    continue;
                }

                int rowidx = rowsOrder != null ? rowsOrder[row] : row;

                if (rowidx >= 0) {
                    rowNames[rowidx] = fields[0];

                    int col = 0;

                    while (col < numColumns && col < fields.length - 1) {

                        int colidx = columnsOrder != null ? columnsOrder[col] : col;

                        Double value = (Double) valueTranslator.stringToValue(fields[col + 1]);

                        matrix.setValue(rowidx, colidx, 0, value);
                        col++;
                    }

                    // fill the rest of the columns with NaNs
                    while (col < numColumns) {
                        int colidx = columnsOrder != null ? columnsOrder[col] : col;
                        matrix.setValue(rowidx, colidx, 0, Double.NaN);
                        col++;
                    }
                }
                row++;
            }

            // Fill the rest of population rows with background value

            Double backgroundValue = getBackgroundValue();
            while (row < numRows) {
                for (int col = 0; col < numColumns; col++)
                    matrix.setValue(row, col, 0, backgroundValue);

                row++;
            }

            in.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        progressMonitor.end();
    }

    @Nullable
    private String[] skipColumns(@Nullable String[] columns) {
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

    void write(@NotNull IResourceLocator resourceLocator, @NotNull R matrix, @NotNull ValueTranslator valueTranslator, @NotNull IProgressMonitor monitor) throws PersistenceException {

        monitor.begin("Saving matrix...", matrix.getColumns().size());

        try {
            OutputStream out = resourceLocator.openOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            int numCols = matrix.getColumns().size();

            final String[] colNames = matrix.getColumnStrings();

            pw.print(matrix.getTitle() != null ? matrix.getTitle() : "");

            for (int i = 0; i < numCols; i++) {
                final String name = i < colNames.length ? colNames[i] : "";
                pw.print('\t');
                pw.print(name);
            }

            pw.print('\n');

            int numRows = matrix.getRows().size();

            final String[] rowNames = matrix.getRowStrings();

            for (int i = 0; i < numRows; i++) {
                final String name = i < rowNames.length ? rowNames[i] : "";

                pw.print(name);

                for (int j = 0; j < numCols; j++) {
                    pw.print('\t');
                    pw.print(valueTranslator.valueToString(matrix.getValue(i, j, 0)));
                }

                pw.print('\n');

                monitor.worked(1);
            }
            pw.close();
            out.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        monitor.end();
    }

}
