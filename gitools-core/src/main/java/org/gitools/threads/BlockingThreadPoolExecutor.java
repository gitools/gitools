/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.threads;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BlockingThreadPoolExecutor extends ThreadPoolExecutor {

	protected Semaphore sem;
	
	public BlockingThreadPoolExecutor(int corePoolSize) {
		super(corePoolSize, corePoolSize, 100, TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue<Runnable>());
		
		init(corePoolSize);
	}
	
	public BlockingThreadPoolExecutor(
			int corePoolSize, long keepAliveTime, TimeUnit unit,
			ThreadFactory threadFactory,
			RejectedExecutionHandler handler) {
		
		super(corePoolSize, corePoolSize, keepAliveTime, unit, 
				new LinkedBlockingQueue<Runnable>(), threadFactory, handler);
		
		init(corePoolSize);
	}
	
	private void init(int corePoolSize) {
		this.sem = new Semaphore(corePoolSize);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		
		sem.release();
	}
	
	@Override
	public void execute(Runnable command) {
		sem.acquireUninterruptibly();
		
		super.execute(command);
	}
}
