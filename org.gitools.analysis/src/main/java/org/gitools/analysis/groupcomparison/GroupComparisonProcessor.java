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
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroup;
import org.gitools.analysis.groupcomparison.format.math33Preview.CombinatoricsUtils;
import org.gitools.analysis.stats.mtc.MTCFactory;
import org.gitools.analysis.stats.test.MannWhitneyWilcoxonTest;
import org.gitools.analysis.stats.test.results.GroupComparisonResult;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.*;
import org.gitools.api.resource.ResourceReference;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.matrix.model.matrix.element.LayerAdapter;
import org.gitools.matrix.model.matrix.element.MapLayerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class GroupComparisonProcessor implements AnalysisProcessor {

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
        MannWhitneyWilcoxonTest test = (MannWhitneyWilcoxonTest) analysis.getTest();

        DimensionGroup[] groups = analysis.getGroups().toArray(new DimensionGroup[analysis.getGroups().size()]);
        ArrayList<String> resultColnames = new ArrayList<>();
        AnnotationMatrix resultColumnAnnotations = new AnnotationMatrix();

        Iterator<int[]> combIterator = CombinatoricsUtils.combinationsIterator(groups.length, 2);
        while (combIterator.hasNext()) {
            int[] groupIndices = combIterator.next();

            DimensionGroup dimensionGroup1 = groups[groupIndices[0]];
            DimensionGroup dimensionGroup2 = groups[groupIndices[1]];

            String combName = dimensionGroup1.getName() + " VS " + dimensionGroup2.getName();
            resultColnames.add(combName);
            resultColumnAnnotations.setAnnotation(combName ,"Group 1", dimensionGroup1.getName());
            resultColumnAnnotations.setAnnotation(combName, "Group 2", dimensionGroup2.getName());
        }

        HashMatrixDimension resultColumns = new HashMatrixDimension(COLUMNS, resultColnames);
        HashMatrixDimension resultsRows = new HashMatrixDimension(ROWS, sourceRows);

        Heatmap resultHeatmap = new Heatmap(
                new HashMatrix(
                    adapter.getMatrixLayers(),
                    resultsRows,
                    resultColumns
            )
        );
        resultHeatmap.getColumns().addAnnotations(resultColumnAnnotations);
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
                                groups)
                )
                .store(resultHeatmap, new MapLayerAdapter<>(resultColumns, adapter));

        // Run multiple test correction
        IMatrixPosition position = resultHeatmap.newPosition();
        IMatrixFunction<Double, Double> mtcFunction = MTCFactory.createFunction(analysis.getMtc());

        for (String condition : position.iterate(resultColumns)) {
            // Left p-Value
            position.iterate(adapter.getLayer(Double.class, "left-p-value"), resultsRows)
                    .transform(mtcFunction)
                    .store(resultHeatmap, adapter.getLayer(Double.class, "corrected-left-p-value"));

            // Right p-Value
            position.iterate(adapter.getLayer(Double.class, "right-p-value"), resultsRows)
                    .transform(mtcFunction)
                    .store(resultHeatmap, adapter.getLayer(Double.class, "corrected-right-p-value"));

            // Two-tail p-Value
            position.iterate(adapter.getLayer(Double.class, "two-tail-p-value"), resultsRows)
                    .transform(mtcFunction)
                    .store(resultHeatmap, adapter.getLayer(Double.class, "corrected-two-tail-p-value"));

        }


        // Finish
        analysis.setStartTime(startTime);
        analysis.setElapsedTime(System.currentTimeMillis() - startTime.getTime());
        analysis.setResults(new ResourceReference<>("results", resultHeatmap));
        monitor.end();
    }


}
