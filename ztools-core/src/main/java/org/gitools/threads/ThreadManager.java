package org.gitools.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import es.imim.bg.progressmonitor.NullProgressMonitor;
import es.imim.bg.progressmonitor.ProgressMonitor;

public class ThreadManager {

	protected static ExecutorService executor = createExecutor();
	
	protected static int nThreads = getAvailableProcessors();
	
	public static void shutdown(ProgressMonitor monitor) {
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
