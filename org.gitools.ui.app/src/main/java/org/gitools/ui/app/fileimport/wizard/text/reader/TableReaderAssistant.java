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
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.utils.readers.FileHeader;
import org.gitools.utils.readers.profile.TableReaderProfile;
import org.gitools.utils.translators.DoubleTranslator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableReaderAssistant extends ReaderAssistant {

    private String currentColId;
    private TableReaderProfile profile;
    private String currentRowId;
    private String[] currentFields;
    private Map<IMatrixLayer, Map<String,Integer>> factorMap;
    private String[] rowAnnFieldNames;
    private String[] rowAnnFields;
    private String[] colAnnFieldNames;
    private String[] colAnnFields;

    public TableReaderAssistant(FlatTextImporter reader) {
        super(reader);
        factorMap = new HashMap<>();
    }

    @Override
    public void fillMatrixAndAnnotations(IMatrix matrix, AnnotationMatrix rowAnnMatrix, AnnotationMatrix colAnnMatrix) {
        processLine();
        for (int i = 0; i < heatmapLayers.length; i++) {
            Double value = DoubleTranslator.get().stringToValue(currentFields[i]);
            if (value == null && !(currentFields[i].equals("") || currentFields[i].equals("-")) ) {
                value = factorize(heatmapLayers[i],currentFields[i]);
            }
            matrix.set(heatmapLayers[i], value, currentRowId, currentColId);
        }
        if (hasRowAnnotation()) {
            for (int i = 0; i < rowAnnFields.length; i++) {
                rowAnnMatrix.setAnnotation(currentRowId, rowAnnFieldNames[i], rowAnnFields[i]);
            }
        }
        if (hasColAnnotation()) {
            for (int i = 0; i < colAnnFields.length; i++) {
                colAnnMatrix.setAnnotation(currentColId, colAnnFieldNames[i], colAnnFields[i]);
            }
        }
    }

    private Double factorize(MatrixLayer heatmapLayer, String value) {

        Map<String, Integer> layerFactorMap;
        if (!factorMap.containsKey(heatmapLayer)) {
            layerFactorMap = new HashMap<>();
            factorMap.put(heatmapLayer, layerFactorMap);
        }

        layerFactorMap = factorMap.get(heatmapLayer);

        if (layerFactorMap.containsKey(value)) {
            return (double) layerFactorMap.get(value);
        } else {
            int newFactor = layerFactorMap.values().size() + 1;
            layerFactorMap.put(value, newFactor);
            return (double) newFactor;
        }
    }

    @Override
    public String[] getHeatmapHeaders() {
        List<FileHeader> fileHeaders = reader.getFileHeaders();
        String[] headers = new String[fileHeaders.size()];
        for (int i = 0; i < headers.length; i++) {
            headers[i] = fileHeaders.get(i).getLabel();
        }
        return getDataFields(headers);
    }

    @Override
    public void update() {
        this.profile = (TableReaderProfile) reader.getReaderProfile();
        this.heatmapLayers = createHeatmapLayers();
        this.updateAnnotationNames();
    }

    @Override
    public boolean hasColAnnotation() {
        return profile.getColumnAnnotationColumns().length > 0;
    }

    @Override
    public boolean hasRowAnnotation() {
        return profile.getRowAnnotationColumns().length > 0;
    }

    @Override
    public void updateAnnotationNames() {
        List<FileHeader> fileHeaders = reader.getFileHeaders();
        if (hasRowAnnotation()) {
            int[] idx = profile.getRowAnnotationColumns();
            this.rowAnnFieldNames = new String[idx.length];
            for (int i = 0; i < idx.length; i++) {
                rowAnnFieldNames[i] = fileHeaders.get(idx[i]).toString();
            }
        }
        if (hasColAnnotation()) {
            int[] idx = profile.getColumnAnnotationColumns();
            this.colAnnFieldNames = new String[idx.length];
            for (int i = 0; i < idx.length; i++) {
                colAnnFieldNames[i] = fileHeaders.get(idx[i]).toString();
            }
        }
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

    private void processLine() {
        String[] currentLine = reader.getCurrentLine();
        this.currentColId = getColId(currentLine);
        this.currentRowId = getRowId(currentLine);
        this.currentFields = getDataFields(currentLine);
        this.rowAnnFields = getFields(currentLine, profile.getRowAnnotationColumns());
        this.colAnnFields = getFields(currentLine, profile.getColumnAnnotationColumns());
    }

    public String[] getDataFields(String[] fields) {
        int[] dataColumns = profile.getValueColumns();
        String[] data = new String[dataColumns.length];
        for (int i = 0; i < dataColumns.length; i++) {
            data[i] = fields[dataColumns[i]];
        }
        return data;
    }

    private String[] getFields(String[] fields, int[] dataColumns) {
        String[] data = new String[dataColumns.length];
        for (int i = 0; i < dataColumns.length; i++) {
            data[i] = fields[dataColumns[i]];
        }
        return data;
    }


    public String getColId(String[] fields) {
        return composeId(fields, profile.getHeatmapColumnsIds());
    }

    public String getRowId(String[] fields) {
        return composeId(fields, profile.getHeatmapRowsIds());
    }

    private String composeId(String[] fields, int[] indices) {
        StringBuilder id = new StringBuilder("");
        for (int i = 0; i < indices.length; i++) {
            if (!id.toString().equals("")) {
                id.append(profile.getFieldGlue());
            }
            id.append(fields[indices[i]]);
        }
        return id.toString();
    }

    public Map<IMatrixLayer, Map<String, Integer>> getFactorMap() {
        return factorMap;
    }
}
