package es.imim.bg.ztools.ui.jobs;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class JobProcessor {

	protected BlockingQueue<Job> queue;
	
	public JobProcessor() {
		queue = new LinkedBlockingQueue<Job>();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					jobScheduller();
				} catch (InterruptedException e) {
					e.printStackTrace(); //FIXME
				}
			}
		}, "JobProcessor").start();
	}
	
	public void addJob(Job job) {
		try {
			queue.put(job);
		} catch (InterruptedException e) {
			e.printStackTrace(); //FIXME
		}
	}
	
	private void jobScheduller() throws InterruptedException {
		Job job = null;
		
		while ((job = queue.take()) != null) {
			job.run();
		}
	}
}
