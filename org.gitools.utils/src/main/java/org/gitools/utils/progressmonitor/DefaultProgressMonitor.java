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

public class DefaultProgressMonitor implements IProgressMonitor {

    String title;
    protected long totalWork;
    private long worked;
    protected int level;
    private boolean cancelled;

    private Throwable cause;

    private IProgressMonitor parent;

    public DefaultProgressMonitor() {
        title = "";
        totalWork = worked = level = 0;
        cancelled = false;
    }

    DefaultProgressMonitor(IProgressMonitor parent) {
        this.parent = parent;
    }

    @Override
    public void begin(String title, long totalWork) {
        this.title = title;
        this.totalWork = totalWork;
        this.worked = 0;
        this.cancelled = false;
    }

    @Override
    public void title(String title) {
        this.title = title;
    }

    @Override
    public void worked(long workInc) {
        worked += workInc;
        if (worked > totalWork) {
            worked = totalWork;
        }
        //System.out.println("\t"+worked);
    }

    @Override
    public boolean isCancelled() {
        return (parent != null && parent.isCancelled()) || cancelled;
    }

    @Override
    public void start() {

    }

    @Override
    public void cancel() {
        this.cancelled = true;
    }

    @Override
    public void end() {
        worked = totalWork;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    protected long getWorked() {
        return worked;
    }

    @Override
    public IProgressMonitor subtask() {
        return new DefaultProgressMonitor(this);
    }

    @Override
    public void debug(String msg) {
        System.out.println(msg);
    }

    @Override
    public void info(String msg) {
    }

    @Override
    public void exception(Throwable cause) {
        this.cause = cause;
        this.cancelled = true;
    }

    public Throwable getCause() {
        return cause;
    }
}
