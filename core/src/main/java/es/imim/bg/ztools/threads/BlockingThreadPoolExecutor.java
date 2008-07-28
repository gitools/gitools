package es.imim.bg.ztools.threads;

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
