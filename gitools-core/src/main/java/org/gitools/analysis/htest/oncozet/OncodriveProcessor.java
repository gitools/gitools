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
package org.gitools.analysis.htest.oncozet;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.htest.HtestProcessor;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.ObjectMatrix;
import org.gitools.matrix.model.element.BeanElementAdapter;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.ResourceReference;
import org.gitools.stats.mtc.MTC;
import org.gitools.stats.mtc.MTCFactory;
import org.gitools.stats.test.Test;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.threads.ThreadManager;
import org.gitools.threads.ThreadQueue;
import org.gitools.threads.ThreadSlot;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class OncodriveProcessor extends HtestProcessor
{

    private class RunSlot extends ThreadSlot
    {
        @Nullable
        public DoubleMatrix1D population;
        @Nullable
        public Test test;

        public RunSlot(ThreadQueue threadQueue)
        {
            super(threadQueue);
            population = null;
            test = null;
        }
    }

    private final OncodriveAnalysis analysis;

    public OncodriveProcessor(OncodriveAnalysis analysis)
    {

        this.analysis = analysis;
    }

    @Override
    public void run(@NotNull IProgressMonitor monitor) throws AnalysisException
    {

        Date startTime = new Date();

        TestFactory testFactory = TestFactory.createFactory(analysis.getTestConfig());

        IMatrix dataMatrix = analysis.getData().get();

        String[] labels = new String[dataMatrix.getColumnCount()];
        for (int i = 0; i < labels.length; i++)
            labels[i] = dataMatrix.getColumnLabel(i);

        ModuleMap csmap = analysis.getModuleMap().get();
        if (csmap != null)
        {
            csmap = csmap.remap(labels, analysis.getMinModuleSize(), analysis.getMaxModuleSize());
        }
        else
        {
            csmap = new ModuleMap("All data columns", labels);
        }

        analysis.setModuleMap(new ResourceReference<ModuleMap>("modules", csmap));

        final int numRows = dataMatrix.getRowCount();
        ObjectMatrix1D rowLabels = ObjectFactory1D.dense.make(numRows);
        for (int i = 0; i < numRows; i++)
            rowLabels.setQuick(i, dataMatrix.getRowLabel(i));

        ObjectMatrix1D csetLabels = ObjectFactory1D.dense.make(csmap.getModuleNames());

        int[][] moduleColumnIndices = csmap.getAllItemIndices();

        final int numCsets = csetLabels.size();

        monitor.begin("Running oncodrive analysis...", numRows * numCsets);

        Test test = testFactory.create();

        final ObjectMatrix resultsMatrix = new ObjectMatrix();

        resultsMatrix.setColumns(csetLabels);
        resultsMatrix.setRows(rowLabels);
        resultsMatrix.makeCells();

        resultsMatrix.setCellAdapter(new BeanElementAdapter(test.getResultClass()));

        int numProcs = ThreadManager.getNumThreads();

        ThreadQueue threadQueue = new ThreadQueue(numProcs);

        for (int i = 0; i < numProcs; i++)
            try
            {
                threadQueue.put(new RunSlot(threadQueue));
            } catch (InterruptedException e)
            {
                monitor.debug("InterruptedException while initializing run queue: " + e.getLocalizedMessage());
            }

        final int minCsetSize = analysis.getMinModuleSize();
        final int maxCsetSize = analysis.getMaxModuleSize();

		/* Test analysis */

        for (int csetIndex = 0; csetIndex < numCsets; csetIndex++)
        {

            final int csetIdx = csetIndex;

            final String csetName = csetLabels.getQuick(csetIndex).toString();

            final int[] columnIndices = moduleColumnIndices[csetIndex];

            final IProgressMonitor condMonitor = monitor.subtask();

            final int numColumns = columnIndices.length;

            if (numColumns >= minCsetSize && numColumns <= maxCsetSize)
            {

                condMonitor.begin("Column set " + csetName + "...", numRows);

                DoubleMatrix1D population = DoubleFactory1D.dense.make(numColumns * numRows);

                int k = 0;
                for (int i = 0; i < numRows; i++)
                    for (int j = 0; j < numColumns; j++)
                        population.setQuick(k++, MatrixUtils.doubleValue(dataMatrix.getCellValue(i, columnIndices[j], 0)));

                population = population.viewSelection(notNaNProc);


                final int[] cindices = new int[numColumns];
                for (int i = 0; i < numColumns; i++)
                    cindices[i] = i;

                for (int itemIndex = 0; itemIndex < numRows; itemIndex++)
                {

                    final int itemIdx = itemIndex;

                    final String itemName = rowLabels.getQuick(itemIdx).toString();

                    final DoubleMatrix1D itemValues = DoubleFactory1D.dense.make(numColumns);

                    for (int j = 0; j < numColumns; j++)
                        itemValues.setQuick(j, MatrixUtils.doubleValue(dataMatrix.getCellValue(itemIdx, columnIndices[j], 0)));

                    final RunSlot slot;
                    try
                    {
                        slot = (RunSlot) threadQueue.take();
                    } catch (InterruptedException ex)
                    {
                        throw new AnalysisException(ex);
                    }

                    if (slot.population != population)
                    {
                        slot.population = population;
                        slot.test = testFactory.create();
                        slot.test.processPopulation(csetName, population);
                    }

                    slot.execute(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            CommonResult result = null;
                            try
                            {
                                result = slot.test.processTest(csetName, itemValues, itemName, cindices);
                            } catch (Throwable cause)
                            {
                                cause.printStackTrace();
                            }

                            try
                            {
                                resultsMatrix.setCell(itemIdx, csetIdx, result);
                            } catch (Throwable cause)
                            {
                                cause.printStackTrace();
                            }
                        }
                    });

                    condMonitor.worked(1);
                }
            }
            else
            {
                condMonitor.begin("Column set " + csetName + " discarded.", 1);
            }

            condMonitor.end();

            monitor.worked(1);
        }

        ThreadManager.shutdown(monitor);

		/* Multiple test correction */

        MTC mtc = MTCFactory.createFromName(analysis.getMtc());

        multipleTestCorrection(resultsMatrix, mtc, monitor.subtask());

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

        analysis.setResults(new ResourceReference<ObjectMatrix>("results", resultsMatrix));

        monitor.end();
    }
}
