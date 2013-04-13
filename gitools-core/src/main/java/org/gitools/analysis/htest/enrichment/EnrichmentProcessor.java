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
import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;
import cern.jet.math.Functions;
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

/* Notes:
 * 'cond' is an abbreviation for condition.
 */

public class EnrichmentProcessor extends HtestProcessor
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

    private final EnrichmentAnalysis analysis;

    public EnrichmentProcessor(EnrichmentAnalysis analysis)
    {

        this.analysis = analysis;
    }

    @Override
    public void run(@NotNull IProgressMonitor monitor) throws AnalysisException
    {

        Date startTime = new Date();

        TestFactory testFactory = TestFactory.createFactory(analysis.getTestConfig());

        IMatrix dataMatrix = analysis.getData().get();

        final int numConditions = dataMatrix.getColumns().size();
        final int numRows = dataMatrix.getRows().size();

        String[] labels = new String[numRows];
        for (int i = 0; i < labels.length; i++)
            labels[i] = dataMatrix.getRows().getLabel(i);

        ModuleMap mmap = analysis.getModuleMap().get();
        mmap = mmap.remap(labels, analysis.getMinModuleSize(), analysis.getMaxModuleSize());

        //DoubleMatrix2D data = null;
        ObjectMatrix1D conditions = ObjectFactory1D.dense.make(dataMatrix.getColumns().size());
        for (int i = 0; i < numConditions; i++)
            conditions.setQuick(i, dataMatrix.getColumns().getLabel(i));

        ObjectMatrix1D modules = ObjectFactory1D.dense.make(mmap.getModuleNames());

        int[][] moduleItemIndices = mmap.getAllItemIndices();

        final int numModules = modules.size();

        monitor.begin("Running enrichment analysis...", numConditions);

        Test test = testFactory.create();

        final ObjectMatrix resultsMatrix = new ObjectMatrix();

        resultsMatrix.setColumns(conditions);
        resultsMatrix.setRows(modules);
        resultsMatrix.makeCells();

        resultsMatrix.setObjectCellAdapter(new BeanElementAdapter(test.getResultClass()));

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

        final int minModuleSize = analysis.getMinModuleSize();
        final int maxModuleSize = analysis.getMaxModuleSize();

		/* Test analysis */

        for (int condIndex = 0; condIndex < numConditions && !monitor.isCancelled(); condIndex++)
        {

            //final String condName = conditions.getQuick(condIndex).toString();
            final String condName = dataMatrix.getColumns().getLabel(condIndex);

            //final DoubleMatrix1D condItems = data.viewRow(condIndex);
            final DoubleMatrix1D condItems = DoubleFactory1D.dense.make(numRows);
            for (int i = 0; i < numRows; i++)
            {
                double value = MatrixUtils.doubleValue(dataMatrix.getCellValue(i, condIndex, 0));

                condItems.setQuick(i, value);
            }

            DoubleMatrix1D population = condItems.viewSelection(notNaNProc);

            final IProgressMonitor condMonitor = monitor.subtask();

            condMonitor.begin("Condition " + condName + "...", numModules);

            for (int moduleIndex = 0; moduleIndex < numModules && !monitor.isCancelled(); moduleIndex++)
            {

                final String moduleName = modules.getQuick(moduleIndex).toString();
                final int[] itemIndices = moduleItemIndices[moduleIndex];

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
                    slot.test.processPopulation(condName, population);
                }

                final int condIdx = condIndex;
                final int moduleIdx = moduleIndex;

                slot.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        CommonResult result = null;
                        try
                        {
                            int moduleSize = (int) condItems.viewSelection(itemIndices).aggregate(Functions.plus, new DoubleFunction()
                            {
                                @Override
                                public double apply(double d)
                                {
                                    return Double.isNaN(d) ? 0 : 1;
                                }
                            });

                            if (moduleSize >= minModuleSize && moduleSize <= maxModuleSize)
                            {
                                result = slot.test.processTest(condName, condItems, moduleName, itemIndices);
                            }
                        } catch (Throwable cause)
                        {
                            cause.printStackTrace();
                        }

                        try
                        {
                            resultsMatrix.setCell(moduleIdx, condIdx, result);
                        } catch (Throwable cause)
                        {
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

        if (monitor.isCancelled())
        {
            return;
        }

		/* Multiple test correction */

        MTC mtc = MTCFactory.createFromName(analysis.getMtc());

        multipleTestCorrection(resultsMatrix, mtc, monitor.subtask());

        analysis.setStartTime(startTime);
        analysis.setElapsedTime(new Date().getTime() - startTime.getTime());

        analysis.setResults(new ResourceReference<ObjectMatrix>("results", resultsMatrix));

        monitor.end();
    }
}
