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
package org.gitools.utils.tools;

import org.gitools.utils.tools.exception.ToolException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.PrintStream;

/**
 * Interface implemented by each tool.
 */
public interface ToolLifeCycle<Context> {

    /**
     * Initialize tool.
     *
     * @throws ToolException
     */
    void initialize(Context context) throws ToolException;

    /**
     * Validate arguments.
     * If there is any error throw an exception otherwise return.
     *
     * @param argsObject Parsed command line arguments
     * @throws ToolException
     */
    void validate(Object argsObject) throws ToolException;

    /**
     * It's the entry point of the tool in the case that
     * arg4j arguments object is used.
     *
     * @param argsObject Parsed command line arguments
     * @throws ToolException
     */
    void run(Object argsObject) throws ToolException;

    /**
     * Undo initialization, if needed.
     *
     * @throws ToolException
     */
    void uninitialize() throws ToolException;

    /**
     * Print tool usage
     *
     * @param appName  command line name
     * @param toolDesc tool description
     * @param parser   command line parser
     */
    public void printUsage(PrintStream outputStream, String appName, ToolDescriptor toolDesc, CmdLineParser parser);
}
