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

import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.hashmatrix.HashMatrix;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class GmtMatrixFormat extends AbstractMatrixFormat {

    public static final String EXTENSION = "gmt";

    public GmtMatrixFormat() {
        super(EXTENSION);
    }

    @Override
    protected IMatrix readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

        progressMonitor.begin("Reading names ...", 1);

        HashMatrix matrix = new HashMatrix();


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
                    matrix.setValue(rowId, columnId, "value", 1.0);
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
    protected void writeResource(@NotNull IResourceLocator resourceLocator, IMatrix matrix, @NotNull IProgressMonitor progressMonitor) throws PersistenceException {
        progressMonitor.begin("Saving matrix...", matrix.getColumns().size());

        try {

            OutputStream out = resourceLocator.openOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

            final int rowCount = matrix.getRows().size();

            for (int ci = 0; ci < matrix.getColumns().size(); ci++) {
                pw.append(matrix.getColumns().getLabel(ci));
                pw.append('\t'); // description, but currently not used
                for (int ri = 0; ri < rowCount; ri++) {
                    Double value = MatrixUtils.doubleValue(matrix.getValue(ri, ci, 0));
                    if (value == 1.0) {
                        pw.append('\t').append(matrix.getRows().getLabel(ri));
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
