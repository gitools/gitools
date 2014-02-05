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
package org.gitools.analysis.htest.enrichment;

import cern.colt.function.DoubleFunction;
import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.math.Functions;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.htest.MtcTestProcessor;
import org.gitools.analysis.stats.mtc.MTC;
import org.gitools.analysis.stats.mtc.MTCFactory;
import org.gitools.analysis.stats.test.Test;
import org.gitools.analysis.stats.test.factory.TestFactory;
import org.gitools.analysis.stats.test.results.CommonResult;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.matrix.model.matrix.element.LayerAdapter;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.modulemap.ModuleMapUtils;
import org.gitools.api.resource.ResourceReference;
import org.gitools.utils.threads.ThreadManager;
import org.gitools.utils.threads.ThreadQueue;
import org.gitools.utils.threads.ThreadSlot;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EnrichmentProcessor extends MtcTestProcessor {

    private final EnrichmentAnalysis analysis;

    public EnrichmentProcessor(EnrichmentAnalysis analysis) {

        this.analysis = analysis;
    }

    @Override
    public void run(IProgressMonitor monitor) throws AnalysisException {

        Date startTime = new Date();

        // Initialize test
        TestFactory testFactory = TestFactory.createFactory(analysis.getTestConfig());
        Test test = testFactory.create();

        IMatrix data = analysis.getData().get();
        IMatrixLayer layer = data.getLayers().iterator().next();
        /*
        if (analysis.isDiscardNonMappedRows()) {
            Heatmap heatmap = new Heatmap(dataMatrix);
            Set<String> rowsToHide = new HashSet<>();
            Set<String> backgroundNames = new HashSet<>(analysis.getModuleMap().get().getItems());
            for (String row : dataMatrix.getRows()) {
                if (!backgroundNames.contains(row)) {
                    rowsToHide.add(row);
                }
            }

            if (!rowsToHide.isEmpty()) {
                heatmap.getRows().hide(rowsToHide);
                dataMatrix = heatmap;
            }
        }*/
        IMatrixDimension conditions = data.getColumns();
        IMatrixDimension items = data.getRows();

        IModuleMap moduleMap = analysis.getModuleMap().get();
        // moduleMap = ModuleMapUtils.filterByItems(moduleMap, dataMatrix.getRows());
        // moduleMap = ModuleMapUtils.filterByModuleSize(moduleMap, analysis.getMinModuleSize(), analysis.getMaxModuleSize());
        Collection<String> modules = moduleMap.getModules();

        final LayerAdapter<CommonResult> adapter = new LayerAdapter<>(test.getResultClass());

        final IMatrix resultsMatrix = new HashMatrix(
                adapter.getMatrixLayers(),
                new HashMatrixDimension(ROWS, modules),
                new HashMatrixDimension(COLUMNS, data.getColumns())
        );

        final int minModuleSize = analysis.getMinModuleSize();
        final int maxModuleSize = analysis.getMaxModuleSize();

        monitor.begin("Running enrichment analysis...", conditions.size());

        for (String condition : conditions) {

            DoubleMatrix1D condItems = DoubleFactory1D.dense.make(items.size());
            int i=0;
            for (String item : items) {
                double value = MatrixUtils.doubleValue(data.get(layer, item, condition));
                condItems.setQuick(i, value);
                i++;
            }

            DoubleMatrix1D population = condItems.viewSelection(notNaNProc);
            test.processPopulation(condition, population);

            final IProgressMonitor condMonitor = monitor.subtask();

            condMonitor.begin("Condition " + condition + "...", modules.size());

            Iterator<String> modulesIterator = modules.iterator();
            while (modulesIterator.hasNext() && !monitor.isCancelled()) {

                final String moduleName = modulesIterator.next();
                final int[] itemIndices = moduleMap.getItemIndices(moduleName);

                CommonResult result = null;
                try {
                    int moduleSize = (int) condItems.viewSelection(itemIndices).aggregate(Functions.plus, new DoubleFunction() {
                        @Override
                        public double apply(double d) {
                            return Double.isNaN(d) ? 0 : 1;
                        }
                    });

                    if (moduleSize >= minModuleSize && moduleSize <= maxModuleSize) {
                        result = test.processTest(condition, condItems, moduleName, itemIndices);
                        //TODO result = test.processTest();
                    }
                } catch (Throwable cause) {
                    cause.printStackTrace();
                }

                try {
                    adapter.set(resultsMatrix, result, moduleName, condition);

                } catch (Throwable cause) {
                    cause.printStackTrace();
                }

                condMonitor.worked(1);
            }

            condMonitor.end();
            monitor.worked(1);
        }

        ThreadManager.shutdown(monitor);

        if (monitor.isCancelled()) {
            return;
        }

        MTC mtc = MTCFactory.createFromName(analysis.getMtc());

        multipleTestCorrection(adapter, resultsMatrix, mtc, monitor.subtask());

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());
        analysis.setResults(new ResourceReference<>("results", resultsMatrix));

        monitor.end();
    }
}
