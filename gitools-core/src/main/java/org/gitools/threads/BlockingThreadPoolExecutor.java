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

import java.util.concurrent.*;

public class BlockingThreadPoolExecutor extends ThreadPoolExecutor
{

    protected Semaphore sem;

    public BlockingThreadPoolExecutor(int corePoolSize)
    {
        super(corePoolSize, corePoolSize, 100, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        init(corePoolSize);
    }

    public BlockingThreadPoolExecutor(
            int corePoolSize, long keepAliveTime, TimeUnit unit,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler)
    {

        super(corePoolSize, corePoolSize, keepAliveTime, unit,
                new LinkedBlockingQueue<Runnable>(), threadFactory, handler);

        init(corePoolSize);
    }

    private void init(int corePoolSize)
    {
        this.sem = new Semaphore(corePoolSize);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t)
    {
        super.afterExecute(r, t);

        sem.release();
    }

    @Override
    public void execute(Runnable command)
    {
        sem.acquireUninterruptibly();

        super.execute(command);
    }
}
