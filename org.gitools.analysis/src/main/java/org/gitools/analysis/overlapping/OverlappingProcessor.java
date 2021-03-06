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
package org.gitools.analysis.overlapping;

import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.resource.ResourceReference;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.matrix.model.matrix.element.LayerAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;


public class OverlappingProcessor implements AnalysisProcessor {

    private final OverlappingAnalysis analysis;

    public OverlappingProcessor(OverlappingAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public void run(IProgressMonitor monitor) {
        Date startTime = new Date();

        IMatrix data = analysis.getSourceData().get();

        IMatrixLayer<Double> layer = data.getLayers().get(analysis.getAttributeName());
        if (layer == null) {
            layer = data.getLayers().iterator().next();
        }

        IMatrixDimension rows = (analysis.isTransposeData() ? data.getDimension(COLUMNS) : data.getDimension(ROWS));
        IMatrixDimension columns = (analysis.isTransposeData() ? data.getDimension(ROWS) : data.getDimension(COLUMNS));

        monitor.begin("Running Overlapping analysis ...", columns.size() * (columns.size() - 1) / 2);

        final LayerAdapter<OverlappingResult> adapter = new LayerAdapter<>(OverlappingResult.class);
        final IMatrix results = new HashMatrix(
                adapter.getMatrixLayers(),
                new HashMatrixDimension(ROWS, columns),
                new HashMatrixDimension(COLUMNS, columns)
        );
        analysis.setCellResults(new ResourceReference<>("results", results));

        Map<String, Boolean> x = new HashMap<>(rows.size());
        Map<String, Boolean> xna = new HashMap<>(rows.size());

        IMatrixPosition positionX = data.newPosition();
        IMatrixPosition positionY = data.newPosition();

        for (String X : positionX.iterate(columns)) {

            int rowCount = 0;
            for (String row : positionX.iterate(rows)) {

                Double value = transformValue(
                        data.get(layer, positionX),
                        analysis
                );

                if (value == 1.0) {
                    rowCount++;
                }

                x.put(row, value == 1.0);
                xna.put(row, Double.isNaN(value));
            }

            for (String Y : positionY.iterate(columns.from(X))) {

                if (monitor.isCancelled()) {
                    break;
                }
                monitor.info("Overlapping " + X + " with " + Y);

                int columnCount = 0;
                int bothCount = 0;

                for (String row : positionY.iterate(rows)) {

                    double v0 = xna.get(row) ? Double.NaN : (x.get(row) ? 1.0 : 0.0);
                    Double v1 = data.get(layer, positionY);
                    v1 = transformValue(v1, analysis);

                    if (v1 == 1.0) {
                        columnCount++;
                    }
                    if (v0 == 1.0 && v1 == 1.0) {
                        bothCount++;
                    }
                }

                adapter.set(results, new OverlappingResult(rowCount, columnCount, bothCount), X, Y);
                monitor.worked(1);
            }
        }

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

    }

    private Double transformValue(Double v, OverlappingAnalysis analysis) throws AnalysisException {

        boolean isNaN = (v == null || Double.isNaN(v));

        if (isNaN) {
            v = (analysis.getReplaceNanValue() == null ? Double.NaN : analysis.getReplaceNanValue());
        }

        if (!isNaN && analysis.isBinaryCutoffEnabled()) {
            v = analysis.getBinaryCutoffCmp().compare(v, analysis.getBinaryCutoffValue()) ? 1.0 : 0.0;
        }

        if (!isNaN && v != 1.0 && v != 0.0) {
            throw new AnalysisException("Not binary value found");
        }

        return v;
    }
}
