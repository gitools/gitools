/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.app.fileimport.wizard.csv;

import org.apache.commons.io.IOUtils;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResource;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.matrix.model.MatrixLayers;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.ui.app.commands.CommandLoadFile;
import org.gitools.utils.datafilters.DoubleTranslator;

import java.util.List;
import java.util.concurrent.CancellationException;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class CommandConvertAndLoadCsvFile extends CommandLoadFile {

    private final int columnId;
    private final int rowId;
    private final List<Integer> values;
    private final CsvReader reader;

    public CommandConvertAndLoadCsvFile(int columnId, int rowId, List<Integer> values, CsvReader reader) {
        super(reader.getLocator().getURL().toString());

        this.columnId = columnId;
        this.rowId = rowId;
        this.values = values;
        this.reader = reader;
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    protected IResource loadResource(IProgressMonitor progressMonitor) {

        try {

            String[] header = reader.readNext();
            if (header.length < 3) {
                throw new PersistenceException("At least 3 fields expected on one line.");
            }

            MatrixLayer<Double> layers[] = new MatrixLayer[values.size()];
            for (int i=0; i < values.size(); i++) {
                layers[i] = new MatrixLayer<>(header[values.get(i)], Double.class);
            }

            HashMatrix resultsMatrix = new HashMatrix(new MatrixLayers<MatrixLayer>(layers), ROWS, COLUMNS);

            // read body
            String fields[];
            while ((fields = reader.readNext()) != null) {

                if (progressMonitor.isCancelled()) {
                    throw new CancellationException();
                }

                final String columnId = fields[this.columnId];
                final String rowId = fields[this.rowId];

                for (int i = 0; i < values.size(); i++) {
                    Double value = DoubleTranslator.get().stringToValue(fields[values.get(i)]);
                    resultsMatrix.set(layers[i], value, rowId, columnId);
                }
            }

            return resultsMatrix;

        } catch (Exception e) {
            throw new PersistenceException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }
}
