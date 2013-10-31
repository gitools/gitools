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
import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.matrix.model.hashmatrix.HashMatrix;
import org.gitools.core.persistence.IResourceLocator;
import org.gitools.core.persistence.PersistenceException;
import org.gitools.utils.csv.CSVReader;
import org.gitools.utils.csv.RawCsvWriter;
import org.gitools.utils.fileutils.IOUtils;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.CancellationException;

public class TdmMatrixFormat extends AbstractMatrixFormat {

    public static final String EXSTENSION = "tdm";

    public TdmMatrixFormat() {
        super(EXSTENSION);
    }

    @Override
    protected IMatrix readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {

        HashMatrix resultsMatrix = new HashMatrix();

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
                    resultsMatrix.setValue(rowId, columnId, layerId, value);
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

        boolean orderByColumn = true;

        monitor.begin("Saving results...", results.getRows().size() * results.getColumns().size());

        try {
            OutputStream out = resourceLocator.openOutputStream();
            Writer writer = new OutputStreamWriter(out);
            writeCells(writer, results, orderByColumn, monitor);
            writer.close();
            out.close();
        } catch (Exception e) {
            throw new PersistenceException(e);
        } finally {
            monitor.end();
        }
    }

    private void writeCells(Writer writer, IMatrix resultsMatrix, boolean orderByColumn, IProgressMonitor progressMonitor) {

        RawCsvWriter out = new RawCsvWriter(writer, '\t', '"');

        out.writeQuotedValue("column");
        out.writeSeparator();
        out.writeQuotedValue("row");

        for (IMatrixLayer prop : resultsMatrix.getLayers()) {
            out.writeSeparator();
            out.writeQuotedValue(prop.getId());
        }

        out.writeNewLine();

        int numColumns = resultsMatrix.getColumns().size();
        int numRows = resultsMatrix.getRows().size();

        if (orderByColumn) {
            for (int colIndex = 0; colIndex < numColumns; colIndex++) {
                for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
                    writeLine(out, resultsMatrix, colIndex, rowIndex, progressMonitor);
                }
            }
        } else {
            for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
                for (int colIndex = 0; colIndex < numColumns; colIndex++) {
                    writeLine(out, resultsMatrix, colIndex, rowIndex, progressMonitor);
                }
            }
        }
    }

    private void writeLine(RawCsvWriter out, IMatrix resultsMatrix, int colIndex, int rowIndex, IProgressMonitor progressMonitor) {

        final String colName = resultsMatrix.getColumns().getLabel(colIndex);
        final String rowName = resultsMatrix.getRows().getLabel(rowIndex);

        out.writeQuotedValue(colName);
        out.writeSeparator();
        out.writeQuotedValue(rowName);

        int numProperties = resultsMatrix.getLayers().size();
        for (int propIndex = 0; propIndex < numProperties; propIndex++) {
            out.writeSeparator();

            Object value = resultsMatrix.getValue(rowIndex, colIndex, propIndex);
            if (value instanceof Double) {
                Double v = (Double) value;
                out.write(DoubleTranslator.get().valueToString(v));
            } else if (value instanceof Integer) {
                out.writeValue(value.toString());
            } else {
                out.writeQuotedValue(value.toString());
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