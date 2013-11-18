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
package org.gitools.core.analysis.htest.oncozet;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import org.gitools.core.analysis.AnalysisException;
import org.gitools.core.analysis.htest.MtcTestProcessor;
import org.gitools.core.matrix.model.IMatrix;
import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.matrix.model.MatrixDimensionKey;
import org.gitools.core.matrix.model.hashmatrix.HashMatrix;
import org.gitools.core.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.core.matrix.model.matrix.element.LayerAdapter;
import org.gitools.core.model.HashModuleMap;
import org.gitools.core.model.IModuleMap;
import org.gitools.core.persistence.ResourceReference;
import org.gitools.core.stats.mtc.MTC;
import org.gitools.core.stats.mtc.MTCFactory;
import org.gitools.core.stats.test.Test;
import org.gitools.core.stats.test.factory.TestFactory;
import org.gitools.core.stats.test.results.CommonResult;
import org.gitools.core.threads.ThreadManager;
import org.gitools.core.threads.ThreadQueue;
import org.gitools.core.threads.ThreadSlot;
import org.gitools.core.utils.MatrixUtils;
import org.gitools.core.utils.ModuleMapUtils;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Iterator;

public class OncodriveProcessor extends MtcTestProcessor {

    private class RunSlot extends ThreadSlot {
        @Nullable
        public DoubleMatrix1D population;
        @Nullable
        public Test test;

        public RunSlot(ThreadQueue threadQueue) {
            super(threadQueue);
            population = null;
            test = null;
        }
    }

    private final OncodriveAnalysis analysis;

    public OncodriveProcessor(OncodriveAnalysis analysis) {

        this.analysis = analysis;
    }

    @Override
    public void run(@NotNull IProgressMonitor monitor) throws AnalysisException {

        Date startTime = new Date();

        TestFactory testFactory = TestFactory.createFactory(analysis.getTestConfig());

        IMatrix dataMatrix = analysis.getData().get();

        IModuleMap csmap = analysis.getModuleMap().get();
        if (csmap != null) {
            csmap = ModuleMapUtils.filterByItems( csmap, dataMatrix.getColumns() );
            csmap = ModuleMapUtils.filterByModuleSize( csmap, analysis.getMinModuleSize(), analysis.getMaxModuleSize());
        } else {
            csmap = new HashModuleMap().addMapping("All data columns", dataMatrix.getColumns() );
        }

        analysis.setModuleMap(new ResourceReference<>("modules", csmap));

        final int numRows = dataMatrix.getRows().size();
        final int numCsets = csmap.getModules().size();

        monitor.begin("Running oncodrive analysis...", numRows * numCsets);

        Test test = testFactory.create();

        final LayerAdapter<CommonResult> adapter = new LayerAdapter<>(test.getResultClass());
        final IMatrix resultsMatrix = new HashMatrix(
                adapter.getMatrixLayers(),
                new HashMatrixDimension(MatrixDimensionKey.ROWS, dataMatrix.getRows()),
                new HashMatrixDimension(MatrixDimensionKey.COLUMNS, csmap.getModules())
        );

        int numProcs = ThreadManager.getNumThreads();

        ThreadQueue threadQueue = new ThreadQueue(numProcs);

        for (int i = 0; i < numProcs; i++)
            try {
                threadQueue.put(new RunSlot(threadQueue));
            } catch (InterruptedException e) {
                monitor.debug("InterruptedException while initializing run queue: " + e.getLocalizedMessage());
            }

        final int minCsetSize = analysis.getMinModuleSize();
        final int maxCsetSize = analysis.getMaxModuleSize();

		/* Test analysis */
        Iterator<String> modulesIterator = csmap.getModules().iterator();
        while (modulesIterator.hasNext()) {
            final String csetName = modulesIterator.next();
            final int[] columnIndices = csmap.getItemIndices(csetName);

            final IProgressMonitor condMonitor = monitor.subtask();

            final int numColumns = columnIndices.length;

            if (numColumns >= minCsetSize && numColumns <= maxCsetSize) {

                condMonitor.begin("Column set " + csetName + "...", numRows);

                DoubleMatrix1D population = DoubleFactory1D.dense.make(numColumns * numRows);
                IMatrixLayer layer = dataMatrix.getLayers().iterator().next();

                int k = 0;
                for (int i = 0; i < numRows; i++) {
                    for (int j = 0; j < numColumns; j++) {
                        population.setQuick(k++, MatrixUtils.doubleValue(dataMatrix.get(layer, dataMatrix.getRows().getLabel(i), dataMatrix.getColumns().getLabel(columnIndices[j]))));
                    }
                }

                population = population.viewSelection(notNaNProc);

                final int[] cindices = new int[numColumns];
                for (int i = 0; i < numColumns; i++)
                    cindices[i] = i;

                for (int itemIndex = 0; itemIndex < numRows; itemIndex++) {

                    final String itemName = dataMatrix.getRows().getLabel(itemIndex);

                    final DoubleMatrix1D itemValues = DoubleFactory1D.dense.make(numColumns);

                    for (int j = 0; j < numColumns; j++)
                        itemValues.setQuick(j, MatrixUtils.doubleValue(dataMatrix.get(layer, dataMatrix.getRows().getLabel(itemIndex), dataMatrix.getColumns().getLabel(columnIndices[j]))));

                    final RunSlot slot;
                    try {
                        slot = (RunSlot) threadQueue.take();
                    } catch (InterruptedException ex) {
                        throw new AnalysisException(ex);
                    }

                    if (slot.population != population) {
                        slot.population = population;
                        slot.test = testFactory.create();
                        slot.test.processPopulation(csetName, population);
                    }

                    slot.execute(new Runnable() {
                        @Override
                        public void run() {
                            CommonResult result = null;
                            try {
                                result = slot.test.processTest(csetName, itemValues, itemName, cindices);
                            } catch (Throwable cause) {
                                cause.printStackTrace();
                            }

                            try {
                                adapter.set(resultsMatrix, result, itemName, csetName);
                            } catch (Throwable cause) {
                                cause.printStackTrace();
                            }
                        }
                    });

                    condMonitor.worked(1);
                }
            } else {
                condMonitor.begin("Column set " + csetName + " discarded.", 1);
            }

            condMonitor.end();

            monitor.worked(1);
        }

        ThreadManager.shutdown(monitor);

        MTC mtc = MTCFactory.createFromName(analysis.getMtc());

        multipleTestCorrection(adapter, resultsMatrix, mtc, monitor.subtask());

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

        analysis.setResults(new ResourceReference<>("results", resultsMatrix));

        monitor.end();
    }
}
