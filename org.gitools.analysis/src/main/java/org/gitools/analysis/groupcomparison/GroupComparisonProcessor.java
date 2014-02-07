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
import org.gitools.analysis.groupcomparison.filters.GroupByLabelPredicate;
import org.gitools.analysis.groupcomparison.filters.GroupByValuePredicate;
import org.gitools.analysis.stats.mtc.MTCFactory;
import org.gitools.analysis.stats.test.MannWhitneyWilxoxonTest;
import org.gitools.analysis.stats.test.results.GroupComparisonResult;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;
import org.gitools.api.matrix.IMatrixFunction;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.IMatrixPredicate;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.matrix.model.iterable.PositionMapping;
import org.gitools.matrix.model.matrix.element.LayerAdapter;
import org.gitools.api.resource.ResourceReference;
import org.gitools.utils.datafilters.BinaryCutoff;

import java.util.Arrays;
import java.util.Date;

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
        IMatrixLayer<Double> layer = dataMatrix.getLayers().get(analysis.getAttributeIndex());

        // Prepare dimensions to compare
        IMatrixDimension rows = (analysis.isTransposeData() ? dataMatrix.getDimension(COLUMNS) : dataMatrix.getDimension(ROWS));
        IMatrixDimension columns = (analysis.isTransposeData() ? dataMatrix.getDimension(ROWS) : dataMatrix.getDimension(COLUMNS));

        // Prepare results data matrix
        LayerAdapter<GroupComparisonResult> adapter = new LayerAdapter<>(GroupComparisonResult.class);
        MannWhitneyWilxoxonTest test = (MannWhitneyWilxoxonTest) analysis.getTest();

        HashMatrixDimension conditions = new HashMatrixDimension(COLUMNS, Arrays.asList(test.getName()));
        HashMatrixDimension modules = new HashMatrixDimension(ROWS, rows);

        IMatrix resultsMatrix = new HashMatrix(
                adapter.getMatrixLayers(),
                modules,
                conditions
        );

        // Prepare group predicates
        IMatrixPredicate<Double> group1Predicate = createPredicate(dataMatrix, columns, analysis.getGroups1(), analysis.getNoneConversion());
        IMatrixPredicate<Double> group2Predicate = createPredicate(dataMatrix, columns, analysis.getGroups2(), analysis.getNoneConversion());

        // Run comparison
        dataMatrix.newPosition()
                .iterate(rows)
                .monitor(monitor, "Running group comparison analysis")
                .transform(
                        new GroupComparisonFunction(test, columns, layer, group1Predicate, group2Predicate)
                )
                .store(
                        resultsMatrix,
                        new PositionMapping().map(rows, modules).fix(conditions, test.getName()),
                        adapter
                );

        // Run multiple test correction
        IMatrixPosition position = resultsMatrix.newPosition().set(conditions, test.getName());
        IMatrixFunction<Double, Double> mtcFunction = MTCFactory.createFunction(analysis.getMtc());

        // Left p-Value
        position.iterate(adapter.getLayer(Double.class, "left-p-value"), modules)
                .transform(mtcFunction)
                .store(resultsMatrix, adapter.getLayer(Double.class, "corrected-left-p-value"));

        // Right p-Value
        position.iterate(adapter.getLayer(Double.class, "right-p-value"), modules)
                .transform(mtcFunction)
                .store(resultsMatrix, adapter.getLayer(Double.class, "corrected-right-p-value"));

        // Two-tail p-Value
        position.iterate(adapter.getLayer(Double.class, "two-tail-p-value"), modules)
                .transform(mtcFunction)
                .store(resultsMatrix, adapter.getLayer(Double.class, "corrected-two-tail-p-value"));

        // Finish
        analysis.setStartTime(startTime);
        analysis.setElapsedTime(System.currentTimeMillis() - startTime.getTime());
        analysis.setResults(new ResourceReference<>("results", resultsMatrix));
        monitor.end();
    }

    private static IMatrixPredicate<Double> createPredicate(IMatrix matrix, IMatrixDimension dimension, ColumnGroup group, double noneConversion) {

        // Group by label
        if (group.getColumns() != null && group.getColumns().size() > 0) {
            return new GroupByLabelPredicate(dimension, group);
        }

        // Group by value
        IMatrixLayer<Double> cutoffLayer = matrix.getLayers().get(group.getCutoffAttributeIndex());
        BinaryCutoff binaryCutoff = group.getBinaryCutoff();
        Double nullValue = (Double.isNaN(noneConversion) ? null : noneConversion);
        return new GroupByValuePredicate(cutoffLayer, binaryCutoff, nullValue);

    }


}
