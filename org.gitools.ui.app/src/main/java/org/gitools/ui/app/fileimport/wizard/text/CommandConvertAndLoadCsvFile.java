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
import org.gitools.ui.app.fileimport.wizard.text.reader.FlatTextImporter;
import org.gitools.ui.app.fileimport.wizard.text.reader.ReaderAssistant;

import java.util.concurrent.CancellationException;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class CommandConvertAndLoadCsvFile extends CommandLoadFile {

    private final FlatTextImporter reader;


    public CommandConvertAndLoadCsvFile(FlatTextImporter reader) {
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

            reader.setPreviewMode(false);

            if (reader.getFileHeaders().size() < 3) {
                throw new PersistenceException("At least 3 fields expected on one line.");
            }

            ReaderAssistant assistant = reader.getReaderAssistant();

            MatrixLayer[] layers = assistant.getHeatmapLayers();
            HashMatrix resultsMatrix = new HashMatrix(new MatrixLayers<>(layers), ROWS, COLUMNS);

            // read the file body
            while ((reader.readNext())) {

                if (progressMonitor.isCancelled()) {
                    throw new CancellationException();
                }

                // Write the read line into the Matrix
                assistant.fillMatrix(resultsMatrix);
            }

            reader.close();

            return resultsMatrix;

        } catch (Exception e) {
            throw new PersistenceException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }
}
