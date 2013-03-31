package org.gitools.ui.commands;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.NullProgressMonitor;
import org.gitools.ui.platform.progress.JobRunnable;


public abstract class AbstractCommand implements Command, JobRunnable, Runnable {

    @Override
    public abstract void execute(IProgressMonitor monitor) throws CommandException;

    @Override
    public void run(IProgressMonitor monitor) {
        try {
            execute(monitor);
            monitor.end();
        } catch (CommandException e) {
            monitor.exception(e);
        }
    }

    @Override
    public void run() {
        run(new NullProgressMonitor() );
    }
}
