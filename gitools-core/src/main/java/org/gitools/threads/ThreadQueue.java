package org.gitools.threads;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

public class ThreadQueue {

	private ArrayBlockingQueue<ThreadSlot> queue;
	
	final ExecutorService executor = ThreadManager.getExecutor();

	public ThreadQueue(int numSlots) {
		queue = new ArrayBlockingQueue<ThreadSlot>(numSlots);
	}

	public void put(ThreadSlot threadSlot) throws InterruptedException {
		queue.put(threadSlot);
	}
	
	public ThreadSlot take() throws InterruptedException {
		return queue.take();
	}
	
	public void offer(ThreadSlot threadSlot) {
		queue.offer(threadSlot);
	}

	public void execute(
			final ThreadSlot threadSlot, 
			final Runnable runnable) {
		
		executor.execute(new Runnable() {
			@Override
			public void run() {
				runnable.run();
				queue.offer(threadSlot);
			}
		});
	}	
}
