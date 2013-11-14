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
package org.gitools.core.analysis.combination;

import cern.jet.stat.Probability;
import org.gitools.core.analysis.AnalysisException;
import org.gitools.core.analysis.AnalysisProcessor;
import org.gitools.core.matrix.model.*;
import org.gitools.core.matrix.model.hashmatrix.HashMatrix;
import org.gitools.core.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.core.model.HashModuleMap;
import org.gitools.core.model.IModuleMap;
import org.gitools.core.persistence.ResourceReference;
import org.gitools.core.utils.ModuleMapUtils;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.util.Date;

import static org.gitools.core.matrix.model.MatrixDimension.COLUMNS;
import static org.gitools.core.matrix.model.MatrixDimension.ROWS;

public class CombinationProcessor implements AnalysisProcessor {

    private static MatrixLayer LAYER_N = new MatrixLayer("N", int.class, "N",  "Number of pvalues combined");
    private static MatrixLayer LAYER_Z_SCORE = new MatrixLayer("z-score", double.class, "Z-Score", "Z-Score of the combination");
    private static MatrixLayer LAYER_P_VALUE = new MatrixLayer("p-value", double.class, "P-Value", "Combined P-Value");

    private final CombinationAnalysis analysis;

    public CombinationProcessor(CombinationAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public void run(IProgressMonitor monitor) throws AnalysisException {

        Date startTime = new Date();

        // Prepare data matrix to combine
        IMatrix data = analysis.getData().get();

        // Dimension to combine using the module map
        MatrixDimension combineDimension = (analysis.isTransposeData() ? ROWS : COLUMNS);

        // Dimension to iterate
        MatrixDimension iterateDimension = (analysis.isTransposeData() ? COLUMNS : ROWS);

        // The module map
        IModuleMap moduleMap ;
        if (analysis.getGroupsMap() == null) {
            moduleMap = new HashModuleMap().addMapping("All data " + combineDimension.getLabel(), data.getIdentifiers(combineDimension));
        } else {
            moduleMap = ModuleMapUtils.filterByItems( analysis.getGroupsMap().get(), data.getIdentifiers(combineDimension) );
        }
        analysis.setGroupsMap(new ResourceReference<>("modules", moduleMap));

        // Set size and p-value layers
        String sizeLayer = analysis.getSizeLayer();
        String pvalueLayer = analysis.getValueLayer();
        if (pvalueLayer == null) {
            pvalueLayer = data.getLayers().iterator().next().getId();
        }

        // Prepare results matrix
        analysis.setResults(new ResourceReference<>("results",
                combine(
                        monitor,
                        data,
                        combineDimension,
                        iterateDimension,
                        moduleMap,
                        (IMatrixLayer<Number>) data.getLayers().get(sizeLayer),
                        (IMatrixLayer<Double>) data.getLayers().get(pvalueLayer))
        ));

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

        monitor.end();
    }

    private IMatrix combine(IProgressMonitor monitor, IMatrix data, MatrixDimension combineDimension, MatrixDimension iterateDimension, IModuleMap moduleMap, IMatrixLayer<? extends Number> sizeLayer,  IMatrixLayer<? extends Double> valueLayer) {


        final IMatrix results = new HashMatrix(
                new MatrixLayers(LAYER_N, LAYER_Z_SCORE, LAYER_P_VALUE),
                new HashMatrixDimension(ROWS, data.getIdentifiers(iterateDimension)),
                new HashMatrixDimension(COLUMNS, moduleMap.getModules())
        );

        monitor.begin("Running combination analysis ...", moduleMap.getModules().size() * data.getIdentifiers(iterateDimension).size());

        for (String row : data.getIdentifiers(iterateDimension)) {

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

        return Probability.normalInverse(pValue);
    }

    private double toPValue(double zScore) {
        if (Double.isNaN(zScore)) {
            return zScore;
        }

        return  1.0 - Probability.normal(zScore);
    }

}
