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
package org.gitools.analysis.groupcomparison;

import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroup;
import org.gitools.analysis.htest.MtcTestProcessor;
import org.gitools.analysis.stats.test.MannWhitneyWilxoxonTest;
import org.gitools.analysis.stats.test.results.GroupComparisonResult;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.*;
import org.gitools.api.resource.ResourceReference;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.matrix.model.matrix.element.LayerAdapter;
import org.gitools.matrix.model.matrix.element.MapLayerAdapter;

import java.util.Arrays;
import java.util.Date;

import static org.gitools.analysis.stats.mtc.MTCFactory.createFromName;
import static org.gitools.analysis.stats.mtc.MTCFactory.createFunction;
import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class GroupComparisonProcessor extends MtcTestProcessor {

    private final GroupComparisonAnalysis analysis;

    public GroupComparisonProcessor(GroupComparisonAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public void run(IProgressMonitor monitor) throws AnalysisException {
        Date startTime = new Date();

        // Prepare input data matrix
        IMatrix dataMatrix = analysis.getData().get();
        IMatrixLayer<Double> layer = dataMatrix.getLayers().get(analysis.getLayerName());

        // Prepare dimensions to compare
        IMatrixDimension sourceRows = (analysis.isTransposeData() ? dataMatrix.getDimension(COLUMNS) : dataMatrix.getDimension(ROWS));
        IMatrixDimension sourceColumns = (analysis.isTransposeData() ? dataMatrix.getDimension(ROWS) : dataMatrix.getDimension(COLUMNS));

        // Prepare results data matrix
        LayerAdapter<GroupComparisonResult> adapter = new LayerAdapter<>(GroupComparisonResult.class);
        MannWhitneyWilxoxonTest test = (MannWhitneyWilxoxonTest) analysis.getTest();

        HashMatrixDimension resultColumns = new HashMatrixDimension(COLUMNS, Arrays.asList(test.getName()));
        HashMatrixDimension resultsRows = new HashMatrixDimension(ROWS, sourceRows);

        IMatrix resultsMatrix = new HashMatrix(
                adapter.getMatrixLayers(),
                resultsRows,
                resultColumns
        );

        // Prepare group predicates
        NullConversion nullConversionFunction = new NullConversion(analysis.getNullConversion());

        // Run comparison
        dataMatrix.newPosition()
                .iterate(sourceRows)
                .monitor(monitor, "Running group comparison analysis")
                .transform(
                        new GroupComparisonFunction(
                                test,
                                sourceColumns,
                                layer,
                                nullConversionFunction,
                                analysis.getGroups().toArray(new DimensionGroup[analysis.getGroups().size()]))
                )
                .store(resultsMatrix, new MapLayerAdapter<>(resultColumns, adapter));

        // Run multiple test correction
        IMatrixPosition position = resultsMatrix.newPosition().set(resultColumns, test.getName());
        IMatrixFunction<Double, Double> mtcFunction = createFunction(createFromName(analysis.mtc()));

        // Left p-Value
        position.iterate(adapter.getLayer(Double.class, "left-p-value"), resultsRows)
                .transform(mtcFunction)
                .store(resultsMatrix, adapter.getLayer(Double.class, "corrected-left-p-value"));

        // Right p-Value
        position.iterate(adapter.getLayer(Double.class, "right-p-value"), resultsRows)
                .transform(mtcFunction)
                .store(resultsMatrix, adapter.getLayer(Double.class, "corrected-right-p-value"));

        // Two-tail p-Value
        position.iterate(adapter.getLayer(Double.class, "two-tail-p-value"), resultsRows)
                .transform(mtcFunction)
                .store(resultsMatrix, adapter.getLayer(Double.class, "corrected-two-tail-p-value"));

        // Finish
        analysis.setStartTime(startTime);
        analysis.setElapsedTime(System.currentTimeMillis() - startTime.getTime());
        analysis.setResults(new ResourceReference<>("results", resultsMatrix));
        monitor.end();
    }


}
