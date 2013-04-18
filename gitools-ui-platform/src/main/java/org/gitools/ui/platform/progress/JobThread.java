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

import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @noinspection ALL
 */
public class JobThread implements JobRunnable {

    private final Window parent;

    private final JobRunnable runnable;

    private Thread thread;

    private JobProgressDialog dlg;

    private JobProgressMonitor monitor;

    public static void execute(Window parent, JobRunnable runnable) {
        new JobThread(parent, runnable).execute();
    }

    public static void executeAndWait(Window parent, JobRunnable runnable) {
        new JobThread(parent, runnable).executeAndWait();
    }

    private JobThread(Window parent, JobRunnable runnable) {
        this.parent = parent;
        this.runnable = runnable;
    }

    public JobThread(Window parent) {
        this.parent = parent;
        this.runnable = this;
    }

    public Window getParent() {
        return parent;
    }

    /*public synchronized boolean isCancelled() {
        return cancelled;
    }

    public synchronized void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }*/

    Thread getThread() {
        return thread;
    }

    synchronized void setThread(Thread jobThread) {
        this.thread = jobThread;
    }

    private synchronized JobProgressDialog getDlg() {
        if (dlg == null) {
            dlg = new JobProgressDialog(parent, false);
            dlg.addCancelListener(new JobProgressDialog.CancelListener() {
                @Override
                public void cancelled() {
                    cancelJob();
                }
            });
        }
        return dlg;
    }

    private synchronized void setDlg(JobProgressDialog dlg) {
        this.dlg = dlg;
    }

    void cancelJob() {
        getMonitor().cancel();

        dlg.setMessage("Cancelling...");

        Timer timer = new Timer("JobThread.cancelJob");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (JobThread.this) {
                    Thread jobThread = getThread();
                    if (jobThread != null && jobThread.isAlive()) {
                        jobThread.interrupt();
                    }
                }
            }

        }, 250);
    }

    void done() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JobProgressDialog dlg = getDlg();
                dlg.setVisible(false);
                dlg.dispose();
                setDlg(null);
            }
        });
    }

    synchronized JobProgressMonitor getMonitor() {
        return monitor;
    }

    synchronized void setMonitor(JobProgressMonitor monitor) {
        this.monitor = monitor;
    }

    void startThread() {
        thread = new Thread("JobThread") {
            @Override
            public void run() {
                newRunnable().run();
            }
        };
        thread.start();
    }

    @Nullable
    private Runnable newRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                JobProgressMonitor m = new JobProgressMonitor(getDlg(), System.out, false, false);

                setMonitor(m);
                org.gitools.utils.progressmonitor.ProgressMonitor.set(m);

                try {
                    runnable.run(monitor);
                } catch (Throwable cause) {
                    m.exception(cause);
                }

                done();

                setThread(null);

                if (monitor.getCause() != null) {
                    ExceptionDialog ed = new ExceptionDialog(parent, monitor.getCause());
                    ed.setVisible(true);
                }
            }
        };
    }

    void execute() {
        startThread();

        getDlg().setModal(true);
        getDlg().setVisible(true);
    }

    void executeAndWait() {

        getDlg().setModal(true);
        getDlg().setVisible(true);

        try {
            SwingUtilities.invokeAndWait(newRunnable());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public boolean isCancelled() {
        return getMonitor().isCancelled();
    }

    public Throwable getCause() {
        return getMonitor().getCause();
    }

    @Override
    public void run(IProgressMonitor monitor) {
        throw new UnsupportedOperationException("Opperation should be overrided");
    }
}
