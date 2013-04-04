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
package org.gitools.threads;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.NullProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadManager
{

    @NotNull
    protected static ExecutorService executor = createExecutor();

    protected static int nThreads = getAvailableProcessors();

    public static void shutdown(@NotNull IProgressMonitor monitor)
    {
        executor.shutdown();
        try
        {
            while (!executor.isTerminated())
                executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e)
        {
            monitor.debug("ThreadManager.shutdown() interrupted by exception: " + e.getMessage());
        }

        executor = createExecutor();
    }

    @NotNull
    private static ExecutorService createExecutor()
    {
        return Executors.newCachedThreadPool();
        //new BlockingThreadPoolExecutor(nThreads);
    }

    @NotNull
    public static ExecutorService getExecutor()
    {
        return executor;
    }

    public static void setNumThreads(int nThreads)
    {
        if (ThreadManager.nThreads != nThreads)
        {
            shutdown(new NullProgressMonitor());

            ThreadManager.nThreads = nThreads;
            ThreadManager.executor = createExecutor();
        }
    }

    public static int getNumThreads()
    {
        return nThreads;
    }

    public static int getAvailableProcessors()
    {
        return Runtime.getRuntime().availableProcessors();
    }
}
