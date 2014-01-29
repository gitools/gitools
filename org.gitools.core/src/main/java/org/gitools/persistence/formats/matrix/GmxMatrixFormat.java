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
import org.gitools.api.matrix.IMatrixLayer;
import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.core.matrix.model.MatrixLayer;
import org.gitools.core.matrix.model.MatrixLayers;
import org.gitools.core.matrix.model.hashmatrix.HashMatrix;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.utils.csv.CSVReader;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

@ApplicationScoped
public class GmxMatrixFormat extends AbstractMatrixFormat {

    public static final String EXTENSION = "gmx";

    public GmxMatrixFormat() {
        super(EXTENSION);
    }

    @Override
    protected IMatrix readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Reading ...", 1);

        MatrixLayer<Double> layer = new MatrixLayer<>("value", Double.class);
        HashMatrix matrix = new HashMatrix(new MatrixLayers<MatrixLayer>(layer), ROWS, COLUMNS);

        try {

            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            // read column names
            String[] columns = parser.readNext();

            // Discard descriptions
            parser.readNext();

            String[] fields;
            while ((fields = parser.readNext()) != null) {

                if (fields.length > columns.length) {
                    throw new PersistenceException("Row with more columns than expected at line " + parser.getLineNumber());
                }

                for (int i = 0; i < fields.length; i++) {
                    matrix.set(layer, 1.0, fields[i], columns[i]);
                }
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
    protected void writeResource(IResourceLocator resourceLocator, IMatrix matrix, IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Saving matrix...", matrix.getColumns().size());

        try {
            OutputStream out = resourceLocator.openOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            IMatrixLayer<Double> layer = matrix.getLayers().iterator().next();
            int numColumns = matrix.getColumns().size();
            int numRows = matrix.getRows().size();

            // column labels
            pw.append(matrix.getColumns().getLabel(0));
            for (int col = 1; col < matrix.getColumns().size(); col++)
                pw.append('\t').append(matrix.getColumns().getLabel(col));
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
                    String colId = matrix.getColumns().getLabel(col);

                    if (col != 0) {
                        line.append('\t');
                    }

                    int row = positions[col];
                    if (row < numRows) {

                        String rowId = matrix.getRows().getLabel(row);
                        double value = MatrixUtils.doubleValue(matrix.get(layer, rowId, colId));
                        while (value != 1.0 && (row < numRows - 1)) {
                            row++;
                            rowId = matrix.getRows().getLabel(row);
                            value = MatrixUtils.doubleValue(matrix.get(layer, rowId, colId));
                        }

                        if (value == 1.0) {
                            line.append(matrix.getRows().getLabel(row));
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
