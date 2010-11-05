/*
 *  Copyright 2009 cperez.
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

package edu.upf.bg.benchmark;

public class TimeCounter {

	private boolean paused;

	private long elapsed;
	private long start;

	public TimeCounter() {
		this.elapsed = 0;
		start();
	}

	public void start() {
		paused = false;
		start = System.nanoTime();
	}

	public void pause() {
		paused = true;
		elapsed = getElapsed();
	}

	public void reset() {
		elapsed = 0;
		start = System.nanoTime();
	}

	public long getElapsed() {
		if (!paused)
			return elapsed + (System.nanoTime() - start);
		else
			return elapsed;
	}

	public float getElapsedSeconds() {
		return getElapsed() / 1000000000.0f;
	}

	@Override
	public String toString() {
		long e = getElapsed();
		double millis = e / 1000000.0;
		double secs = e / 1000000000.0;
		double mins = e / 60000000000.0;

		String time = "";
		if (millis < 1000)
			time = Double.toString(millis) + " millisecs";
		else if (secs < 60)
			time = Double.toString(secs) + " secs";
		else
			time = Double.toString(mins) + " mins";

		return time;
	}

}
