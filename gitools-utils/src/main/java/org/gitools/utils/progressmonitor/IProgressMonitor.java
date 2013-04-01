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

public interface IProgressMonitor
{

    public void begin(String title, int totalWork);

    public void title(String title);

    public void worked(int workInc);

    public void cancel();

    public boolean isCancelled();

    public void end();

    public int getLevel();

    public void setLevel(int level);

    //public int getWorked();

    public IProgressMonitor subtask();

    public void info(String msg);

    public void debug(String msg);

    public void exception(Throwable cause);
}
