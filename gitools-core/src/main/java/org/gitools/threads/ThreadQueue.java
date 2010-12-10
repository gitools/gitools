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
