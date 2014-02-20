/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.app.fileimport.wizard.text.reader;

import org.gitools.api.matrix.IMatrix;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.utils.readers.FileHeader;
import org.gitools.utils.readers.MatrixReaderProfile;
import org.gitools.utils.translators.DoubleTranslator;

import java.util.List;

public class MatrixReaderAssistant extends ReaderAssistant {

    private String[] columnHeaders;
    private MatrixReaderProfile profile;
    private String currentRowId;
    private String[] currentFields;

    public MatrixReaderAssistant(FlatTextImporter reader) {
        super(reader);
    }

    @Override
    public void fillMatrix(IMatrix matrix) {
        processLine();
        for (int i = 0; i < currentFields.length; i++) {
            Double value = DoubleTranslator.get().stringToValue(currentFields[i]);
            matrix.set(heatmapLayers[0], value, currentRowId, columnHeaders[i]);
        }
    }

    private void processLine() {
        String[] currentLine = reader.getCurrentLine();
        currentFields = extractFields(currentLine, profile.getValueColumns());
        currentRowId = currentLine[profile.getRowIdsPosition()];
    }

    private String[] extractFields(String[] fields, int[] indices) {
        String[] extracted = new String[indices.length];
        for (int i = 0; i < indices.length; i++) {
            extracted[i] = fields[indices[i]];
        }
        return extracted;
    }

    @Override
    public String[] getHeatmapHeaders() {
        return new String[]{profile.getDataName()};
    }

    @Override
    public void update() {
        this.profile = (MatrixReaderProfile) reader.getReaderProfile();
        this.heatmapLayers = createHeatmapLayers();
        this.columnHeaders = getColumnHeaders();
    }

    private String[] getColumnHeaders() {
        List<FileHeader> fileHeaders = reader.getFileHeaders();
        String[] headers = new String[fileHeaders.size()];
        for (int i = 0; i < headers.length; i++) {
            headers[i] = fileHeaders.get(i).getLabel();
        }
        return extractFields(headers, profile.getValueColumns());
    }


    protected MatrixLayer[] createHeatmapLayers() {
        MatrixLayer[] heatmapLayers;
        String[] heatmapHeaders = getHeatmapHeaders();
        heatmapLayers = new MatrixLayer[profile.getValueColumnsNumber()];
        for (int i = 0; i < heatmapHeaders.length; i++) {
            heatmapLayers[i] = new MatrixLayer<>(heatmapHeaders[i], Double.class);
        }
        return heatmapLayers;
    }
}
