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
package org.gitools.ui.commands;

import org.gitools.utils.progressmonitor.IProgressMonitor;

/**
 * @noinspection ALL
 */
public interface Command
{

    public class CommandException extends Exception
    {
        private static final long serialVersionUID = 2147640402258540409L;

        public CommandException(Exception e)
        {
            super(e);
        }

        public CommandException(String msg)
        {
            super(msg);
        }

        public CommandException(String msg, Throwable cause)
        {
            super(msg, cause);
        }
    }

    void execute(IProgressMonitor monitor) throws CommandException;
}
