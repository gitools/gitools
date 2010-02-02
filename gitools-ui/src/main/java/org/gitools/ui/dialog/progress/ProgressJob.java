/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.gitools.ui.dialog.progress;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import edu.upf.bg.progressmonitor.NullProgressMonitor;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import java.awt.Frame;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;

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

    private synchronized ProgressDialog getDlg() {
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

    private synchronized void setDlg(ProgressDialog dlg) {
        this.dlg = dlg;
    }

	protected IProgressMonitor getProgressMonitor() {
		//return new NullProgressMonitor(); //TODO
		return new ProgressJobMonitor(getDlg(), System.out, true, false);
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

    public void asyncExecute() {
        jobThread = new Thread() {
            @Override
            public void run() {
                runJob();

				done();

                setJobThread(null);

                if (cause != null) {
                    ExceptionDialog ed = new ExceptionDialog(parent, cause);
                    ed.setVisible(true);
                }
            }
        };

        jobThread.start();
    }

	public void execute() {
		asyncExecute();

		getDlg().setModal(true);
		getDlg().setVisible(true);
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
