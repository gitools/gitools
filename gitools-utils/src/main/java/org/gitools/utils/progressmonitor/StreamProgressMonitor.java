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

import cern.colt.Timer;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;

public class StreamProgressMonitor extends DefaultProgressMonitor
{

    private static final String indentString = "  ";

    protected PrintStream out;

    protected Timer timer;
    protected int lastprogress;

    protected String tabs;
    protected boolean flag;

    protected boolean verbose;
    protected boolean debug;

    public StreamProgressMonitor(IProgressMonitor parent, PrintStream out, boolean verbose, boolean debug)
    {
        super(parent);
        this.out = out;
        this.timer = new Timer();
        this.lastprogress = 0;
        this.flag = false;
        this.tabs = "";
        this.verbose = verbose;
        this.debug = debug;
    }

    public StreamProgressMonitor(PrintStream out, boolean verbose, boolean debug)
    {
        this(null, out, verbose, debug);
    }

    @Override
    public void begin(String title, int totalWork)
    {
        super.begin(title, totalWork);
        flag = false;
        tabs = tabbulate(level);
        print("\n" + tabs + title);
        timer.start();
    }

    @Override
    public void title(String title)
    {
        super.title(title);
        print("\n" + tabs + title);
    }

    @Override
    public void worked(int workInc)
    {
        super.worked(workInc);
        /*int progress = (worked * 100 / totalWork);
        if (lastprogress != progress) {
			lastprogress = progress;
			out.println(tabbulate(title + " " + progress + "%", level));
		}*/
    }

    @NotNull
    @Override
    public IProgressMonitor subtask()
    {
        IProgressMonitor subtask =
                createSubtaskMonitor(this, out, verbose, debug);
        subtask.setLevel(level + 1);
        flag = true;
        return subtask;
    }

    @NotNull
    protected IProgressMonitor createSubtaskMonitor(
            IProgressMonitor parentMonitor,
            PrintStream out,
            boolean verbose,
            boolean debug)
    {

        return new StreamProgressMonitor(
                parentMonitor, out, verbose, debug);
    }

    @Override
    public void end()
    {
        super.end();

        double millis = timer.millis();
        double secs = timer.seconds();
        double mins = timer.minutes();

        String time = "";
        if (millis < 1000)
        {
            time = Double.toString(millis) + " millisecs";
        }
        else if (secs < 60)
        {
            time = Double.toString(secs) + " secs";
        }
        else
        {
            time = Double.toString(mins) + " mins";
        }

        if (flag)
        {
            print("\n" + tabs + title + " " + time);
        }
        else
        {
            print(" " + time);
        }
    }

    @Override
    public void exception(@NotNull Throwable cause)
    {
        super.exception(cause);

        print("\n\nEXCEPTION: " + cause.getLocalizedMessage());
    }

    @Override
    public void info(String msg)
    {
        if (verbose)
        {
            log(msg);
        }
    }

    @Override
    public void debug(String msg)
    {
        if (debug)
        {
            log(msg);
        }
    }

    private void log(String msg)
    {
        print("\n" + tabs + indentString + msg);
        flag = true;
    }

    protected void print(String text)
    {
        out.print(text);
    }

    @NotNull
    private String tabbulate(int level)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++)
            sb.append(indentString);
        return sb.toString();
    }
}
