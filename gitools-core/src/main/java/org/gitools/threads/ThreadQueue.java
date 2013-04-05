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


import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * @noinspection ALL
 */
public class ThreadQueue
{

    private final ArrayBlockingQueue<ThreadSlot> queue;

    private final ExecutorService executor = ThreadManager.getExecutor();

    public ThreadQueue(int numSlots)
    {
        queue = new ArrayBlockingQueue<ThreadSlot>(numSlots);
    }

    public void put(ThreadSlot threadSlot) throws InterruptedException
    {
        queue.put(threadSlot);
    }

    public ThreadSlot take() throws InterruptedException
    {
        return queue.take();
    }

    public void offer(ThreadSlot threadSlot)
    {
        queue.offer(threadSlot);
    }

    public void execute(final ThreadSlot threadSlot, @NotNull final Runnable runnable)
    {

        executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                runnable.run();
                queue.offer(threadSlot);
            }
        });
    }
}
