package es.imim.bg.ztools.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadManager {

	protected static ExecutorService executor = 
		new BlockingThreadPoolExecutor(getAvailableProcessors());
	
	protected static int nThreads;
	
	public static void shutdown() {
		executor.shutdown();
		try {
			while (!executor.isTerminated())
				executor.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static ExecutorService getExecutor() {
		return executor;
	}
	
	public static void setNumThreads(int nThreads) {
		if (ThreadManager.nThreads != nThreads) {
			shutdown();
		
			ThreadManager.nThreads = nThreads;
			ThreadManager.executor = 
				new BlockingThreadPoolExecutor(nThreads);
		}
	}
	
	public static int getNumThreads() {
		return nThreads;
	}
	
	public static int getAvailableProcessors() {
		return Runtime.getRuntime().availableProcessors();
	}
}
