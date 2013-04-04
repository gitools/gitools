/*
 * #%L
 * gitools-ui-platform
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
package org.gitools.ui.platform.progress;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.PrintStream;

public class JobProgressMonitor extends StreamProgressMonitor
{

    private final JobProgressDialog dlg;

    public JobProgressMonitor(JobProgressDialog dlg, PrintStream out, boolean verbose, boolean debug)
    {
        super(out, verbose, debug);

        this.dlg = dlg;
    }

    private JobProgressMonitor(IProgressMonitor parentMonitor, JobProgressDialog dlg,
                               PrintStream out, boolean verbose, boolean debug)
    {

        super(parentMonitor, out, verbose, debug);

        this.dlg = dlg;
    }

    @Override
    public void begin(final String title, final int totalWork)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                if (!dlg.isVisible())
                {
                    dlg.setVisible(true);
                }
                dlg.setMessage(title);
                dlg.setWork(totalWork);
                dlg.setProgress(0);
            }
        });

        super.begin(title, totalWork);
    }

    @Override
    public void title(final String title)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                dlg.setMessage(title);
            }
        });

        super.title(title);
    }

    @Override
    public void worked(int workInc)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                dlg.setWork(totalWork);
                dlg.setProgress(getWorked());
            }
        });

        super.worked(workInc);
    }

    @Override
    public void end()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                dlg.setWork(totalWork);
                dlg.setProgress(getTotalWork());
                if (level == 0)
                {
                    dlg.dispose();
                }
            }
        });

        super.end();
    }

    @Override
    public void info(final String msg)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                dlg.setInfo(msg);
            }
        });

        super.info(msg);
    }

    @Override
    protected void print(String text)
    {
        super.print(text);

        //TODO dlg.append(text);
    }

    @NotNull
    @Override
    protected IProgressMonitor createSubtaskMonitor(
            IProgressMonitor parentMonitor,
            PrintStream out,
            boolean verbose,
            boolean debug)
    {

        return new JobProgressMonitor(
                parentMonitor, dlg, out, verbose, debug);
    }
}
