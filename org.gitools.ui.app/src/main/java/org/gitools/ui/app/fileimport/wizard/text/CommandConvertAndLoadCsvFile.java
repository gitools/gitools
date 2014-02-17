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
package org.gitools.ui.app.fileimport.wizard.text;

import org.apache.commons.io.IOUtils;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResource;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.matrix.model.MatrixLayers;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.ui.app.commands.CommandLoadFile;
import org.gitools.utils.text.ReaderProfile;
import org.gitools.utils.translators.DoubleTranslator;

import java.util.concurrent.CancellationException;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class CommandConvertAndLoadCsvFile extends CommandLoadFile {

    private final FlatTextReader reader;


    public CommandConvertAndLoadCsvFile(FlatTextReader reader) {
        super(reader.getLocator().getURL().toString());
        this.reader = reader;
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    protected IResource loadResource(IProgressMonitor progressMonitor) {

        try {

            if (reader.getFileHeaders().size() < 3) {
                throw new PersistenceException("At least 3 fields expected on one line.");
            }

            String[] heatmapHeaders = reader.getHeatmapHeaders();
            MatrixLayer<Double> layers[] = new MatrixLayer[reader.getLayerNumber()];
            for (int i = 0; i < heatmapHeaders.length; i++) {
                layers[i] = new MatrixLayer<>(heatmapHeaders[i], Double.class);
            }

            HashMatrix resultsMatrix = new HashMatrix(new MatrixLayers<MatrixLayer>(layers), ROWS, COLUMNS);

            // read body
            while ((reader.readNext())) {

                if (progressMonitor.isCancelled()) {
                    throw new CancellationException();
                }

                if (reader.getReaderProfile().getLayout().equals(ReaderProfile.TABLE)) {
                    final String[] fields = reader.getFields();
                    final String columnId = reader.getColId(fields);
                    final String rowId = reader.getRowId(fields);

                    for (int i = 0; i < fields.length; i++) {
                        Double value = DoubleTranslator.get().stringToValue(fields[fields.length]);
                        resultsMatrix.set(layers[i], value, rowId, columnId);
                    }
                } else if (reader.getReaderProfile().getLayout().equals(ReaderProfile.MATRIX)) {
                    //TODO matrix fill
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
