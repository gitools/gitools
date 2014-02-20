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
package org.gitools.utils.progressmonitor;

import org.gitools.api.analysis.IProgressMonitor;

import java.io.PrintStream;

public class StreamProgressMonitor extends DefaultProgressMonitor {

    private static final String indentString = "  ";

    private final PrintStream out;

    private long timer;

    private String tabs;
    private boolean flag;
    private boolean showingbar = false;

    private final boolean verbose;
    private final boolean debug;

    protected StreamProgressMonitor(IProgressMonitor parent, PrintStream out, boolean verbose, boolean debug) {
        super(parent);
        this.out = out;
        this.flag = false;
        this.tabs = "";
        this.verbose = verbose;
        this.debug = debug;
    }

    public StreamProgressMonitor(PrintStream out, boolean verbose, boolean debug) {
        this(null, out, verbose, debug);
    }

    @Override
    public void begin(String title, long totalWork) {
        super.begin(title, totalWork);
        flag = false;
        showingbar = false;
        tabs = tabbulate(level);
        print("\n" + tabs + title);
    }

    @Override
    public void start() {
        timer = System.currentTimeMillis();
    }

    @Override
    public void title(String title) {
        super.title(title);
        print("\n" + tabs + title);
    }

    @Override
    public void worked(long workInc) {
        super.worked(workInc);
        double progress = ((double) getWorked() / (double) totalWork);

        if (!showingbar) {
            print("\n");
            showingbar = true;
        }

        int width = 70;
        StringBuilder bar = new StringBuilder(width + 5);
        bar.append("\r[");
        int i = 0;
        for (; i <= (int) (progress * width); i++) {
            bar.append(".");
        }
        for (; i < width; i++) {
            bar.append(" ");
        }
        bar.append("] ").append((int) (progress * 100)).append("%");
        print(bar.toString());
    }


    @Override
    public IProgressMonitor subtask() {
        IProgressMonitor subtask = createSubtaskMonitor(this, out, verbose, debug);
        subtask.setLevel(level + 1);
        flag = true;
        return subtask;
    }


    protected IProgressMonitor createSubtaskMonitor(IProgressMonitor parentMonitor, PrintStream out, boolean verbose, boolean debug) {
        return new StreamProgressMonitor(parentMonitor, out, verbose, debug);
    }

    @Override
    public void end() {
        super.end();

        long millis = System.currentTimeMillis() - timer;
        long secs = (millis / 1000) % 60;
        long mins = millis / (60 * 1000);

        String time = "";
        if (millis < 10000) {
            time = Double.toString(millis) + " millisecs";
        } else {
            time = Double.toString(mins) + " mins - " + Double.toString(secs) + " secs";
        }

        if (flag) {
            print("\n" + tabs + title + " " + time);
        } else {
            print("\n " + time);
        }

        print("\n");
    }

    @Override
    public void exception(Throwable cause) {
        super.exception(cause);

        print("\n\nEXCEPTION: " + cause.getLocalizedMessage());
    }

    @Override
    public void info(String msg) {
        if (verbose) {
            log(msg);
        }
    }

    @Override
    public void debug(String msg) {
        if (debug) {
            log(msg);
        }
    }

    private void log(String msg) {
        print("\n" + tabs + indentString + msg);
        flag = true;
    }

    protected void print(String text) {
        out.print(text);
    }


    private String tabbulate(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++)
            sb.append(indentString);
        return sb.toString();
    }
}
