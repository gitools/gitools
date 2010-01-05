package org.gitools.threads;

public class ThreadSlot implements Runnable {

	private ThreadQueue threadQueue;
	
	public ThreadSlot(ThreadQueue threadQueue) {
		
		this.threadQueue = threadQueue;
	}
	
	public void execute(Runnable runnable) {
		threadQueue.execute(this, runnable);
	}

	@Override
	public void run() {
	}
}
