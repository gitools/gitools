/*
 * #%L
 * gitools-utils
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.utils.benchmark;

public class TimeCounter {

    private boolean paused;

    private long elapsed;
    private long start;

    public TimeCounter() {
        this.elapsed = 0;
        start();
    }

    void start() {
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

    long getElapsed() {
        if (!paused) {
            return elapsed + (System.nanoTime() - start);
        } else {
            return elapsed;
        }
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
        if (millis < 1000) {
            time = Double.toString(millis) + " millisecs";
        } else if (secs < 60) {
            time = Double.toString(secs) + " secs";
        } else {
            time = Double.toString(mins) + " mins";
        }

        return time;
    }

}
