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

import org.gitools.core.datafilters.DoubleTranslator;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.hashmatrix.HashMatrix;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.gitools.core.matrix.model.MatrixDimensionKey.COLUMNS;
import static org.gitools.core.matrix.model.MatrixDimensionKey.ROWS;


public abstract class AbstractCdmMatrixFormat extends AbstractMatrixFormat {

    private final int skipLines;
    private final Set<Integer> skipColumns;

    protected AbstractCdmMatrixFormat(String extension, int skipLines, Integer... skipColumns) {
        super(extension);

        this.skipLines = skipLines;
        this.skipColumns = new HashSet<>(Arrays.asList(skipColumns));
    }

    @Override
    protected IMatrix readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

        progressMonitor.begin("Reading data ...", 1);

        HashMatrix matrix = new HashMatrix(ROWS, COLUMNS);

        try {
            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            if (skipLines > 0) {
                for (int s = 0; s < skipLines; s++) {
                    parser.readNext();
                }
            }

            // Read columns
            String columns[] = skipColumns(parser.readNext());

            String[] fields;
            while ((fields = skipColumns(parser.readNext())) != null) {
                checkLine(fields, columns, parser.getLineNumber());

                String rowId = fields[0];
                for (int i=1; i < fields.length; i++) {

                    String columnId = columns[i];
                    Double value = DoubleTranslator.get().stringToValue(fields[i]);
                    matrix.set("value", value, rowId, columnId);
                }
            }

            in.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        progressMonitor.end();

        return matrix;
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

    @Override
    protected void writeResource(IResourceLocator resourceLocator, IMatrix matrix, IProgressMonitor monitor) throws PersistenceException {

        monitor.begin("Saving matrix...", matrix.getColumns().size());

        try {
            OutputStream out = resourceLocator.openOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            int numCols = matrix.getColumns().size();

            final String[] colNames = MatrixUtils.createLabelArray(matrix.getColumns());

            for (int i = 0; i < numCols; i++) {
                final String name = i < colNames.length ? colNames[i] : "";
                pw.print('\t');
                pw.print(name);
            }

            pw.print('\n');

            int numRows = matrix.getRows().size();

            final String[] rowNames = MatrixUtils.createLabelArray(matrix.getRows());

            for (int i = 0; i < numRows; i++) {
                final String name = i < rowNames.length ? rowNames[i] : "";

                pw.print(name);

                for (int j = 0; j < numCols; j++) {
                    pw.print('\t');
                    pw.print(DoubleTranslator.get().valueToString(MatrixUtils.doubleValue(matrix.getValue(i, j, 0))));
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
