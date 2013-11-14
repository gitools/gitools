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
package org.gitools.core.analysis.correlation;

import org.gitools.core.analysis.AnalysisException;
import org.gitools.core.analysis.AnalysisProcessor;
import org.gitools.core.analysis.MethodException;
import org.gitools.core.analysis.correlation.methods.CorrelationMethodFactory;
import org.gitools.core.matrix.model.*;
import org.gitools.core.matrix.model.hashmatrix.HashMatrix;
import org.gitools.core.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.core.persistence.ResourceReference;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static org.gitools.core.matrix.model.MatrixDimension.COLUMNS;
import static org.gitools.core.matrix.model.MatrixDimension.ROWS;

public class CorrelationProcessor implements AnalysisProcessor {

    private static MatrixLayer<Integer> LAYER_N = new MatrixLayer<>("n", Integer.class, "Observations", "Number of observations");
    private static MatrixLayer<Double> LAYER_SCORE = new MatrixLayer<>("score", Double.class, "Correlation", "Correlation score");
    private static MatrixLayer<Double> LAYER_STANDARD_ERROR = new MatrixLayer<>("se", Double.class, "Standard Error", "Standard Error");
    private final CorrelationAnalysis analysis;

    public CorrelationProcessor(CorrelationAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public void run(@NotNull IProgressMonitor monitor) throws AnalysisException {

        Date startTime = new Date();

        CorrelationMethod method = CorrelationMethodFactory.createMethod(analysis.getMethod(), analysis.getMethodProperties());

        IMatrix data = analysis.getData().get();
        int layerIndex = analysis.getAttributeIndex();

        IMatrixLayer<Double> layer = data.getLayers().get(layerIndex);
        IMatrixDimension rows = analysis.isTransposeData() ? data.getIdentifiers(COLUMNS) : data.getIdentifiers(ROWS);
        IMatrixDimension columns = analysis.isTransposeData() ? data.getIdentifiers(ROWS) : data.getIdentifiers(COLUMNS);

        monitor.begin("Running correlation analysis ...", columns.size() * (columns.size() - 1) / 2);

        final IMatrix results = new HashMatrix(
                new MatrixLayers<MatrixLayer>(
                        LAYER_N, LAYER_SCORE, LAYER_STANDARD_ERROR
                ),
                new HashMatrixDimension(ROWS, columns),
                new HashMatrixDimension(COLUMNS, columns)
        );

        analysis.setResults(new ResourceReference<>("results", results));

        MatrixIterable<Double> x = new MatrixIterable<>(data, layer, rows.getId());
        MatrixIterable<Double> y = new MatrixIterable<>(data, layer, rows.getId());

        for (int i = 0; i < columns.size() && !monitor.isCancelled(); i++) {

            String columnX = columns.getLabel(i);
            x.set(columns.getId(), columnX);

            for (int j = i; j < columns.size() && !monitor.isCancelled(); j++) {

                String columnY = columns.getLabel(j);
                y.set(columns.getId(), columnY);

                monitor.info("Correlating " + columnX + " with " + columnY);

                try {
                    CorrelationResult result = method.correlation(x, y, analysis.getReplaceNanValue());

                    results.set(LAYER_N, result.getN(), columnX, columnY);
                    results.set(LAYER_SCORE, result.getScore(), columnX, columnY);
                    results.set(LAYER_STANDARD_ERROR, result.getStandardError(), columnX, columnY);

                } catch (MethodException ex) {
                    throw new AnalysisException(ex);
                }

                monitor.worked(1);
            }
        }

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

        monitor.end();
    }
}
