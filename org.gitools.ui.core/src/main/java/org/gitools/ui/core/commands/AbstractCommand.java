/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.core.commands;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.utils.progressmonitor.NullProgressMonitor;


public abstract class AbstractCommand implements Command, JobRunnable, Runnable {

    @Override
    public abstract void execute(IProgressMonitor monitor) throws CommandException;

    private int exitStatus = -1;

    @Override
    public void run(IProgressMonitor monitor) {
        try {
            execute(monitor);
        } catch (CommandException e) {
            monitor.exception(e);
        }
    }

    @Override
    public void run() {
        run(new NullProgressMonitor());
    }

    @Override
    public int getExitStatus() {
        return exitStatus;
    }

    protected void setExitStatus(int exitStatus) {
        this.exitStatus = exitStatus;
    }
}
