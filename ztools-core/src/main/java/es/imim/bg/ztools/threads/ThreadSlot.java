package es.imim.bg.ztools.threads;

public class ThreadSlot {

	private ThreadQueue threadQueue;
	
	public ThreadSlot(ThreadQueue threadQueue) {
		
		this.threadQueue = threadQueue;
	}
	
	public void execute(Runnable runnable) {
		threadQueue.execute(this, runnable);
	}
}
