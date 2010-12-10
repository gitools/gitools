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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.upf.bg.progressmonitor.NullProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class ThreadManager {

	protected static ExecutorService executor = createExecutor();
	
	protected static int nThreads = getAvailableProcessors();
	
	public static void shutdown(IProgressMonitor monitor) {
		executor.shutdown();
		try {
			while (!executor.isTerminated())
				executor.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			monitor.debug("ThreadManager.shutdown() interrupted by exception: " + e.getMessage());
		}
		
		executor = createExecutor();
	}
	
	private static ExecutorService createExecutor() {
		return Executors.newCachedThreadPool();
		//new BlockingThreadPoolExecutor(nThreads);
	}

	public static ExecutorService getExecutor() {
		return executor;
	}
	
	public static void setNumThreads(int nThreads) {
		if (ThreadManager.nThreads != nThreads) {
			shutdown(new NullProgressMonitor());
		
			ThreadManager.nThreads = nThreads;
			ThreadManager.executor = createExecutor();
		}
	}
	
	public static int getNumThreads() {
		return nThreads;
	}
	
	public static int getAvailableProcessors() {
		return Runtime.getRuntime().availableProcessors();
	}
}
