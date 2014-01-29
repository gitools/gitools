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
package org.gitools.persistence.formats.matrix;

import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.core.matrix.model.MatrixLayer;
import org.gitools.core.matrix.model.MatrixLayers;
import org.gitools.core.matrix.model.hashmatrix.HashMatrix;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.datafilters.DoubleTranslator;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


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

        MatrixLayer<Double> layer = new MatrixLayer<>("value", Double.class);
        HashMatrix matrix = new HashMatrix(new MatrixLayers<MatrixLayer>(layer), ROWS, COLUMNS);

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
                for (int i = 1; i < fields.length; i++) {
                    String columnId = columns[i];
                    Double value = DoubleTranslator.get().stringToValue(fields[i]);
                    matrix.set(layer, value, rowId, columnId);
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

            IMatrixLayer<Double> layer = matrix.getLayers().iterator().next();
            IMatrixDimension rows = matrix.getRows();
            IMatrixDimension columns = matrix.getColumns();

            for (String column : columns) {
                pw.print('\t');
                pw.print(column);
            }
            pw.print('\n');

            for (String row : rows) {
                pw.print(row);

                for (String column : columns) {
                    pw.print('\t');
                    pw.print(layer.getTranslator().valueToString(matrix.get(layer, row, column)));
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
