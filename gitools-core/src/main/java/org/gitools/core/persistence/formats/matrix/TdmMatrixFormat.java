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
import org.gitools.core.matrix.model.IMatrixDimension;
import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.matrix.model.hashmatrix.HashMatrix;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.csv.RawCsvWriter;
import org.gitools.utils.fileutils.IOUtils;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.io.*;
import java.util.concurrent.CancellationException;

import static org.gitools.core.matrix.model.MatrixDimensionKey.COLUMNS;
import static org.gitools.core.matrix.model.MatrixDimensionKey.ROWS;

public class TdmMatrixFormat extends AbstractMatrixFormat {

    public static final String EXSTENSION = "tdm";

    public TdmMatrixFormat() {
        super(EXSTENSION);
    }

    @Override
    protected IMatrix readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

        HashMatrix resultsMatrix = new HashMatrix(ROWS, COLUMNS);

        try {
            InputStream in = resourceLocator.openInputStream(progressMonitor);
            CSVReader parser = new CSVReader(new InputStreamReader(in));

            String[] header = parser.readNext();
            if (header.length < 3) {
                throw new PersistenceException("At least 3 fields expected on one line.");
            }

            // read body
            String fields[];
            while ((fields = parser.readNext()) != null) {

                if (progressMonitor.isCancelled()) {
                    throw new CancellationException();
                }

                checkLine(fields, header, parser.getLineNumber());

                final String columnId = fields[0];
                final String rowId = fields[1];

                for (int i = 2; i < fields.length; i++) {
                    final String layerId = header[i];
                    Double value = DoubleTranslator.get().stringToValue(fields[i]);
                    resultsMatrix.set(layerId, value, rowId, columnId);
                }
            }

            in.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        }

        return resultsMatrix;
    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, IMatrix results, IProgressMonitor monitor) throws PersistenceException {

        monitor.begin("Saving results...", results.getRows().size() * results.getColumns().size());

        try {
            OutputStream out = resourceLocator.openOutputStream();
            Writer writer = new OutputStreamWriter(out);
            writeCells(writer, results, monitor);
            writer.close();
            out.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        } finally {
            monitor.end();
        }
    }

    private void writeCells(Writer writer, IMatrix resultsMatrix, IProgressMonitor progressMonitor) {

        RawCsvWriter out = new RawCsvWriter(writer, '\t', '"');

        out.writeQuotedValue("column");
        out.writeSeparator();
        out.writeQuotedValue("row");

        for (IMatrixLayer layer : resultsMatrix.getLayers()) {
            out.writeSeparator();
            out.writeQuotedValue(layer.getId());
        }

        out.writeNewLine();

        IMatrixDimension columns = resultsMatrix.getColumns();
        IMatrixDimension rows = resultsMatrix.getRows();

        for (String column : columns) {
            for (String row : rows) {
                writeLine(out, resultsMatrix, column, row, progressMonitor);
            }
        }

    }

    private void writeLine(RawCsvWriter out, IMatrix resultsMatrix, String column, String row, IProgressMonitor progressMonitor) {

        out.writeQuotedValue(column);
        out.writeSeparator();
        out.writeQuotedValue(row);

        for (IMatrixLayer layer : resultsMatrix.getLayers()) {
            out.writeSeparator();

            Object value = resultsMatrix.get(layer, row, column);
            if (value instanceof Double) {
                Double v = (Double) value;
                out.write(DoubleTranslator.get().valueToString(v));
            } else if (value instanceof Integer) {
                out.writeValue(value.toString());
            } else if (value != null) {
                out.writeQuotedValue(value.toString());
            } else {
                out.writeValue("-");
            }
        }

        out.writeNewLine();
        progressMonitor.worked(1);
    }

    @Deprecated
    public static String[] readHeader(File file) throws PersistenceException {

        String[] matrixHeaders = null;
        try {
            Reader reader = IOUtils.openReader(file);

            CSVReader parser = new CSVReader(reader);

            String[] line = parser.readNext();

            // read header
            if (line.length < 3) {
                throw new PersistenceException("At least 3 columns expected.");
            }

            int numAttributes = line.length - 2;
            matrixHeaders = new String[numAttributes];
            System.arraycopy(line, 2, matrixHeaders, 0, numAttributes);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
        return matrixHeaders;
    }

}