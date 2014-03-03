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
package org.gitools.analysis.correlation;

import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.analysis.MethodException;
import org.gitools.analysis.correlation.methods.CorrelationMethodFactory;
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

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class CorrelationProcessor implements AnalysisProcessor {

    private final CorrelationAnalysis analysis;

    public CorrelationProcessor(CorrelationAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public void run(IProgressMonitor monitor) throws AnalysisException {

        Date startTime = new Date();

        CorrelationMethod method = CorrelationMethodFactory.createMethod(analysis.getMethod(), analysis.getMethodProperties());

        IMatrix data = analysis.getData().get();
        int layerIndex = analysis.getAttributeIndex();

        IMatrixLayer<Double> layer = data.getLayers().get(layerIndex);
        IMatrixDimension comparedDimension = analysis.isTransposeData() ? data.getDimension(COLUMNS) : data.getDimension(ROWS);
        IMatrixDimension correlatedDimension = analysis.isTransposeData() ? data.getDimension(ROWS) : data.getDimension(COLUMNS);

        monitor.begin("Running correlation analysis ...", correlatedDimension.size() * (correlatedDimension.size() - 1) / 2);

        LayerAdapter<CorrelationResult> adapter = new LayerAdapter<>(CorrelationResult.class);
        final IMatrix results = new HashMatrix(
                adapter.getMatrixLayers(),
                new HashMatrixDimension(ROWS, correlatedDimension),
                new HashMatrixDimension(COLUMNS, correlatedDimension)
        );

        analysis.setResults(new ResourceReference<>("results", results));

        IMatrixPosition positionX = data.newPosition();
        IMatrixPosition positionY = data.newPosition();

        for (String X : positionX.iterate(correlatedDimension)) {

            if (monitor.isCancelled()) {
                break;
            }

            for (String Y : positionY.iterate(correlatedDimension).from(X)) {

                monitor.info("Correlating " + X + " with " + Y);

                try {
                    CorrelationResult result = method.correlation(
                            positionX.iterate(layer, comparedDimension),
                            positionY.iterate(layer, comparedDimension),
                            analysis.getReplaceNanValue()
                    );

                    adapter.set(results, result, X, Y);

                } catch (MethodException ex) {
                    throw new AnalysisException(ex);
                }

                monitor.worked(1);
            }
        }

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

    }
}
