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

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.ui.platform.dialog.ExceptionGlassPane;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;

public class JobThread implements JobRunnable {

    private final Window parent;

    private final JobRunnable runnable;
    private CountDownLatch latch;

    private Thread thread;

    private IProgressComponent progressDialog;

    private JobProgressMonitor monitor;

    private static boolean running = false;

    public static void execute(Window parent, JobRunnable runnable) {
        new JobThread(parent, runnable).execute();
    }

    public static void execute(Window parent, JobRunnable runnable, CountDownLatch waitingLatch) {
        new JobThread(parent, runnable, waitingLatch).execute();
    }

    private JobThread(Window parent, JobRunnable runnable) {
        this.parent = parent;
        this.runnable = runnable;
    }

    private JobThread(Window parent, JobRunnable runnable, CountDownLatch latch) {
        this.parent = parent;
        this.runnable = runnable;
        this.latch = latch;
    }

    private JobThread(Window parent, JobRunnable runnable, IProgressComponent dialog) {
        this.parent = parent;
        this.runnable = runnable;
        this.progressDialog = dialog;
    }

    public JobThread(JFrame parent) {
        this.parent = parent;
        this.runnable = this;
    }

    public Window getParent() {
        return parent;
    }

    Thread getThread() {
        return thread;
    }

    synchronized void setThread(Thread jobThread) {
        this.thread = jobThread;
    }

    private synchronized IProgressComponent getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new JobProgressGlassPane(parent, true);
            progressDialog.addCancelListener(new CancelListener() {
                @Override
                public void cancelled() {
                    cancelJob();
                }
            });
        }
        return progressDialog;
    }

    private synchronized void setProgressDialog(IProgressComponent progressDialog) {
        this.progressDialog = progressDialog;
    }

    void cancelJob() {
        getMonitor().cancel();

        progressDialog.setMessage("Cancelling...");

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
        running = false;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                IProgressComponent dlg = getProgressDialog();
                dlg.setVisible(false);
                //TODO progressDialog.dispose();
                setProgressDialog(null);
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
                // notify waiting thread
                if (latch != null) {
                    latch.countDown();
                }
            }
        };
        thread.start();
    }


    private Runnable newRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                JobProgressMonitor m = new JobProgressMonitor(getProgressDialog(), System.out, false, false);

                setMonitor(m);
                org.gitools.utils.progressmonitor.ProgressMonitor.set(m);

                try {

                    monitor.start();
                    running = true;
                    runnable.run(monitor);
                    running = false;
                    monitor.end();

                } catch (CancellationException e) {
                    if (!monitor.isCancelled()) {
                        monitor.exception(e);
                    }
                }
                catch (Throwable cause) {
                    if (!(cause.getCause() instanceof CancellationException && m.isCancelled())) {
                        m.exception(cause);
                    }
                }

                done();

                setThread(null);

                if (monitor.getCause() != null && !(monitor.getCause() instanceof CancellationException && monitor.isCancelled())) {
                    ExceptionGlassPane ed = new ExceptionGlassPane(parent, monitor.getCause());
                    ed.setVisible(true);
                }
            }
        };
    }

    void execute() {
        startThread();

        //getProgressDialog().setModal(true);
        getProgressDialog().setVisible(true);
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

    public static boolean isRunning() {
        return running;
    }
}
