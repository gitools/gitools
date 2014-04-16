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
package org.gitools.analysis.combination;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.AnalysisProcessor;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.api.resource.ResourceReference;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.matrix.model.MatrixLayers;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.matrix.modulemap.HashModuleMap;
import org.gitools.matrix.modulemap.ModuleMapUtils;

import java.util.Date;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class CombinationProcessor implements AnalysisProcessor {

    private static MatrixLayer LAYER_N = new MatrixLayer("N", int.class, "N", "Number of pvalues combined");
    private static MatrixLayer LAYER_Z_SCORE = new MatrixLayer("z-score", double.class, "Z-Score", "Z-Score of the combination");
    private static MatrixLayer LAYER_P_VALUE = new MatrixLayer("p-value", double.class, "P-Value", "Combined P-Value");

    private final CombinationAnalysis analysis;

    private static NormalDistribution NORMAL = new NormalDistribution();

    public CombinationProcessor(CombinationAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public void run(IProgressMonitor monitor) {

        Date startTime = new Date();

        // Prepare data matrix to combine
        IMatrix data = analysis.getData().get();

        // Dimension to combine using the module map
        MatrixDimensionKey combineDimension = (analysis.isTransposeData() ? ROWS : COLUMNS);

        // Dimension to iterate
        MatrixDimensionKey iterateDimension = (analysis.isTransposeData() ? COLUMNS : ROWS);

        // The module map
        IModuleMap moduleMap;
        if (analysis.getGroupsMap() == null) {
            moduleMap = new HashModuleMap().addMapping("All data " + combineDimension.getLabel(), data.getDimension(combineDimension));
        } else {
            moduleMap = ModuleMapUtils.filterByItems(analysis.getGroupsMap().get(), data.getDimension(combineDimension));
        }
        analysis.setGroupsMap(new ResourceReference<>("modules", moduleMap));

        // Set size and p-value layers
        String sizeLayer = analysis.getSizeLayer();
        String pvalueLayer = analysis.getValueLayer();

        if (pvalueLayer == null) {
            for (IMatrixLayer layer : data.getLayers()) {
                pvalueLayer = layer.getId();
            }
        }

        // Prepare results matrix
        analysis.setResults(new ResourceReference<>("results",
                combine(
                        monitor,
                        data,
                        combineDimension,
                        iterateDimension,
                        moduleMap,
                        sizeLayer,
                        pvalueLayer)
        ));

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

    }

    private IMatrix combine(IProgressMonitor monitor, IMatrix data, MatrixDimensionKey combineDimension, MatrixDimensionKey iterateDimension, IModuleMap moduleMap, String sizeLayerId, String valueLayerId) {

        final IMatrix results = new HashMatrix(
                new MatrixLayers(LAYER_N, LAYER_Z_SCORE, LAYER_P_VALUE),
                new HashMatrixDimension(ROWS, data.getDimension(iterateDimension)),
                new HashMatrixDimension(COLUMNS, moduleMap.getModules())
        );

        monitor.begin("Running combination analysis ...", moduleMap.getModules().size() * data.getDimension(iterateDimension).size());

        IMatrixLayer<Double> valueLayer = data.getLayers().get(valueLayerId);
        IMatrixLayer<Number> sizeLayer = data.getLayers().get(sizeLayerId);

        for (String row : data.getDimension(iterateDimension)) {

            for (String module : moduleMap.getModules()) {

                int n = 0;
                double sumSizeZ = 0;
                double sumSizeSqr = 0;

                for (String item : moduleMap.getMappingItems(module)) {

                    Double pValue = data.get(valueLayer, row, item);
                    if (pValue != null) {

                        double size = sizeLayer == null ? 1 : data.get(sizeLayer, row, item).doubleValue();
                        double zScore = toZScore(pValue);

                        if (!Double.isNaN(size + pValue + zScore)) {
                            n++;
                            sumSizeZ += size * zScore;
                            sumSizeSqr += size * size;
                        }
                    }

                }

                double zComb = sumSizeZ / Math.sqrt(sumSizeSqr);
                double pValue = toPValue(zComb);

                results.set(LAYER_N, n, row, module);
                results.set(LAYER_Z_SCORE, zComb, row, module);
                results.set(LAYER_P_VALUE, pValue, row, module);

                monitor.worked(1);

            }
        }

        return results;
    }

    private double toZScore(double pValue) {
        if (Double.isNaN(pValue)) {
            return pValue;
        }

        pValue = 1.0 - pValue;
        pValue = pValue < 0.000001 ? 0.000001 : pValue;
        pValue = pValue > 0.999999 ? 0.999999 : pValue;

        return NORMAL.inverseCumulativeProbability(pValue);
    }

    private double toPValue(double zScore) {
        if (Double.isNaN(zScore)) {
            return zScore;
        }

        return 1.0 - NORMAL.cumulativeProbability(zScore);

    }

}
