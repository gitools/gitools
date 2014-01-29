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
import org.gitools.api.matrix.IMatrixLayer;
import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;
import org.gitools.analysis._DEPRECATED.heatmap.Heatmap;
import org.gitools.analysis._DEPRECATED.matrix.model.hashmatrix.HashMatrix;
import org.gitools.analysis._DEPRECATED.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.analysis._DEPRECATED.matrix.model.matrix.element.LayerAdapter;
import org.gitools.analysis._DEPRECATED.model.IModuleMap;
import org.gitools.analysis._DEPRECATED.utils.MatrixUtils;
import org.gitools.analysis._DEPRECATED.utils.ModuleMapUtils;
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

    private class RunSlot extends ThreadSlot {

        public DoubleMatrix1D population;

        public Test test;

        public RunSlot(ThreadQueue threadQueue) {
            super(threadQueue);
            population = null;
            test = null;
        }
    }

    private final EnrichmentAnalysis analysis;

    public EnrichmentProcessor(EnrichmentAnalysis analysis) {

        this.analysis = analysis;
    }

    @Override
    public void run(IProgressMonitor monitor) throws AnalysisException {

        Date startTime = new Date();

        TestFactory testFactory = TestFactory.createFactory(analysis.getTestConfig());

        IMatrix dataMatrix = analysis.getData().get();
        IMatrixLayer layer = dataMatrix.getLayers().iterator().next();

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
        }

        final int numConditions = dataMatrix.getColumns().size();
        final int numRows = dataMatrix.getRows().size();

        IModuleMap mmap = analysis.getModuleMap().get();
        mmap = ModuleMapUtils.filterByItems(mmap, dataMatrix.getRows());
        mmap = ModuleMapUtils.filterByModuleSize(mmap, analysis.getMinModuleSize(), analysis.getMaxModuleSize());

        Collection<String> modules = mmap.getModules();

        monitor.begin("Running enrichment analysis...", numConditions);
        Test test = testFactory.create();

        final LayerAdapter<CommonResult> adapter = new LayerAdapter<>(test.getResultClass());

        final IMatrix resultsMatrix = new HashMatrix(
                adapter.getMatrixLayers(),
                new HashMatrixDimension(ROWS, modules),
                new HashMatrixDimension(COLUMNS, dataMatrix.getColumns())
        );

        int numProcs = ThreadManager.getNumThreads();

        ThreadQueue threadQueue = new ThreadQueue(numProcs);

        for (int i = 0; i < numProcs; i++)
            try {
                threadQueue.put(new RunSlot(threadQueue));
            } catch (InterruptedException e) {
                monitor.debug("InterruptedException while initializing run queue: " + e.getLocalizedMessage());
            }

        final int minModuleSize = analysis.getMinModuleSize();
        final int maxModuleSize = analysis.getMaxModuleSize();

		/* Test analysis */

        for (int condIndex = 0; condIndex < numConditions && !monitor.isCancelled(); condIndex++) {

            final String condName = dataMatrix.getColumns().getLabel(condIndex);

            final DoubleMatrix1D condItems = DoubleFactory1D.dense.make(numRows);
            for (int i = 0; i < numRows; i++) {
                double value = MatrixUtils.doubleValue(dataMatrix.get(layer, dataMatrix.getRows().getLabel(i), dataMatrix.getColumns().getLabel(condIndex)));

                condItems.setQuick(i, value);
            }

            DoubleMatrix1D population = condItems.viewSelection(notNaNProc);

            final IProgressMonitor condMonitor = monitor.subtask();

            condMonitor.begin("Condition " + condName + "...", modules.size());

            Iterator<String> modulesIterator = modules.iterator();
            while (modulesIterator.hasNext() && !monitor.isCancelled()) {

                final String moduleName = modulesIterator.next();
                final int[] itemIndices = mmap.getItemIndices(moduleName);

                final RunSlot slot;
                try {
                    slot = (RunSlot) threadQueue.take();
                } catch (InterruptedException ex) {
                    throw new AnalysisException(ex);
                }

                if (slot.population != population) {
                    slot.population = population;
                    slot.test = testFactory.create();
                    slot.test.processPopulation(condName, population);
                }

                slot.execute(new Runnable() {
                    @Override
                    public void run() {
                        CommonResult result = null;
                        try {
                            int moduleSize = (int) condItems.viewSelection(itemIndices).aggregate(Functions.plus, new DoubleFunction() {
                                @Override
                                public double apply(double d) {
                                    return Double.isNaN(d) ? 0 : 1;
                                }
                            });

                            if (moduleSize >= minModuleSize && moduleSize <= maxModuleSize) {
                                result = slot.test.processTest(condName, condItems, moduleName, itemIndices);
                            }
                        } catch (Throwable cause) {
                            cause.printStackTrace();
                        }

                        try {
                            adapter.set(resultsMatrix, result, moduleName, condName);

                        } catch (Throwable cause) {
                            cause.printStackTrace();
                        }
                    }
                });

                condMonitor.worked(1);
            }

            condMonitor.end();
            monitor.worked(1);
        }

        ThreadManager.shutdown(monitor);

        if (monitor.isCancelled()) {
            return;
        }

		/* Multiple test correction */

        MTC mtc = MTCFactory.createFromName(analysis.getMtc());

        multipleTestCorrection(adapter, resultsMatrix, mtc, monitor.subtask());

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

        analysis.setResults(new ResourceReference<>("results", resultsMatrix));

        monitor.end();
    }
}
