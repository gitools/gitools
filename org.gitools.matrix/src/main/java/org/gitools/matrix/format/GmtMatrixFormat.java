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
package org.gitools.matrix.format;

import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.matrix.model.MatrixLayers;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.utils.text.CSVReader;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

@ApplicationScoped
public class GmtMatrixFormat extends AbstractMatrixFormat {

    public static final String EXTENSION = "gmt";

    public GmtMatrixFormat() {
        super(EXTENSION);
    }

    @Override
    protected IMatrix readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

        progressMonitor.begin("Reading names ...", 1);

        MatrixLayer<Double> layer = new MatrixLayer<>("value", Double.class);
        HashMatrix matrix = new HashMatrix(new MatrixLayers<MatrixLayer>(layer), ROWS, COLUMNS);


        try {

            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            String[] fields;

            while ((fields = parser.readNext()) != null) {

                if (fields.length < 2) {
                    throw new PersistenceException("Invalid row, at least 2 columns required (name and description) at line " + parser.getLineNumber());
                }

                String columnId = fields[0];

                //TODO Use fields[1] as layer description

                for (int i = 2; i < fields.length; i++) {
                    String rowId = fields[i];
                    matrix.set(layer, 1.0, rowId, columnId);
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
            IMatrixDimension rows = matrix.getRows();
            IMatrixDimension columns = matrix.getColumns();

            for (String column : columns) {
                pw.append(column);
                pw.append('\t'); // description, but currently not used
                for (String row : rows) {
                    if (matrix.get(layer, row, column) == 1.0) {
                        pw.append('\t').append(row);
                    }
                }
                pw.println();
            }

            out.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        progressMonitor.end();
    }
}
