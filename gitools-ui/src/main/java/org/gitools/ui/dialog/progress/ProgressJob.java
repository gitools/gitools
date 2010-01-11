/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.gitools.ui.dialog.progress;

import org.gitools.ui.platform.dialog.ExceptionDialog;
import java.awt.Frame;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;

//TODO It should implement or provide IProgressMonitor
public abstract class ProgressJob {

    private ProgressDialog dlg;

    private Frame parent;

    private boolean cancelled;

    private Thread jobThread;

    private Throwable cause;

    public ProgressJob(Frame parent) {
        this.parent = parent;
        this.cancelled = false;
    }

    public Frame getParent() {
        return parent;
    }

    public synchronized boolean isCancelled() {
        return cancelled;
    }

    public synchronized void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Thread getJobThread() {
        return jobThread;
    }

    public synchronized void setJobThread(Thread jobThread) {
        this.jobThread = jobThread;
    }

    private ProgressDialog getDlg() {
        if (dlg == null) {
            dlg = new ProgressDialog(parent, false);
            dlg.addCancelListener(new ProgressDialog.CancelListener() {
				@Override public void cancelled() {
                    cancelJob();
                }
            });
        }
        return dlg;
    }

    private void setDlg(ProgressDialog dlg) {
        this.dlg = dlg;
    }

    protected void start(final String msg, final int work) {
        SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
                ProgressDialog dlg = getDlg();
                if (!dlg.isVisible())
                    dlg.setVisible(true);
                dlg.setMessage(msg);
                dlg.setWork(work);
                dlg.setProgress(0);
            }
        });
    }

    protected void message(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                getDlg().setMessage(msg);
            }
        });
    }

    protected void progress(final int progress) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                getDlg().setProgress(progress);
            }
        });
    }

    protected void done() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                ProgressDialog dlg = getDlg();
                dlg.setVisible(false);
                dlg.dispose();
                setDlg(null);
            }
        });
    }

    protected void exception(Throwable cause) {
        this.cause = cause;
        setCancelled(true);
    }

    public void execute() {
        jobThread = new Thread() {
            @Override
            public void run() {
                runJob();
                setJobThread(null);

                if (cause != null) {
                    ExceptionDialog ed = new ExceptionDialog(parent, cause);
                    /*JOptionPane.showMessageDialog(parent,
                            "Ha habido un error:\n" +
                            cause.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);*/
                    ed.setVisible(true);
                }
            }
        };

        jobThread.start();
    }

    protected abstract void runJob();

    protected void cancelJob() {
        setCancelled(true);
        
        dlg.setMessage("Cancelling...");

        Timer timer = new Timer("CancellingJob");
        timer.schedule(new TimerTask() {
            @Override public void run() {
                synchronized(this) {
                    Thread jobThread = getJobThread();
                    if (jobThread != null && jobThread.isAlive())
                        jobThread.interrupt();
                }
            }

        }, 250);
    }
}
